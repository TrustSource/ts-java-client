/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import java.util.Collections;
import java.util.List;

public class Scan {
    
    private final String project;
    private final String module;
    private final String moduleId;
    private final String branch;
    private final String tag;
    private final List<Dependency> dependencies;

    public Scan(String project, String module, String moduleId, String branch, String tag, List<Dependency> dependencies) {
        this.project = project;
        this.module = module;
        this.moduleId = moduleId;
        this.branch = branch;
        this.tag = tag;
        this.dependencies = dependencies;
    }

    public Scan(String project, String module, String moduleId, String branch, String tag, Dependency dependency) {
        this(project, module, moduleId, branch, tag, Collections.singletonList(dependency));
    }


    public List<Dependency> getDependencies() {
        return dependencies;
    }


    public String getProject() {
        return project;
    }

    public String getModule() {
        return module;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getBranch() {
        return branch;
    }

    public String getTag() {
        return tag;
    }
}
