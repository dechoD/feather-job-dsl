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

  JobBuilder SetUiTestParameters(String branch, String sitefinityPackage, String category, Boolean sslEnabled, Boolean enableMultisite, Boolean readOnlyMode, Boolean rerunFailedUITests) {
    job.with {
      parameters {
        stringParam("Branch", branch)
        stringParam("SitefinityPackage", sitefinityPackage)
        stringParam("Category", category)
        booleanParam("SslEnabled", sslEnabled)
        booleanParam("EnableMultisite", enableMultisite)
        booleanParam("ReadOnlyMode", readOnlyMode)
        booleanParam("RerunFailedUITests", rerunFailedUITests)
      }
    }

    return this
  }

  JobBuilder ExecuteConcurentBuilds() {
    job.with {
      concurrentBuild()
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

  JobBuilder DeleteWorkspaceWhenBuildIsDone()
  {
    job.with {
      publishers {
        wsCleanup()
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

  JobBuilder RunUiTests(String command) {
    job.with {
      steps {
        batchFile('C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "' + command + ' -sitefinityPackage \'%SitefinityPackage%\' -buildNumber \'%JOB_NAME%_%BUILD_NUMBER%\' -category \'%Category%\' -sslEnabled $%SslEnabled% -multisiteEnabled $%EnableMultisite% -readOnlyMode $%ReadOnlyMode% -rerunFailedUITests $%RerunFailedUITests%"')
      }
    }

    return this
  }

  JobBuilder RunMvcUnitTests() {
    job.with {
      steps {
        batchFile('''echo Enabling JustMock...

SET JUSTMOCK_INSTANCE=1
SET COR_ENABLE_PROFILING=1
SET COR_PROFILER={B7ABE522-A68F-44F2-925B-81E7488E9EC0}

echo Run unit tests...

"C:\\Progra~2\\Microsoft Visual Studio 12.0\\Common7\\IDE\\MSTest.exe" /resultsfile:tests.trx /testcontainer:Tests\\Telerik.Sitefinity.Mvc.TestUnit\\bin\\Release\\Telerik.Sitefinity.Mvc.TestUnit.dll''')
      }
    }

    return this
  }

  JobBuilder PushMvcNuget() {
    job.with {
      steps {
        batchFile('''.nuget\\nuget pack "Telerik.Sitefinity.Mvc\\Telerik.Sitefinity.Mvc.csproj" -Properties "Configuration=Release"  -Verbose
.nuget\\nuget pack "Tests\\Telerik.Sitefinity.Mvc.TestUtilities\\Telerik.Sitefinity.Mvc.TestUtilities.csproj" -Properties "Configuration=Release"  -Verbose

FOR /F "tokens=*" %%G IN ('dir /b Telerik.Sitefinity.Mvc.*.nupkg') DO echo %%G
@ECHO OFF
FOR /F "tokens=*" %%G IN ('dir /b Telerik.Sitefinity.Mvc.*.nupkg') DO .nuget\\nuget push %%G -s http://feather-ci.cloudapp.net:8088/ 1221C1AF59B2C

FOR /F "tokens=*" %%G IN ('dir /b Telerik.Sitefinity.Mvc.TestUtilities.*.nupkg') DO echo %%G
@ECHO OFF
FOR /F "tokens=*" %%G IN ('dir /b Telerik.Sitefinity.Mvc.TestUtilities.*.nupkg') DO .nuget\\nuget push %%G -s http://feather-ci.cloudapp.net:8088/ 1221C1AF59B2C''')
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

  JobBuilder SetUiTestsGitSources(String branchToUse) {
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
            relativeTargetDirectory('FeatherWidgets')
            wipeOutWorkspace()
          }
        }
        git {
          remote {
            github('Sitefinity/feather-packages')
            credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
          }
          branch(branchToUse)
          extensions {
            relativeTargetDirectory('FeatherPackages')
            wipeOutWorkspace()
          }
        }
        git {
          remote {
            github('Sitefinity/Tooling')
            credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
          }
          branch('*/tooling-refactoring')
          extensions {
            relativeTargetDirectory('Tooling')
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