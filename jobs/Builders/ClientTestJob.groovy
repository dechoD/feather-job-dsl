package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class ClientTestJob {
  String name
  String description
  String emails
  String featherBranch

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .RestrictWhereThisProjectCanBeRun("ClientTests")
        .SetClientTestsGitSource(this.featherBranch)
        .TriggerBuildOnGitPush()
        .InjectEnvironmentalVariable('Path', '$Path;C:\\Users\\jenkinsci\\AppData\\Roaming\\npm')
        .AddNodeJsFolderToPath('nodejs')
        .RunClientTests()
        .PublishCoberturaCoverageReport('Tests/Telerik.Sitefinity.Frontend.ClientTest/coverage/cobertura/cobertura-coverage.xml')
        .PublishJunitTestReport('Tests/Telerik.Sitefinity.Frontend.ClientTest/TestResults/*.xml')
        .GetJob()
    }
  }