package jobs.helpers

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class UiTestBase {
  String name
  String description
  String branch
  String sitefinityPackage
  String category
  Boolean sslEnabled
  Boolean enableMultisite
  Boolean readOnlyMode
  Boolean rerunFailedUITests
  String command
  String emails
  String cronExpression
  Boolean mandatory

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression,
      mandatory: this.mandatory
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetUiTestParameters(this.branch, this.sitefinityPackage, this.category, this.sslEnabled, this.enableMultisite, this.readOnlyMode, this.rerunFailedUITests)
        .ExecuteConcurentBuilds()
        .RestrictWhereThisProjectCanBeRun('UITests')
        .DeleteWorkspaceBeforeBuildStarts()
        .RunUiTests(this.command)
        .PublishMSTestReport('TestResults\\*.trx')
        .DeleteWorkspaceWhenBuildIsDone()
        .GetJob()
    }
  }