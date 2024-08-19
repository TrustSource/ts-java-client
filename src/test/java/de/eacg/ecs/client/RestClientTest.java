/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



public class RestClientTest {

    private Client client;
    private WebTarget webTarget;
    private Invocation.Builder builder;
    private Invocation invocation;
    private Response response;

    @Before
    public void setUp() throws Exception {
        client = Mockito.mock(Client.class);
        webTarget = Mockito.mock(WebTarget.class);
        builder = Mockito.mock(Invocation.Builder.class);
        invocation = Mockito.mock(Invocation.class);
        response = Mockito.mock(Response.class);
    }


    @Test
    public void transfer() throws Exception {
        Dependency.Builder db = new Dependency.Builder().setName("testDependency").setKey("test:key");
        Scan scan = new Scan(
                "test-project", "test-module", "test-module", "test-branch", "test-tag",
                Collections.singletonList(db.buildDependency()));

        JsonProperties config = new JsonProperties((String)null);
        config.setBaseUrl("http://test.com");
        config.setApiPath("api/v2.0/");
        config.setApiKey("12345");

        RestClient ecsClient = new RestClient(config, "userAgent", client);

        Mockito.when(client.target("http://test.com")).thenReturn(webTarget);
        Mockito.when(webTarget.path("api/v2.0/")).thenReturn(webTarget);
        Mockito.when(webTarget.path("core/scans")).thenReturn(webTarget);
        Mockito.when(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
        Mockito.when(builder.header("User-Agent", "userAgent")).thenReturn(builder);
        Mockito.when(builder.header("X-ApiKey", "12345")).thenReturn(builder);
        Mockito.when(builder.header("X-User", "John Smith")).thenReturn(builder);
        Mockito.when(builder.buildPost(Mockito.any(Entity.class))).thenReturn(invocation);
        Mockito.when(invocation.invoke()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(String.class)).thenReturn("Ok");


        String result = ecsClient.transferScan(scan);

        assertEquals("Ok", result);
        assertEquals(200, ecsClient.getResponseStatus());
    }
}