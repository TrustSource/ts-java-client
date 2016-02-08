/*
 * Copyright (c) 2016. Enterprise Architecture Group, EACG
 *
 * SPDX-License-Identifier:	MIT
 *
 */

package de.eacg.ecs.client;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Dependency {
    private final String name;
    private final String description;
    private final String homepageUrl;
    private final String repoUrl;
    private final String key;
    private final Set<String> versions;
    private final boolean priv;
    private final Set<License> licenses;
    private final Set<Dependency> dependencies;
    private final String checksum;

    private Dependency(String name, String description, String key, Set<String> versions, String homepageUrl,
                       String repoUrl, boolean priv, Set<License> licenses, Set<Dependency>dependencies,
                       String checksum) {

        if(name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if(key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        this.name = name;
        this.description = description;
        this.homepageUrl = homepageUrl;
        this.repoUrl = repoUrl;
        this.key = key;
        this.priv = priv;
        this.checksum = checksum;
        this.versions = versions = versions == null ? new HashSet<String>() : versions;
        this.licenses = licenses = licenses == null ? new HashSet<License>() : licenses;
        this.dependencies = dependencies == null ? new HashSet<Dependency>() : dependencies;
    }

    public String getName() {
        return name;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }
    public String getRepoUrl() {
        return repoUrl;
    }

    public String getKey() {
        return key;
    }


    public String getDescription() {
        return description;
    }

    public boolean getPrivate() {
        return priv;
    }

    public String getChecksum() {
        return checksum;
    }

    public Set<Dependency> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    public Set<String> getVersions() {
        return Collections.unmodifiableSet(versions);
    }

    public Set<License> getLicenses() {
        return Collections.unmodifiableSet(licenses);
    }

    public void addLicense(License l) {
        licenses.add(l);
    }


    public static class Builder {
        private String name;
        private String description;
        private String homepageUrl;
        private String repoUrl;
        private String key;
        private boolean priv = false;
        private String checksum;
        private final Set<String> versions = new HashSet<>();
        private final Set<License> licenses = new HashSet<>();
        private final Set<Dependency> dependencies = new HashSet<>();


        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setHomepageUrl(String url) {
            this.homepageUrl = url;
            return this;
        }

        public Builder setRepoUrl(String url) {
            this.repoUrl = url;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder addVersions(Collection<String> versions) {
            this.versions.addAll(versions);
            return this;
        }

        public Builder addVersion(String version) {
            versions.add(version);
            return this;
        }

        public Builder setPrivate(boolean priv) {
            this.priv = priv;
            return this;
        }

        public Builder setChecksum(String checksum) {
            this.checksum = checksum;
            return this;
        }

        public Builder addLicenses(Collection<License> licenses) {
            this.licenses.addAll(licenses);
            return this;
        }

        public Builder addLicense(String name) {
            licenses.add(new License(name));
            return this;
        }

        public Builder addLicense(String name, String url) {
            licenses.add(new License(name, url));
            return this;
        }

        public Builder addDependency(Dependency dependency) {
            dependencies.add(dependency);
            return this;
        }

        public Builder addDependencies(Collection<Dependency> dependencies) {
            dependencies.addAll(dependencies);
            return this;
        }

        public Dependency buildDependency() {
            return new Dependency(name, description, key, versions, homepageUrl, repoUrl, priv, licenses, dependencies, checksum);
        }
    }
}
