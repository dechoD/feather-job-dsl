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

  Job build(DslFactory factory) {
    Job baseJob = new UnitTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetClientTestsGitSource(featherBranch, 'Sitefinity/sitefinity-mvc')
        .DeleteWorkspaceBeforeBuildStarts()
        .MSBuildProject('.\\Telerik.Sitefinity.Mvc.sln')
        .RunMvcUnitTests()
        .PushMvcNuget()
        .GetJob()
    }
  }