package jobs.builders

import jobs.helpers.BaseJobBuilder
import jobs.helpers.JobBuilder
import jobs.helpers.ClientTestBase
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class ClientWidgetsTestJob {
  String name
  String description
  String emails
  String featherBranch

  Job build(DslFactory factory) {
    Job baseJob = new ClientTestBase(
      name: this.name,
      description: this.description,
      emails: this.emails
      ).build(factory)

      def jobBuilder = new JobBuilder(baseJob)

      jobBuilder
        .SetWidgetClientTestsGitSources(this.featherBranch)
        .RunWidgetClientTests()
        .GetJob()
    }
  }