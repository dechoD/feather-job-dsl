package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class UnitTestJob {
  String name
  String description
  String emails
  String featherBranch
  String cronExpression

  String testFiles = 'Tests\\Telerik.Sitefinity.Frontend.TestUnit\\bin\\Release\\Telerik.Sitefinity.Frontend.TestUnit.dll'

  Job build(DslFactory factory) {
    Job baseJob = new UnitTestBase(
      name: this.name,
      description: this.description,
      branchParameter: this.featherBranch,
      emails: this.emails,
      cronExpression: this.cronExpression
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetUnitTestsGitSource("Sitefinity/feather", "feather")
        .SetGitHubProject("https://github.com/Sitefinity/feather/")
        .RunUnitTests(".\\Tooling\\Feather\\UnitTests\\Feather.ps1")
        .PublishEmmaCoverageReport('data.xml')
        .GetJob()
    }
  }