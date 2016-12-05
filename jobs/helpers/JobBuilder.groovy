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

  // Integration test steps before Stoyan's refactoring
  JobBuilder DownloadSitefinityAndTestRunner(String blobName) {
    job.with {
      steps {
        batchFile('''rd %LOCALAPPDATA%\\NuGet\\Cache /s /q

        C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\AzureBlobStorage.ps1; DeleteLocalBlobStorage; DownloadFromBlobStorage -BlobName ''' + blobName + '''; DownloadFromBlobStorage -BlobName 'Telerik.WebTestRunner.zip' -UnzipLocation 'C:\\Tools\\Telerik.WebTestRunner.Cmd'"

        C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe restore C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln -source "http://feather-ci.cloudapp.net:8088/nuget/;\\\\telerik.com\\distributions\\OfficialReleases\\Sitefinity\\nuget;http://nuget.sitefinity.com/nuget;https://www.nuget.org/api/v2" -NoCache''')
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

  JobBuilder RestoreSitefinityNugetPackages() {
    job.with {
      steps {
        batchFile('"C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe" restore "C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln" -source "http://feather-ci.cloudapp.net:8088/nuget/;http://nuget.sitefinity.com/nuget/;https://www.nuget.org/api/v2" -NoCache')
      }
    }

    return this
  }

  JobBuilder CopyFeatherDllAndPackages() {
    job.with {
      steps {
        batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\FeatherWidgets\\' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "

        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.dll' "
        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend.Data\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "
        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Mvc.dll' "

        powershell.exe -executionpolicy unrestricted -noninteractive -command "Remove-Item 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages\\*' -recurse; Get-ChildItem Bootstrap -path '.\\FeatherPackages' | Copy-Item -destination 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages' -force -recurse"''')
      }
    }

    return this
  }

  JobBuilder SetupSitefinityWithFeather() {
    job.with {
      steps {
        batchFile('''if exist %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe (
          echo "Powershell path is %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe"
          %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\SitefinitySetup.ps1 -ArgumentList $true, $false, $true, $true;import-module .\\Tooling\\Feather\\SetupScripts\\FeatherSetup.ps1;DeleteFeatherWidgets;InstallFeatherPackages .\\FeatherPackages;InstallFeatherWidgets .\\FeatherWidgets;CopyTestAssemblies .\\Feather $websiteBinariesDirectory;"
          ) else (
            echo "Powershell path is %windir%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe"
            %windir%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\SitefinitySetup.ps1 -ArgumentList $true, $false, $true, $true;import-module .\\Tooling\\Feather\\SetupScripts\\FeatherSetup.ps1;DeleteFeatherWidgets;InstallFeatherPackages .\\FeatherPackages;InstallFeatherWidgets .\\FeatherWidgets;CopyTestAssemblies .\\Feather $websiteBinariesDirectory;"
            )''')
          }
        }

        return this
      }

      JobBuilder RecreateFolder(String folderPath) {
        job.with {
          steps {
            batchFile("""echo 'Recreate test results folder'
            rd /S /Q ${folderPath}
            mkdir ${folderPath}""")
          }
        }

        return this
      }

      JobBuilder EnsureSitefinityIsRunning() {
        job.with {
          steps {
            batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Config.ps1;import-module .\\Tooling\\Feather\\SetupScripts\\IIS.ps1;EnsureSitefinityIsRunning $($config.SitefinitySite.sslurl)''')
          }
        }

        return this
      }

      JobBuilder RunIntegrationTests() {
        job.with {
          configure {
            it / 'builders' / 'org.jenkinsci.plugins.windows__exe__runner.ExeBuilder'(plugin: 'windows-exe-runner@1.2') {
              'exeName'('')
              'cmdLineArgs'('''run
              /Url="https://localhost:443/";
              /assemblyname="Telerik.Sitefinity.Frontend.TestIntegration"
              /TimeOutInMinutes="300"
              /TraceFilePath="TestResults\\\$JOB_NAME-\$BUILD_NUMBER-\$BUILD_ID.trx";
              /RunName="FeatherIntegrationTests";
              /LoggerType=MsTrx
              /WriteTestResultToConsole="true"''')
              'failBuild'('false')
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

    // End of integration test steps before Stoyan's refactoring