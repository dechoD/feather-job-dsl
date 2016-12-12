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
  String testFiles

  Job build(DslFactory factory) {
    Job baseJob = new UnitTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression
      ).build(factory)

      if (this.testFiles == null) {
        this.testFiles = 'Tests\\Telerik.Sitefinity.Frontend.TestUnit\\bin\\Release\\Telerik.Sitefinity.Frontend.TestUnit.dll'
      }

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetClientTestsGitSource(this.featherBranch)
        .MSBuildProject('.nuget\\NuGet.targets', 'CheckPrerequisites')
        .InstallFeatherPackages()
        .MSBuildProject('Feather.sln')
        .RunUnitTestsWithMSTest(this.testFiles)
        .RunWindowsExe('CodeCoverageConverter.exe', '-source:TestResults\\In\\FEATHER-CI\\data.coverage -dest:data.xml', 'true')
        .PublishEmmaCoverageReport('data.xml')
        .GetJob()
    }
  }