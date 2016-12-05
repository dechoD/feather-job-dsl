package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.FeatherUnitTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class FeatherUnitTestJob {
  String name
  String description
  String emails
  String featherBranch

  Job build(DslFactory factory) {
    Job baseJob = new FeatherUnitTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .GetJob()
    }
  }