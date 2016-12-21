package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.ClientTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class ClientTestJob {
  String name
  String description
  String emails
  String featherBranch
  String cronExpression
  Boolean mandatory

  Job build(DslFactory factory) {
    Job baseJob = new ClientTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression,
      mandatory: this.mandatory
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetClientTestsGitSource(this.featherBranch)
        .RunClientTests()
        .PublishCoberturaCoverageReport('Tests/Telerik.Sitefinity.Frontend.ClientTest/coverage/cobertura/cobertura-coverage.xml')
        .PublishJunitTestReport('Tests/Telerik.Sitefinity.Frontend.ClientTest/TestResults/*.xml')
        .GetJob()
    }
  }