import jobs.builders.*

new ClientTestJob(
  name: "Codebase_FeatherClientTest_DSL",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com").build(this)

//new ClientTestJob(
//  name: "Master_FeatherClientTest",
//  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
//  featherBranch: "*/master",
//  emails: "decho.decchev@progress.com").build(this)
//
//new ClientTestJob(
//  name: "DBP_Codebase_FeatherClientTest",
//  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
//  featherBranch: "*/DBP",
//  emails: "decho.decchev@progress.com").build(this)
//
new ClientWidgetsTestJob(
  name: "Codebase_FeatherWidgetsClientTest_DSL",
  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com").build(this)

//new ClientWidgetsTestJob(
//  name: "Master_FeatherWidgetsClientTest",
//  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
//  featherBranch: "*/master",
//  emails: "decho.decchev@progress.com").build(this)
//
//new ClientWidgetsTestJob(
//  name: "DBP_Codebase_FeatherWidgetsClientTest",
//  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
//  featherBranch: "*/DBP",
//  emails: "decho.decchev@progress.com").build(this)
//
new FeatherUnitTestJob(
  name: "Codebase_FeatherUnitTests_DSL",
  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com",
  testFiles: "Tests\\Telerik.Sitefinity.Frontend.TestUnit\\bin\\Release\\Telerik.Sitefinity.Frontend.TestUnit.dll").build(this)

//new FeatherUnitTestJob(
//  name: "DBP_Codebase_FeatherUnitTests",
//  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
//  featherBranch: "*/DBP",
//  emails: "decho.decchev@progress.com").build(this)
//
//new FeatherUnitTestJob(
//  name: "Master_FeatherUnitTests",
//  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
//  featherBranch: "*/master",
//  emails: "decho.decchev@progress.com").build(this)
//
//new FeatherWidgetsUnitTestJob(
//  name: "Codebase_FeatherWidgetsUnitTests",
//  description: "The Frontend Widgets for the Sitefinity Feather project.",
//  featherBranch: "*/CodeBaseIntegration",
//  emails: "decho.decchev@progress.com").build(this)
//
//new FeatherWidgetsUnitTestJob(
//  name: "DBP_Codebase_FeatherWidgetsUnitTests",
//  description: "The Frontend Widgets for the Sitefinity Feather project.",
//  featherBranch: "*/DBP",
//  emails: "decho.decchev@progress.com").build(this)
//
//new FeatherWidgetsUnitTestJob(
//  name: "Master_FeatherWidgetsUnitTests",
//  description: "The Frontend Widgets for the Sitefinity Feather project.",
//  featherBranch: "*/master",
//  emails: "decho.decchev@progress.com").build(this)
//
new MvcUnitTestJob(
  name: "CodeBase_Telerik.Sitefinity.Mvc_UnitTests_DSL",
  description: "",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com").build(this)

//new MvcUnitTestJob(
//  name: "Master_Telerik.Sitefinity.Mvc_UnitTests",
//  description: "",
//  featherBranch: "*/master",
//  emails: "decho.decchev@progress.com").build(this)
//
//new UiTestJob(
//  name: "Tooling_FeatherWidgetsUITests_ContentBlock",
//  description: "The Frontend Widgets for the Sitefinity Feather project.",
//  branch: "master",
//  sitefinityPackage: "SitefinityWebApp_9.2_6280.0_Internal.zip",
//  category: "ContentBlock",
//  sslEnabled: false,
//  enableMultisite: true,
//  readOnlyMode: false,
//  rerunFailedUITests: true,
//  command: ".\\Tooling\\Feather\\UITests\\FeatherWidgets.ps1",
//  emails: "decho.decchev@progress.com").build(this)