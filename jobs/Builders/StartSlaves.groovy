package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class StartSlaves {
  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: "SystemJob-StartSlaves_DSL",
      description: "Job that starts deallocated slaves.",
      emails: "ivan.dimitrov@telerik.com"
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      Iterable<String> jobs = ["SystemJob-StopSlaves"]

      jobBuilder
        .RestrictWhereThisProjectCanBeRun('Master')
        .BlockBuildIfCertainJobsAreRunning(jobs)
        .SetSingleGitSource("Sitefinity/Tooling", "*/master")
        .ToggleSlaves("Start")
        .GetJob()
    }
  }