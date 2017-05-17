project {
  modelVersion '4.0.0'
  groupId 'flink-starter'
  artifactId 'flink-starter'
  version '1.0-SNAPSHOT'
  name 'Sample Flink Project with Java, Scala and polygot-groovy Maven'

  properties {
    'scala.macros.version' '2.0.1'
    'slf4j.version' '1.7.7'
    'scala.version' '2.11.7'
    'scala.binary.version' '2.11'
    'log4j.version' '1.2.17'
    'flink.version' '1.2.0'
    'project.build.sourceEncoding' 'UTF-8'
  }

  dependencies {
    dependency 'org.apache.flink:flink-java:${flink.version}'
    dependency 'org.apache.flink:flink-streaming-java_${scala.binary.version}:${flink.version}'
    dependency 'org.apache.flink:flink-clients_${scala.binary.version}:${flink.version}'

    dependency 'org.slf4j:slf4j-log4j12:${slf4j.version}'
    dependency 'log4j:log4j:${log4j.version}'

    dependency 'org.apache.flink:flink-connector-kafka-0.9_${scala.binary.version}:${flink.version}'
    dependency 'org.apache.flink:flink-connector-filesystem_${scala.binary.version}:${flink.version}'

    dependency 'com.amazonaws:aws-java-sdk:1.7.4'
    dependency 'org.apache.hadoop:hadoop-aws:2.7.2'

    dependency 'org.hamcrest:hamcrest-all:1.3:test'
    dependency 'org.apache.flink:flink-test-utils_${scala.binary.version}:${flink.version}:test'

  }

  repositories {
    repository {
      releases { enabled 'false' }
      snapshots { enabled 'true' }
      id 'apache.snapshots'
      name 'Apache Development Snapshot Repository'
      url 'https://repository.apache.org/content/repositories/snapshots/'
    }
  }

  build {
    plugins {
      plugin {
        artifactId 'maven-shade-plugin'
        version '2.4.1'
        executions {
          execution {
            phase 'package'
            goals  'shade'
            configuration {
              artifactSet {
                excludes {
                  exclude 'org.apache.flink:flink-annotations'
                  exclude 'org.apache.flink:flink-shaded-hadoop2'
                  exclude 'org.apache.flink:flink-shaded-curator-recipes'
                  exclude 'org.apache.flink:flink-core'
                  exclude 'org.apache.flink:flink-java'
                  exclude 'org.apache.flink:flink-scala_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-runtime_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-optimizer_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-clients_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-avro_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-examples-batch_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-examples-streaming_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-streaming-java_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-streaming-scala_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-scala-shell_${scala.binary.version}'
                  exclude 'org.apache.flink:flink-python'
                  exclude 'org.apache.flink:flink-metrics-core'
                  exclude 'org.apache.flink:flink-metrics-jmx'
                  exclude 'org.apache.flink:flink-statebackend-rocksdb_${scala.binary.version}'
                  exclude 'log4j:log4j'
                  exclude 'org.scala-lang:scala-library'
                  exclude 'org.scala-lang:scala-compiler'
                  exclude 'org.scala-lang:scala-reflect'
                  exclude 'com.data-artisans:flakka-actor_*'
                  exclude 'com.data-artisans:flakka-remote_*'
                  exclude 'com.data-artisans:flakka-slf4j_*'
                  exclude 'io.netty:netty-all'
                  exclude 'io.netty:netty'
                  exclude 'commons-fileupload:commons-fileupload'
                  exclude 'org.apache.avro:avro'
                  exclude 'commons-collections:commons-collections'
                  exclude 'org.codehaus.jackson:jackson-core-asl'
                  exclude 'org.codehaus.jackson:jackson-mapper-asl'
                  exclude 'com.thoughtworks.paranamer:paranamer'
                  exclude 'org.xerial.snappy:snappy-java'
                  exclude 'org.apache.commons:commons-compress'
                  exclude 'org.tukaani:xz'
                  exclude 'com.esotericsoftware.kryo:kryo'
                  exclude 'com.esotericsoftware.minlog:minlog'
                  exclude 'org.objenesis:objenesis'
                  exclude 'com.twitter:chill_*'
                  exclude 'com.twitter:chill-java'
                  exclude 'commons-lang:commons-lang'
                  exclude 'junit:junit'
                  exclude 'org.apache.commons:commons-lang3'
                  exclude 'org.slf4j:slf4j-api'
                  exclude 'org.slf4j:slf4j-log4j12'
                  exclude 'log4j:log4j'
                  exclude 'org.apache.commons:commons-math'
                  exclude 'org.apache.sling:org.apache.sling.commons.json'
                  exclude 'commons-logging:commons-logging'
                  exclude 'commons-codec:commons-codec'
                  exclude 'com.fasterxml.jackson.core:jackson-core'
                  exclude 'com.fasterxml.jackson.core:jackson-databind'
                  exclude 'com.fasterxml.jackson.core:jackson-annotations'
                  exclude 'stax:stax-api'
                  exclude 'com.typesafe:config'
                  exclude 'org.uncommons.maths:uncommons-maths'
                  exclude 'com.github.scopt:scopt_*'
                  exclude 'commons-io:commons-io'
                  exclude 'commons-cli:commons-cli'
                }
              }
              filters {
                filter {
                  artifact 'org.apache.flink:*'
                  excludes {
                    exclude 'org/apache/flink/shaded/com/**'
                    exclude 'web-docs/**'
                  }
                }
                filter {
                  artifact '*:*'
                  excludes {
                    exclude 'META-INF/*.SF'
                    exclude 'META-INF/*.DSA'
                    exclude 'META-INF/*.RSA'
                  }
                }
              }
              createDependencyReducedPom 'false'
            }
          }
        }
      }
      plugin {
        artifactId 'maven-compiler-plugin'
        version '3.1'
        dependencies {
          dependency  'org.eclipse.tycho:tycho-compiler-jdt:0.21.0'
        }
        configuration {
          source '1.8'
          target '1.8'
          //compilerId: 'jdt'
        }
      }
      plugin {
        groupId 'net.alchim31.maven'
        artifactId 'scala-maven-plugin'
        executions {
          execution {
            id 'scala-compile-first'
            phase 'process-resources'
            goals 'compile'
          }
          execution {
            id 'scala-test-compile'
            phase 'process-test-resources'
            goals 'testCompile'
          }
        }
        configuration {
          jvmArgs {
            jvmArg '-Xms128m'
            jvmArg '-Xmx512m'
          }
          scalaCompatVersion '${scala.binary.version}'
          compilerPlugins('combine.children':'append') {
            compilerPlugin {
              groupId 'org.scalamacros'
              artifactId 'paradise_${scala.version}'
              version '${scala.macros.version}'
            }
          }
        }
      }
      plugin {
        groupId 'org.codehaus.mojo'
        artifactId 'build-helper-maven-plugin'
        version '1.7'
        executions {
          execution {
            id 'add-source'
            phase 'generate-sources'
            goals  'add-source'
            configuration {
              sources {
                source 'src/main/scala'
              }
            }
          }
          execution {
            id 'add-test-source'
            phase 'generate-test-sources'
            goals 'add-test-source'
            configuration {
              sources {
                source 'src/test/scala'
              }
            }
          }
        }
      }
    }
  }
  profiles {
    profile {
      id 'build-jar'
      activation {}
      build {
        plugins {
          plugin {
            artifactId 'maven-shade-plugin'
            version '2.4.1'
            executions {
              execution {
                phase 'package'
                goals 'shade'
                configuration {
                  artifactSet {
                    excludes('combine.self':'override') {}
                  }
                }
              }
            }
          }
        }
      }

      dependencies {
        dependency 'org.apache.flink:flink-java:${flink.version}:provided'
        dependency 'org.apache.flink:flink-streaming-java_${scala.binary.version}:${flink.version}:provided'
        dependency 'org.apache.flink:flink-clients_${scala.binary.version}:${flink.version}:provided'

        dependency 'org.slf4j:slf4j-log4j12:${slf4j.version}:provided'
        dependency 'log4j:log4j:${log4j.version}:provided'
      }
    }
  }
}
