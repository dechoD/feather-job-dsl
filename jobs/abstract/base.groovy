package feather.ci.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class BaseJobBuilder {
  String name
  String description
  String emails

  Job build(DslFactory factory) {
    factory.job(name) {
      it.description this.description
      logRotator(-1, 50, -1, -1)
      publishers {
        mailer(this.emails, false, true)
      }
    }
  }
}