/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DependencyBuilderTest {

    @Test
    public void build_returns_valid_Dependency() {
        Dependency.Builder b = new Dependency.Builder();

        b.setName("test");
        b.setDescription("a test");
        b.setKey("a:b:c");
        b.setRepoUrl("http://repo.com");
        b.addLicense("MIT", "http://test.com");
        b.addVersion("1.0.0");

        Dependency d = b.buildDependency();
        assertEquals("test", d.getName());
        assertEquals("a test", d.getDescription());
        assertEquals("a:b:c", d.getKey());
        assertEquals("http://repo.com", d.getRepoUrl());
        assertEquals("MIT", d.getLicenses().iterator().next().getName());
        assertEquals("http://test.com", d.getLicenses().iterator().next().getUrl());
        assertEquals("1.0.0", d.getVersions().iterator().next());

        assertEquals(0, d.getDependencies().size());
    }

    @Test
    public void build_with_child_dependency_returns_valid_Dependency() {
        Dependency.Builder childBuilder = new Dependency.Builder();
        childBuilder.setName("child");
        childBuilder.setKey("c:1");

        Dependency.Builder parentBuilder = new Dependency.Builder();

        parentBuilder.setName("parent");
        parentBuilder.setKey("p:1");
        parentBuilder.addDependency(childBuilder.buildDependency());

        Dependency d = parentBuilder.buildDependency();

        assertEquals("parent", d.getName());
        assertEquals("child", d.getDependencies().iterator().next().getName());

    }

    @Test
    public void build_without_child_dependency_returns_valid_Dependency() {
        Dependency.Builder parentBuilder = new Dependency.Builder();

        parentBuilder.setName("parent");
        parentBuilder.setKey("p:1");
        Dependency d = parentBuilder.buildDependency();

        Dependency.Builder childDependency = new Dependency.Builder();
        childDependency.setName("child");
        childDependency.setKey("c:1");


        d.addDependency(childDependency.buildDependency());
    }
}