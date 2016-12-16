package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class MvcUnitTestJob {
  String name
  String description
  String emails
  String featherBranch
  String cronExpression

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
        .SetUnitTestsGitSource("Sitefinity/sitefinity-mvc", "sitefinity-mvc")
        .SetGitHubProject("https://github.com/Sitefinity/sitefinity-mvc/")
        .RunUnitTests(".\\Tooling\\Feather\\UnitTests\\SitefinityMvc.ps1")
        .GetJob()
    }
  }