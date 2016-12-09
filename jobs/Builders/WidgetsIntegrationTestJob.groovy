package jobs.builders

import jobs.helpers.IntegrationTestBase
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class WidgetsIntegrationTestJob {
  String name
  String description
  String branch
  String sitefinityPackage
  String testRunnerPackage
  String category
  Boolean sslEnabled
  Boolean readOnlyMode
  String command
  String emails

  Job build(DslFactory factory) {
    Job baseJob = new IntegrationTestBase(
      name: this.name,
      description: this.description,
      branch: this.branch,
      emails: this.emails,
      sitefinityPackage: this.sitefinityPackage,
      testRunnerPackage: this.testRunnerPackage,
      category: this.category,
      sslEnabled: this.sslEnabled,
      readOnlyMode: this.readOnlyMode
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .GetJob()
    }
  }