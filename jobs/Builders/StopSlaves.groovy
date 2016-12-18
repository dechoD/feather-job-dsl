package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class StopSlaves {
  Job build(DslFactory factory) {
    Job baseJob = new BaseJobBuilder(
      name: "SystemJob-StopSlaves",
      description: "Job that deallocate slaves during quiet hours.",
      emails: "ivan.dimitrov@telerik.com"
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      Iterable<String> jobs = ["SystemJob-StopSlaves"]

      jobBuilder
        .RestrictWhereThisProjectCanBeRun('Master')
        .BlockBuildIfCertainJobsAreRunning(jobs)
        .SetSingleGitSource("Sitefinity/Tooling", "*/master")
        .ToggleSlaves("Stop")
        .GetJob()
    }
  }