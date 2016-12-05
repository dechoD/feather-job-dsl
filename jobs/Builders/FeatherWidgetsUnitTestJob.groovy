package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.UnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class FeatherWidgetsUnitTestJob {
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
        .SetClientTestsGitSource('*/CodeBaseIntegration', 'Sitefinity/feather-widgets')
        .MSBuildProject('FeatherWidgets.sln')
        .RunUnitTestsWithMSTest('Tests\\FeatherWidgets.TestUnit\\bin\\Release\\FeatherWidgets.TestUnit.dll')
        .RunWindowsExe('CodeCoverageConverter.exe', '-source:TestResults\\In\\FEATHER-CI\\data.coverage -dest:data.xml', 'true')
        .PublishEmmaCoverageReport('data.xml')        
        .BuildOtherProject('Codebase_FeatherWidgetsClientTest', 'SUCCESS')
        .GetJob()
    }
  }