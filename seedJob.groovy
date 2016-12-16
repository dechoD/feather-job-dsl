import jobs.builders.*
import utilities.JsonReader

def jobs = JsonReader.getJsonConfig("jobs.json")

jobs.clientJobs.each {
  new ClientTestJob(
    name: it.name,
    description: it.description,
    featherBranch: it.featherBranch,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.clientWidgetJobs.each {
  new ClientWidgetsTestJob(
    name: it.name,
    description: it.description,
    featherBranch: it.featherBranch,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.unitJobs.each {
  new UnitTestJob(
    name: it.name,
    description: it.description,
    featherBranch: it.featherBranch,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.widgetUnitJobs.each {
  new WidgetsUnitTestJob(
    name: it.name,
    description: it.description,
    featherBranch: it.featherBranch,
    emails: it.emails,
    downstreamProject: it.downstreamProject,
    cronExpression: it.cronExpression).build(this)
}

jobs.mvcUnitJobs.each {
  new MvcUnitTestJob(
    name: it.name,
    description: it.description,
    featherBranch: it.featherBranch,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.uiJobs.each {
  new UiTestJob(
    name: it.name,
    description: it.description,
    branch: it.branch,
    sitefinityPackage: it.sitefinityPackage,
    category: it.category,
    sslEnabled: it.sslEnabled,
    enableMultisite: it.enableMultisite,
    readOnlyMode: it.readOnlyMode,
    rerunFailedUITests: it.rerunFailedUITests,
    command: it.command,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.mvcUiJobs.each {
  new MvcUiTestJob(
    name: it.name,
    description: it.description,
    branch: it.branch,
    sitefinityPackage: it.sitefinityPackage,
    category: it.category,
    sslEnabled: it.sslEnabled,
    enableMultisite: it.enableMultisite,
    readOnlyMode: it.readOnlyMode,
    rerunFailedUITests: it.rerunFailedUITests,
    command: it.command,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
  }

jobs.integrationJobs.each {
  new IntegrationTestJob(
    name: it.name,
    description: it.description,
    branch: it.branch,
    sitefinityPackage: it.sitefinityPackage,
    testRunnerPackage: it.testRunnerPackage,
    category: it.category,
    sslEnabled: it.sslEnabled,
    readOnlyMode: it.readOnlyMode,
    gitProjectUrl: it.gitProjectUrl,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.widgetIntegrationJobs.each {
  new WidgetsIntegrationTestJob(
    name: it.name,
    description: it.description,
    branch: it.branch,
    sitefinityPackage: it.sitefinityPackage,
    testRunnerPackage: it.testRunnerPackage,
    category: it.category,
    sslEnabled: it.sslEnabled,
    readOnlyMode: it.readOnlyMode,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

jobs.mvcIntegrationJobs.each {
  new MvcIntegrationTestJob(
    name: it.name,
    description: it.description,
    branch: it.branch,
    sitefinityPackage: it.sitefinityPackage,
    testRunnerPackage: it.testRunnerPackage,
    category: it.category,
    sslEnabled: it.sslEnabled,
    readOnlyMode: it.readOnlyMode,
    gitProjectUrl: it.gitProjectUrl,
    buildAfterProject: it.buildAfterProject,
    buildWhenChangeIsPushed: it.buildWhenChangeIsPushed,
    emails: it.emails,
    cronExpression: it.cronExpression).build(this)
}

new CleanUiMachines().build(this)
new StartSlaves().build(this)
new StopSlaves().build(this)