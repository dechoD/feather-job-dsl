import feather.ci.builders.integration.*
import utilities.*
import feather.ci.jobs.*

new IntegrationCodeBaseJobBuilder(
  name: "Codebase_FeatherIntegrationTests",
  description: "Feather Codebase Integration Tests",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com",
  featherBranch: "*/CodeBaseIntegration",
  sitefinityBlob: "\"SitefinityWebApp_10.0.6400.0_Feather.zip\"").build(this)

new IntegrationCodeBaseJobBuilder(
  name: "DBP Codebase_FeatherIntegrationTests",
  description: "DBP Feather Codebase Integration Tests",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com",
  featherBranch: "*/DBP",
  sitefinityBlob: "\"SitefinityWebApp_9.3_6300.0_Feather.zip\"").build(this)

new IntegrationMasterJobBuilder(
  name: "InternalMaster_FeatherIntegrationTests",
  description: "Internal Master Feather Integration Tests",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com",
  featherBranch: "*/master").build(this)

new IntegrationMasterJobBuilder(
  name: "Master_FeatherIntegrationTests",
  description: "Master Integration Tests",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com",
  featherBranch: "*/master").build(this)

new ClientTestJob(
  name: "Codebase_FeatherClientTest",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)
