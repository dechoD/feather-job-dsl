package jobs.builders.jobs

import feather.ci.builders.*
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*
import utilities.*

class ClientTestJob {
  String name
  String description
  String emails

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .RestrictWhereThisProjectCanBeRun("ClientTests")
        .SetGitSourceCodeManagement("*/CodeBaseIntegration")
        .TriggerBuildOnGitPush()
        .InjectEnvironmentalVariable('Path', '$Path;C:\\Users\\jenkinsci\\AppData\\Roaming\\npm')
        .AddNodeJsFolderToPath('nodejs')
        .RunClientTests()
        .GetJob()
    }
  }