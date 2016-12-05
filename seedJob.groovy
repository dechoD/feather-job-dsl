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
