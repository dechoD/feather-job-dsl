package jobs.helpers

import javaposse.jobdsl.dsl.jobs.*
import javaposse.jobdsl.dsl.Job

class JobBuilder {
  def job

  JobBuilder (FreeStyleJob _job) {
    job = _job
  }

  Job GetJob () {
    return job
  }

  JobBuilder RestrictWhereThisProjectCanBeRun(String agentLabel) {
    job.with {
      label(agentLabel)
    }

    return this
  }

  JobBuilder TriggerBuildOnGitPush() {
    job.with {
      triggers {
        githubPush()
      }
    }

    return this
  }

  JobBuilder DeleteWorkspaceBeforeBuildStarts()
  {
    job.with {
      wrappers {
        preBuildCleanup()
      }
    }

    return this
  }

  JobBuilder InjectEnvironmentalVariable(String variableName, String variableValue)
  {
    job.with {
      wrappers {
        environmentVariables {
          env(variableName, variableValue)
        }
      }
    }

    return this
  }

  JobBuilder RunClientTests() {
    job.with {
      steps {
        powerShell('''npm install
grunt''')
      }
    }

    return this
  }

  JobBuilder RunWidgetClientTests() {
    job.with {
      steps {
        powerShell('''cd feather-widgets
npm install
grunt''')
      }
    }

    return this
  }

  JobBuilder InstallFeatherPackages() {
    job.with {
      steps {
        batchFile('''rd %LOCALAPPDATA%\\NuGet\\Cache /s /q
.nuget\\NuGet.exe install ".\\Telerik.Sitefinity.Frontend\\packages.config" -source "\\\\telerik.com\\distributions\\OfficialReleases\\Sitefinity\\nuget\\feather;\\\\feather-ci\\C$\\FeatherFeed\\FeatherFeed\\Packages;http://feather-ci.cloudapp.net:8088/nuget/;https://www.nuget.org/api/v2/;http://nuget.sitefinity.com/nuget/"  -NonInteractive -RequireConsent -solutionDir ".\\ " -NoCache''')
      }
    }

    return this
  }

  JobBuilder AddNodeJsFolderToPath(String folderPath) {
    job.with {
      wrappers {
        nodejs(folderPath)
      }
    }

    return this
  }

  JobBuilder SetClientTestsGitSource(String branchToUse, String repository = 'Sitefinity/feather') {
    job.with {
      scm {
        git {
          branch(branchToUse)
          remote {
            github(repository, 'https')
            credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
          }
          extensions {
            wipeOutWorkspace()
          }
        }
      }
    }

    return this
  }

  JobBuilder SetWidgetClientTestsGitSources(String branchToUse) {
    job.with {
      multiscm {
        git {
          remote {
            github('Sitefinity/feather')
            credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
          }
          branch(branchToUse)
          extensions {
            relativeTargetDirectory('Feather')
            wipeOutWorkspace()
          }
        }
        git {
          remote {
            github('Sitefinity/feather-widgets')
            credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
          }
          branch(branchToUse)
          extensions {
            relativeTargetDirectory('feather-widgets')
            wipeOutWorkspace()
          }
        }
      }
    }

    return this
  }

  JobBuilder MSBuildProject(String fileTobuild, String targets ='Rebuild') {
    job.with {
      steps {
        msBuild {
          msBuildInstallation('.NET 4.0 64bit')
          buildFile("${fileTobuild}")
          args("/p:Configuration=\"Release\" /p:Platform=\"Any CPU\" /t:${targets}")
        }
      }
    }

    return this
  }

  JobBuilder RunWindowsExe(String filePath, String parameters, String failOnError) {
    job.with {
      configure {
        it / 'builders' / 'org.jenkinsci.plugins.windows__exe__runner.ExeBuilder'(plugin: 'windows-exe-runner@1.2') {
          'exeName'(filePath)
          'cmdLineArgs'(parameters)
          'failBuild'(failOnError)
        }
      }
    }

    return this
  }

  JobBuilder RunUnitTestsWithMSTest(String filePath) {
    job.with {
      configure {
        it / 'builders' / 'org.jenkinsci.plugins.MsTestBuilder'(plugin: 'mstestrunner@1.1.2') {
          'msTestName'('MsTest')
          'testFiles'(filePath)
          'resultFile'('tests.trx')
          'cmdLineArgs'('/testsettings:Tests\\TestSettings.testsettings')
          'continueOnFail'('false')
        }
      }
    }

    return this
  }

  JobBuilder PublishMSTestReport(String filePath) {
    job.with {
      configure {
        it / 'publishers' / 'hudson.plugins.mstest.MSTestPublisher'(plugin: 'mstest@0.19') {
          'testResultsFile'(filePath)
          'buildTime'('0')
          'failOnError'('false')
          'keepLongStdio'('true')
        }
      }
    }

    return this
  }

  JobBuilder BuildOtherProject(String project, String treshold) {
    job.with {
      publishers {
        downstream(project, treshold)
      }
    }

    return this
  }

  JobBuilder PublishEmmaCoverageReport(String filePath) {
    job.with {
      publishers {
        emma(filePath) {
          minClass(50)
          maxClass(100)
          minMethod(50)
          maxMethod(100)
          minBlock(50)
          maxBlock(80)
          minLine(50)
          maxLine(80)
          minCondition(0)
          maxCondition(0)
        }
      }
    }

    return this
  }

  JobBuilder PublishCoberturaCoverageReport(String filePath) {
    job.with {
      publishers {
        cobertura(filePath) {
          failNoReports(true)
          sourceEncoding('ASCII')

          // the following targets are added by default to check the method, line and conditional level coverage
          methodTarget(80, 0, 0)
          lineTarget(80, 0, 0)
          conditionalTarget(70, 0, 0)
        }
      }
    }

    return this
  }

  JobBuilder PublishJunitTestReport(String filePath) {
    job.with {
      publishers {
        archiveJunit(filePath) {
          retainLongStdout()
        }
      }
    }

    return this
  }
}