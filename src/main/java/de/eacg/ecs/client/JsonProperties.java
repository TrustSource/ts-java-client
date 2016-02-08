/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Simple <code>java.util.Properties</code> implementation, that is able to parse a properties file in json format.
 * Restrictions: supported types: String and (nested) Object ; other types in json file are ignored.
 * Nested objects key names are separated by '.'
 *
 * Example:
 * { "name": {"firstName": "Bart", "lastName": "Simpson"}}
 *
 * produces the properties:
 * name.firstName = "Bart"
 * name.lastName = "Simpson"
 *
 * Additional features:
 * - an additional 'defaults' properties list, my by provided in the constructors.
 * - defaults for the manadatory properties required by the RestClient may also be set by named setters (e.g. setUserName())
 * - a validate method checks if all required properties for RestClient are available
 *
 */
public class JsonProperties extends Properties {

    public static final String USER_NAME = "userName";
    public static final String API_KEY = "apiKey";
    public static final String BASE_URL = "baseUrl";
    public static final String API_PATH = "apiPath";

    private static final List<String> MANDATORY_KEYS = Arrays.asList(
        new String[] { USER_NAME, API_KEY, BASE_URL, API_PATH});

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonProperties(String filename) throws IOException {
        this(filename, new Properties());
    }

    public JsonProperties(String filename, Properties defaults) throws IOException {
        super(defaults);
        if (filename != null) {
            File f = new File(filename.replaceAll("^~", System.getProperty("user.home")));
            mapProperties(mapper.readValue(f, Map.class), "");
        }
    }
    public JsonProperties(InputStream is) throws IOException {
        this(is, new Properties());
    }

    public JsonProperties(InputStream is, Properties defaults) throws IOException {
        super(defaults);
        mapProperties(mapper.readValue(is, Map.class), "");
    }

    public void setUserName(String userName) {
        setPropertyIfNotNull(USER_NAME, userName);
    }

    public void setApiKey(String apiKey) {
        setPropertyIfNotNull(API_KEY, apiKey);
    }

    public void setBaseUrl(String baseUrl) {
        setPropertyIfNotNull(BASE_URL, baseUrl);
    }

    public void setApiPath(String apiPath) {
        setPropertyIfNotNull(API_PATH, apiPath);
    }

    /**
     * check if minimum required parameters are set:
     * "userName", "apiKey", "baseUrl", "apiPath"
     * @param keys additional keys to check
     * @return a list of missing keys
     */
    public List<String> validate(String... keys) {
        List<String> required = new ArrayList<>();
        required.addAll(MANDATORY_KEYS);
        required.addAll(Arrays.asList(keys));
        List<String> errors = new ArrayList<>();

        for(String key : required) {
            String value = getProperty(key);
            if(value == null || value.isEmpty()) {
                errors.add(key);
            }
        }
        return errors;
    }

    private void setPropertyIfNotNull(String key, String value) {
        if(key != null && value != null) {
            this.defaults.setProperty(key, value);
        }
    }

    private void mapProperties(Map<String, Object>props, String prefix) {
        for(Map.Entry<String, Object> e : props.entrySet()) {
            if(String.class.isInstance(e.getValue())) {
                setProperty(prefix + e.getKey(), (String)e.getValue());
            } else if(Map.class.isInstance(e.getValue())) {
                mapProperties((Map<String, Object>)e.getValue(),
                        prefix + e.getKey() + '.');
            }
        }
    }
}
