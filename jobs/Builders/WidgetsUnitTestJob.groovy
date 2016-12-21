package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class WidgetsUnitTestJob {
  String name
  String description
  String emails
  String featherBranch
  String cronExpression
  String downstreamProject
  Boolean mandatory

  Job build(DslFactory factory) {
    Job baseJob = new UnitTestBase(
      name: this.name,
      description: this.description,
      branchParameter: this.featherBranch,
      emails: this.emails,
      cronExpression: this.cronExpression,
      mandatory: this.mandatory
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      if (this.downstreamProject != null) {
        jobBuilder.BuildOtherProject(this.downstreamProject, 'SUCCESS')
      }

      jobBuilder
        .SetUnitTestsGitSource("Sitefinity/feather-widgets", "FeatherWidgets")
        .SetGitHubProject("https://github.com/Sitefinity/feather-widgets/")
        .RunUnitTests(".\\Tooling\\Feather\\UnitTests\\FeatherWidgets.ps1")
        .PublishEmmaCoverageReport('data.xml')
        .GetJob()
    }
  }