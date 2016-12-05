package feather.ci.builders.integration

import feather.ci.builders.*
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

import utilities.Steps
import utilities.JobBuilder

class IntegrationMasterJobBuilder {
  String name
  String description
  String emails
  String featherBranch
  String sitefinityBlob

  Job build(DslFactory factory) {
    Job baseJob = new IntegrationJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails,
      featherBranch: this.featherBranch
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .DownloadSitefinityAndTestRunner("\"SitefinityWebApp_9.2_6200.0_Feather.zip\"")
        .MSBuildProject(".\\Feather\\Feather.sln")
        .MSBuildProject(".\\FeatherWidgets\\FeatherWidgets.sln")
        .RestoreSitefinityNugetPackages()
        .CopyFeatherDllAndPackages()
        .MSBuildProject("C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln")
        .SetupSitefinityWithFeather()
        .RecreateFolder("TestResults")
        .EnsureSitefinityIsRunning()
        .RunIntegrationTests()
        .PublishMSTestReport("TestResults\\*.trx")
        .GetJob()

      //Steps.DownloadSitefinityAndTestRunner(baseJob, "\"SitefinityWebApp_9.2_6200.0_Feather.zip\"")
      //Steps.MSBuildProject(baseJob, ".\\Feather\\Feather.sln")
      //Steps.MSBuildProject(baseJob, ".\\FeatherWidgets\\FeatherWidgets.sln")
      //Steps.RestoreSitefinityNugetPackages(baseJob)
      //Steps.CopyFeatherDllAndPackages(baseJob)
      //Steps.MSBuildProject(baseJob, "C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln")
      //Steps.SetupSitefinityWithFeather(baseJob)
      //Steps.RecreateFolder(baseJob, "TestResults")
      //Steps.EnsureSitefinityIsRunning(baseJob)
      //Steps.RunIntegrationTests(baseJob)
      //Steps.PublishMSTestReport(baseJob, "TestResults\\*.trx")
    }
  }