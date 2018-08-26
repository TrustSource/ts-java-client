/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

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

//    public RestClient(String baseUrl, String apiPath, Properties properties, String userAgent) {
//        this(baseUrl, apiPath, properties, userAgent, createClient());
//    }

    public RestClient(Properties properties, String userAgent) {
        this(properties, userAgent, createClient());
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
                    header("X-User", this.properties.getProperty("userName")).
                    buildPost(Entity.json(scan)).invoke();

        responseStatus = response.getStatus();
        return response.readEntity(String.class);
    }


    public CheckResults checkScan(Scan scan) throws Exception {
        Response response =
                client.target(baseUrl).path(apiPath).path("check_component").
                        request(MediaType.APPLICATION_JSON_TYPE).
                        header("User-Agent", this.userAgent).
                        header("X-ApiKey", this.properties.getProperty("apiKey")).
                        header("X-User", this.properties.getProperty("userName")).
                        buildPost(Entity.json(Check.from(scan))).invoke();

        responseStatus = response.getStatus();

        if (responseStatus == 200) {
            //System.out.print(response.readEntity(String.class));
            return response.readEntity(CheckResults.class);
        } else {
            return null;
        }

    }


    public int getResponseStatus() {
        return responseStatus;
    }

    private static Client createClient() {
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(
                ProxySelector.getDefault());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();

        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);

        return new ResteasyClientBuilder().httpEngine(engine).build();
    }
}
