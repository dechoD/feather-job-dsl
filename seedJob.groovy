import jobs.builders.*

new ClientTestJob(
  name: "Codebase_FeatherClientTest",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)

new ClientTestJob(
  name: "Master_FeatherClientTest",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  featherBranch: "*/master",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)

new ClientTestJob(
  name: "DBP_Codebase_FeatherClientTest",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  featherBranch: "*/DBP",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)

new ClientWidgetsTestJob(
  name: "Codebase_FeatherWidgetsClientTest",
  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
  featherBranch: "*/CodeBaseIntegration",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)

new ClientWidgetsTestJob(
  name: "Master_FeatherWidgetsClientTest",
  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
  featherBranch: "*/master",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)

new ClientWidgetsTestJob(
  name: "DBP_Codebase_FeatherWidgetsClientTest",
  description: "Runs client side tests for FeatherWidgets projects and checks code coverage and jshint",
  featherBranch: "*/DBP",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)


