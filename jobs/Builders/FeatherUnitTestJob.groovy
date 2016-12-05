package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class FeatherUnitTestJob {
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
        .SetClientTestsGitSource('*/CodeBaseIntegration')
        .MSBuildProject('.nuget\\NuGet.targets', 'CheckPrerequisites')
        .InstallFeatherPackages()
        .MSBuildProject('Feather.sln')
        .PublishEmmaCoverageReport('data.xml')
        .GetJob()
    }
  }