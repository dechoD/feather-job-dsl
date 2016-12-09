package jobs.helpers

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class BaseJobBuilder {
  String name
  String description = ""
  String emails
  String cronExpression

  Job build(DslFactory factory) {
    factory.job(name) {
      it.description this.description
      logRotator(-1, 50, -1, -1)

      if (this.cronExpression != null) {
        triggers {
          cron(this.cronExpression)
        }
      }

      if (this.emails != null) {
        publishers {
          mailer(this.emails, false, true)
        }
      }
    }
  }
}