/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.client.jaxrs.engines.HttpContextProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ProxySelector;
import java.util.Properties;

public class RestClient {

    private final String baseUrl;
    private final String apiPath;
    private final String userAgent;

    private final Client client;
    private final Properties properties;

    private int responseStatus = -1;

    public static class RestClientException extends Exception {
        public RestClientException(String message) {
            super(message);
        }
    }


//    public RestClient(String baseUrl, String apiPath, Properties properties, String userAgent) {
//        this(baseUrl, apiPath, properties, userAgent, createClient());
//    }

    public RestClient(Properties properties, String userAgent) {
        this(properties, userAgent, createClient(properties));
    }


    /* package private constructor,mainly for testing purpose */
    RestClient(Properties properties, String userAgent, Client client) {
        this(properties.getProperty("baseUrl"),
                properties.getProperty("apiPath"),
                properties,
                userAgent,
                client);
    }

    /* package private constructor,mainly for testing purpose */
    RestClient(String baseUrl, String apiPath, Properties properties, String userAgent, Client client) {
        this.baseUrl = baseUrl;
        this.apiPath = apiPath;
        this.properties = properties;
        this.userAgent = userAgent;
        this.client = client;
    }

    public String transferScan(Scan scan) throws Exception {
        Response response =
            client.target(baseUrl).path(apiPath).path("scans").
                    request(MediaType.APPLICATION_JSON_TYPE).
                    header("User-Agent", this.userAgent).
                    header("X-ApiKey", this.properties.getProperty("apiKey")).
                    buildPost(Entity.json(scan)).invoke();

        responseStatus = response.getStatus();
        return response.readEntity(String.class);
    }


    public CheckResults checkScan(Scan scan) throws RestClientException {
        Response response =
                client.target(baseUrl).path(apiPath).path("check_component").
                        request(MediaType.APPLICATION_JSON_TYPE).
                        header("User-Agent", this.userAgent).
                        header("X-ApiKey", this.properties.getProperty("apiKey")).
                        buildPost(Entity.json(Check.from(scan))).invoke();

        responseStatus = response.getStatus();

        if (responseStatus == 200) {
            return response.readEntity(CheckResults.class);
        } else if (responseStatus >= 400 && responseStatus < 500) {
            CheckError err = response.readEntity(CheckError.class);
            throw new RestClientException(err.getError());
        } else {
            throw new RestClientException("Calling Rest API failed with error code " + responseStatus);
        }
    }


    public int getResponseStatus() {
        return responseStatus;
    }



    private static Client createClient(Properties properties) {
        String proxyUrl = properties.getProperty("proxyUrl", "");
        String proxyPort = properties.getProperty("proxyPort", "8080");
        String proxyUser = properties.getProperty("proxyUser", "");
        String proxyPass = properties.getProperty("proxyPass", "");

        ApacheHttpClient4Engine engine;

        if (!proxyUrl.equals("")) {
            HttpHost proxy = new HttpHost(proxyUrl, Integer.valueOf(proxyPort));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

            CredentialsProvider credentialsProvider = null;
            HttpContextProvider contextProvider = null;

            if(!proxyUser.equals("")) {
                credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(proxy),
                        new UsernamePasswordCredentials(proxyUser,  proxyPass));

                AuthCache authCache = new BasicAuthCache();
                BasicScheme basicAuth = new BasicScheme();

                authCache.put(proxy, basicAuth);

                final HttpClientContext context = HttpClientContext.create();
                context.setCredentialsProvider(credentialsProvider);
                context.setAuthCache(authCache);

                contextProvider = () -> context;
            }

            if(credentialsProvider != null) {
                CloseableHttpClient httpClient = HttpClients.custom()
                        .setRoutePlanner(routePlanner)
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .build();

                engine = new ApacheHttpClient4Engine(httpClient, contextProvider);

            } else {
                CloseableHttpClient httpClient = HttpClients.custom()
                        .setRoutePlanner(routePlanner)
                        .build();

                engine = new ApacheHttpClient4Engine(httpClient);
            }

        } else {
            SystemDefaultRoutePlanner routePlanner =
                    new SystemDefaultRoutePlanner(ProxySelector.getDefault());

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();

            engine = new ApacheHttpClient4Engine(httpClient);

        }

        return new ResteasyClientBuilder().httpEngine(engine).build();
    }
}
