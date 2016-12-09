package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class UiTestJob {
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

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetUiTestParameters(this.branch, this.sitefinityPackage, this.category, this.sslEnabled, this.enableMultisite, this.readOnlyMode, this.rerunFailedUITests)
        .ExecuteConcurentBuilds()
        .RestrictWhereThisProjectCanBeRun('UITests')
        .SetUiTestsGitSources('$Branch')
        .DeleteWorkspaceBeforeBuildStarts()
        .RunUiTests(this.command)
        .PublishMSTestReport('TestResults\\*.trx')
        .DeleteWorkspaceWhenBuildIsDone()
        .GetJob()
    }
  }