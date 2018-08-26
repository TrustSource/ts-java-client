/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Check {
    private final String projectName;
    private final String moduleName;
    private final List<Component> components;

    public Check(String projectName, String moduleName, List<Component> components) {
        this.projectName = projectName;
        this.moduleName = moduleName;
        this.components = components;
    }

    public Check(String projectName, String moduleName, Component component) {
        this(projectName, moduleName, Collections.singletonList(component));
    }

    public String getProjectName() {
        return projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<Component> getComponents() {
        return components;
    }


    public static Check from(Scan scan) {
        List<Component> components = new ArrayList<Component>();

        for(Dependency dependency: scan.getDependencies()){
            components.addAll(Component.from(dependency));
        }

        return new Check(scan.getProject(), scan.getModule(), components);
    }
}
