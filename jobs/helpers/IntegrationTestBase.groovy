package jobs.helpers

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class IntegrationTestBase {
  String name
  String description
  String branch
  String sitefinityPackage
  String testRunnerPackage
  String category
  Boolean sslEnabled
  Boolean readOnlyMode
  String emails

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)
      .SetIntegrationTestParameters(this.branch, this.sitefinityPackage, this.testRunnerPackage, this.category, this.sslEnabled, this.readOnlyMode)
      .ExecuteConcurentBuilds()
      .RestrictWhereThisProjectCanBeRun('IntegrationTests')
      .DeleteWorkspaceBeforeBuildStarts()
      .PublishMSTestReport('TestResults\\*.trx')
      .DeleteWorkspaceWhenBuildIsDone()
      .GetJob()
    }
  }