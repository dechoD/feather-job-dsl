# Feather Jenkins CI Job DSL groovy scripts
This project is used by https://feather-ci.sitefinity.com/ SeedJob to create and recreate all other jobs except team specific job definitions.

## Usage

1. Jobs are defined in jobs.json. There is an array for each type of jenkins job:
 + clientJobs
 + clientWidgetJobs
 + unitJobs
 + widgetUnitJobs
 + mvcUnitJobs
 + integrationJobs
 + widgetIntegrationJobs
 + mvcIntegrationJobs
 + mvcUiJobs
 + uiJobs

1. Add your job using the existing as reference

1. Supported parameters are as follows:
  + All jobs support :
    + String name
    + String description (defaults to empty string)
    + String emails (no email notification set if missing)
    + String cronExpression (no schedule run is set if missing)
    + Boolean mandatory (defaults to false)
  + Integration test jobs support their parameters:
    + String branch
    + String sitefinityPackage
    + String testRunnerPackage
    + String category
    + Boolean sslEnabled
    + Boolean readOnlyMode
  + Unit tests:
    + String branchParameter
  + UI tests:
    + String sitefinityPackage
    + String category
    + Boolean sslEnabled
    + Boolean enableMultisite
    + Boolean readOnlyMode
    + Boolean rerunFailedUITests
    + String command
      + ".\\\\Tooling\\\\Feather\\\\UITests\\\\Feather.ps1" for feather tests
      + ".\\\\Tooling\\\\Feather\\\\UITests\\\\FeatherWidgets.ps1" for widget tests
  + Mvc UI Test jobs:
    + String sitefinityPackage
    + String category
    + Boolean sslEnabled
    + Boolean enableMultisite
    + Boolean readOnlyMode
    + Boolean rerunFailedUITests

### Local development helper

1. You need this only if you are going to write a script for a new type of job, to add a new job definition refer to [Usage](#Usage) section
1. Install and run a local Jenkins (If you need quick and safe script validation)
1. Clone this repo
1. Run `./gradlew build` from the project root directory
1. Start writing your jobs.

For local development, as you iterate on your job files, it's usually nice to tweak your file locally,
create those jobs in a local jenkins, and then push to GitHub when they're working as expected.

To do so, use the gradle `rest` task, which we've included in `jenkins-automation` and lifted nearly verbatim from https://github.com/sheehan/job-dsl-gradle-example (thanks!)


`./gradlew rest -Dpattern=<pattern> -DbaseUrl=<baseUrl> [-Dusername=<username>] [-Dpassword=<password>]`

* `pattern` - [ant-style path pattern](https://ant.apache.org/manual/dirtasks.html) of files to include
* `baseUrl` - base URL of Jenkins server
* `username` - Jenkins username, if secured
* `password` - Jenkins password or token, if secured
* `JAC_ENVIRONMENT` - An environment variable we set in all of our Jenkinses. Defaults to "dev". Used by our `EnvironmentUtils`. See example usage in `jobs/jobs.groovy` in this repo
* `JAC_HOST` - An environment variable we set in all of our Jenkinses. Defaults to "aws".

This uses a task named `rest` to execute `jenkins.automation.rest.RestApiScriptRunner`,
which lives in the `jenkins-automation` repo and is available here since `jenkins-automation` is a dependency.

Finally, install and run Jenkins locally:
* download the `.war` file from [https://jenkins.io/](https://jenkins.io/)
* run with `nohup java -jar path-to-file/jenkins.war --httpPort=8081`

#### Examples

Run all jobs files in the `jobs` directory against a locally running Jenkins

`./gradlew rest -Dpattern=jobs/** -DbaseUrl=http://localhost:8081`

Run all jobs defined in`jobs/jobs.groovy` against a locally running Jenkins.

`./gradlew rest -Dpattern=jobs/jobs.groovy -DbaseUrl=http://localhost:8081`

Same as above, but override the default JAC_ENVIRONMENT variable if those jobs behave different based on that variable
`./gradlew rest -Dpattern=jobs/jobs.groovy -DbaseUrl=http://localhost:8081 -DJAC_ENVIRONMENT=prod`


Run everything in the `jobs` directory against a remote Jenkins

`./gradlew rest -Dpattern=jobs/** -DbaseUrl=http://dev.jenkins -Dusername=foo -Dpassword=bar`
