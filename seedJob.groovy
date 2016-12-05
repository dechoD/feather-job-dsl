import jobs.builders.jobs.*

new ClientTestJob(
  name: "Codebase_FeatherClientTest",
  description: "Runs client side tests for Feather projects and checks code coverage and jshint",
  emails: "decho.decchev@progress.com tihomir.petrov@progress.com").build(this)
