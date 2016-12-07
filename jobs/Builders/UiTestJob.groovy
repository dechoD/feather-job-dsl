package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UiTestBase
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

  Job build(DslFactory factory) {
    Job baseJob = new UiTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetUiTestParameters(this.branch, this.sitefinityPackage, this.category, this.sslEnabled, this.enableMultisite, this.readOnlyMode, this.rerunFailedUITests)
        .ExecuteConcurentBuilds()
        .RestrictWhereThisProjectCanBeRun('UITestsNew')
        .SetUiTestsGitSources('$Branch')
        .DeleteWorkspaceBeforeBuildStarts()
        .RunUiTests(this.command)
        .PublishMSTestReport('TestResults\\*.trx')
        .DeleteWorkspaceWhenBuildIsDone()
        .GetJob()
    }
  }