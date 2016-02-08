/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonPropertiesTest {

    private JsonProperties properties;
    private JsonProperties emptyPropertiesWithDefaults;
    private JsonProperties propertiesWithDefaults;

    @Before
    public void before() throws Exception {
        Properties defaults = new Properties();
        defaults.setProperty("userName", "Otto");
        defaults.setProperty("apiKey", "12345");
        defaults.setProperty("baseUrl", "http://base");
        defaults.setProperty("apiPath", "/path");

        defaults.setProperty("extra.large", "extraLarge");

        properties = new JsonProperties(this.getClass().getResourceAsStream("/test.json"));
        emptyPropertiesWithDefaults = new JsonProperties((String)null, defaults);
        propertiesWithDefaults = new JsonProperties(this.getClass().getResourceAsStream("/test.json"), defaults);
    }

    @Test
    public void validate_without_errors() throws Exception {
        List<String> missingKeys = propertiesWithDefaults.validate();
        assertTrue("keys are missing:" + missingKeys, missingKeys.isEmpty());
    }

    @Test
    public void validate_with_errors() throws Exception {
        List<String> missingKeys = propertiesWithDefaults.validate("unsetKey");
        assertEquals(1, missingKeys.size());
        assertEquals("unsetKey", missingKeys.get(0));
    }


    @Test
    public void get_without_defaults() throws Exception {
        assertEquals("willy", properties.getProperty("userName"));
        assertEquals("12345678-12345678", properties.getProperty("apiKey"));
    }

    @Test
    public void get_groupedString_without_defaults() throws Exception {
        assertEquals("user", properties.getProperty("basicAuth.user"));
        assertEquals("passwd", properties.getProperty("basicAuth.passwd"));
    }

    @Test
    public void empty_propeties_return_defaults() throws Exception {
        assertEquals("Otto", emptyPropertiesWithDefaults.getProperty("userName"));
        assertEquals("12345", emptyPropertiesWithDefaults.getProperty("apiKey"));
        assertEquals("extraLarge", emptyPropertiesWithDefaults.getProperty("extra.large"));
    }
    @Test
    public void propeties_return_defaults_for_undefined() throws Exception {
        assertEquals("willy", propertiesWithDefaults.getProperty("userName"));
        assertEquals("extraLarge", propertiesWithDefaults.getProperty("extra.large"));
    }

    @Test
    public void get_deep_nessted_property() throws Exception {

        String json = "{" +
                            "\"lvl1\": {" +
                                "\"lvl2\": {" +
                                    "\"lvl3\": {" +
                                        "\"lvl4\": \"abc\","+
                                        "\"sibl\": \"s3\""+
                                    "}," +
                                    "\"sibl\": \"s2\"" +
                                "}," +
                                "\"sibl\": \"s1\"" +
                            "},"+
                            "\"sibl\": \"s0\"" +
                        "}";
        JsonProperties properties = new JsonProperties(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        assertEquals("abc", properties.getProperty("lvl1.lvl2.lvl3.lvl4"));
        assertEquals("s0", properties.getProperty("sibl"));
        assertEquals("s1", properties.getProperty("lvl1.sibl"));
        assertEquals("s2", properties.getProperty("lvl1.lvl2.sibl"));
        assertEquals("s3", properties.getProperty("lvl1.lvl2.lvl3.sibl"));

    }
}
