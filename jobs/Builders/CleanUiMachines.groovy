package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class CleanUiMachines {
  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: "SystemJob-CleanUIMachines_DSL",
      description: "Job that cleans Test Agent temp files on UI machines.",
      emails: "tihomir.petrov@telerik.com",
      cronExpression: "30 18 * * *"
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .RestrictWhereThisProjectCanBeRun('IntegrationTests')
        .DeleteWorkspaceBeforeBuildStarts()
        .SetSingleGitSource("Sitefinity/Tooling", "*/master")
        .CleanUiMachines()
        .GetJob()
    }
  }