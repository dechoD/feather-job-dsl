package jobs.helpers

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class UiTestBase {
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
      .GetJob()
    }
  }