[![Build Status](https://travis-ci.org/eacg-gmbh/ecs-java-client.svg?branch=master)](https://travis-ci.org/eacg-gmbh/ecs-java-client)
[![Maven](https://img.shields.io/maven-central/v/de.eacg/ecs-java-client.svg)](http://search.maven.org/#search|gav|1|g%3A%22de.eacg%22%20AND%20a%3A%22ecs-java-client%22)
[![MIT License](https://img.shields.io/npm/l/check-dependencies.svg?style=flat-square)](http://opensource.org/licenses/MIT)

# TrustSource java-client
TrustSource (https://www.trustsource.io) is a legal resolver and OpenChain compliant workflow engine that allows you to manage your open source dependencies, provide legal compliance and create bill of materials.

TrustSource Java client is a java library to transfer dependency information to TrustSource-Server via its REST-API. There are several plugins available that use (wrap) this library to integrate with different build tools:

Please see the following links for more details on the corresponding package manager: 
* [Ivy/Ant (Java)](https://github.com/eacg-gmbh/TrustSource-Ant-Plugin)
* [Maven (Java)](https://github.com/eacg-gmbh/ecs-mvn-plugin)
* [Gradle (Java)](https://github.com/eacg-gmbh/ecs-gradle-plugin)
* [Kobalt (Java)](https://github.com/eacg-gmbh/TrustSource-Kobalt-Plugin)
* [Node (JScript)](https://github.com/eacg-gmbh/ecs-node-client)
* [Grunt (JScript)](https://github.com/eacg-gmbh/ecs-grunt-plugin) could also be used for gulp ([see here](https://support.trustsource.io/hc/en-us/articles/115003209085-How-to-integrate-TrustSource-with-npm-via-gulp))
* [PIP (Python)](https://github.com/eacg-gmbh/ecs-pip-plugin)
* [Bundler (Ruby)](https://github.com/eacg-gmbh/ecs-bundler)
* [Composer (PHP)](https://github.com/eacg-gmbh/ecs-composer)
* [SPM (Swift)](https://github.com/eacg-gmbh/ecs-spm-plugin)
* [nuget (.NET)](https://github.com/eacg-gmbh/ts-dotnet)

However, since it became difficult to pre-set CI/CD-pipelines with all the different tools, we decided to unify the different tools under the umbrella of new [ts-scan](https://github.com/trustsource/ts-scan) project. Stepwise we will migrate the different environments there and stop continuing the development of the different tools.

# TrustSource and Continuous Integration
There is a Jenkins Plugin, which can trigger most of the scanners listed above. You will find further information on this plugin at [Jenkins plugin](https://github.com/jenkinsci/ts-publisher-plugin) or directly on the [Jenkins Plugin Marketplace](https://plugins.jenkins.io/ecs-publisher). 
PLEASE NOTE: Development for the Jenkins-Plugin has been discontinued. We recognized the delta in demands from different organisations are too big. That is why we decided to focus on a suitable API and provisioning of an SDK. Shortly we will provide our API v2, which comes with predefined SDKs.

# How to obtain a TrustSource API Key
TrustSource provide a free version. You may tregister and select the egar wheel on the upper right side and select API keys from the menu. Then select API-Key and generate the key. Paste user & API key into your local settings file and run your scan. Be compliant ;-)

# How to obtain Support
Write us an email to support@trustsurce.io. We will be happy to hear from you. Or visit our knowledgebase at https://support.trustsource.io for more insights and tutorials.

