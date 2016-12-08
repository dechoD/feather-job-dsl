package jobs.helpers

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class ClientTestBase {
  String name
  String description
  String emails
  String cronExpression

  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails,
      cronExpression: this.cronExpression
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .RestrictWhereThisProjectCanBeRun("ClientTests")
        .TriggerBuildOnGitPush()
        .InjectEnvironmentalVariable('Path', '$Path;C:\\Users\\jenkinsci\\AppData\\Roaming\\npm')
        .AddNodeJsFolderToPath('nodejs')
        .GetJob()
    }
  }