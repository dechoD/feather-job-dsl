package feather.ci.builders.integration

import feather.ci.builders.*
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class IntegrationJobBuilder {
  String name
  String description
  String emails
  String featherBranch

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      baseJob.with {
        label('IntegrationTests')
        multiscm {
          git {
            remote {
              github('Sitefinity/feather')
              credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
            }
            branch(this.featherBranch)
            extensions {
              relativeTargetDirectory('Feather')
            }
          }
          git {
            remote {
              github('Sitefinity/feather-packages')
              credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
            }
            branch(this.featherBranch)
            extensions {
              relativeTargetDirectory('FeatherPackages')
            }
          }
          git {
            remote {
              github('Sitefinity/Tooling')
              credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
            }
            branch('*/master')
            extensions {
              relativeTargetDirectory('Tooling')
            }
          }
          git {
            remote {
              github('Sitefinity/sitefinity-mvc')
              credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
            }
            branch(this.featherBranch)
            extensions {
              relativeTargetDirectory('sitefinity-mvc')
            }
          }
          git {
            remote {
              github('Sitefinity/feather-widgets')
              credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
            }
            branch(this.featherBranch)
            extensions {
              relativeTargetDirectory('FeatherWidgets')
            }
          }
        }
      }

      return baseJob
    }
  }