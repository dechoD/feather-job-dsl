package utilities

import javaposse.jobdsl.dsl.jobs.*

class Steps {
  static void DownloadSitefinityAndTestRunner(FreeStyleJob job, String blobName) {
    job.with {
      steps {
        batchFile('''rd %LOCALAPPDATA%\\NuGet\\Cache /s /q

        C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\AzureBlobStorage.ps1; DeleteLocalBlobStorage; DownloadFromBlobStorage -BlobName ''' + blobName + '''; DownloadFromBlobStorage -BlobName 'Telerik.WebTestRunner.zip' -UnzipLocation 'C:\\Tools\\Telerik.WebTestRunner.Cmd'"

        C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe restore C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln -source "http://feather-ci.cloudapp.net:8088/nuget/;\\\\telerik.com\\distributions\\OfficialReleases\\Sitefinity\\nuget;http://nuget.sitefinity.com/nuget;https://www.nuget.org/api/v2" -NoCache''')
      }
    }
  }

  static void MSBuildProject(FreeStyleJob job, String fileTobuild) {
    job.with {
      steps {
        msBuild {
          msBuildInstallation('.NET 4.0 64bit')
          buildFile("${fileTobuild}")
          args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
        }
      }
    }
  }

  static void RestoreSitefinityNugetPackages(FreeStyleJob job) {
    job.with {
      steps {
        batchFile('"C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe" restore "C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln" -source "http://feather-ci.cloudapp.net:8088/nuget/;http://nuget.sitefinity.com/nuget/;https://www.nuget.org/api/v2" -NoCache')
      }
    }
  }

  static void CopyFeatherDllAndPackages(FreeStyleJob job) {
    job.with {
      steps {
        batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\FeatherWidgets\\' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "

        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.dll' "
        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend.Data\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "
        powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Mvc.dll' "

        powershell.exe -executionpolicy unrestricted -noninteractive -command "Remove-Item 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages\\*' -recurse; Get-ChildItem Bootstrap -path '.\\FeatherPackages' | Copy-Item -destination 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages' -force -recurse"''')
      }
    }
  }

  static void SetupSitefinityWithFeather(FreeStyleJob job) {
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
      }

      static void RecreateFolder(FreeStyleJob job, String folderPath) {
        job.with {
          steps {
            batchFile("""echo 'Recreate test results folder'
            rd /S /Q ${folderPath}
            mkdir ${folderPath}""")
          }
        }
      }

      static void EnsureSitefinityIsRunning(FreeStyleJob job) {
        job.with {
          steps {
            batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Config.ps1;import-module .\\Tooling\\Feather\\SetupScripts\\IIS.ps1;EnsureSitefinityIsRunning $($config.SitefinitySite.sslurl)''')
          }
        }
      }

      static void RunIntegrationTests(FreeStyleJob job) {
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
      }

      static void PublishMSTestReport(FreeStyleJob job, String filePath) {
        job.with {
          configure {
            it / 'publishers' / 'hudson.plugins.mstest.MSTestPublisher'(plugin: 'mstest@0.19') {
              'testResultsFile'('TestResults\\*.trx')
              'buildTime'('0')
              'failOnError'('false')
              'keepLongStdio'('true')
            }
          }
        }
      }
    }