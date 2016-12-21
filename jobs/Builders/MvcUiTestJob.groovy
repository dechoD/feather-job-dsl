package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UiTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class MvcUiTestJob {
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
    Job baseJob = new UiTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression,
      branch: this.branch,
      sitefinityPackage: this.sitefinityPackage,
      category: this.category,
      sslEnabled: this.sslEnabled,
      enableMultisite: this.enableMultisite,
      readOnlyMode: this.readOnlyMode,
      rerunFailedUITests: this.rerunFailedUITests,
      command: this.command,
      mandatory: this.mandatory
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetMvcTestsGitSources('$Branch')
        .GetJob()
    }
  }