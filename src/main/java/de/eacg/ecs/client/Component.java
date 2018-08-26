/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Component {
    private final String key;
    private final String name;
    private final String version;

    public Component(String key, String version) {
        this.key = key;
        this.name = "";
        this.version = version;
    }

    @JsonCreator
    public Component(@JsonProperty("key")String key,
                     @JsonProperty("name")String name,
                     @JsonProperty("version")String version) {
        this.key = key;
        this.name = name;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public static List<Component> from(Dependency dependency) {
        List<Component> components = new ArrayList<Component>();

        String key = dependency.getKey();
        Set<String> versions = dependency.getVersions();

        for (String version: versions) {
            components.add(new Component(key, version));
        }

        for(Dependency dep: dependency.getDependencies()){
            components.addAll(Component.from(dep));
        }

        return components;
    }
}
