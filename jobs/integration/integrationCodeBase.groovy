package feather.ci.builders.integration

import feather.ci.builders.*
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*

class IntegrationCodeBaseJobBuilder {
  String name
  String description
  String emails
  String featherBranch
  String sitefinityBlob

  Job build(DslFactory factory) {
    Job baseJob = new IntegrationJobBuilder(
      name: this.name,
      description: this.description,
      emails: this.emails,
      featherBranch: this.featherBranch
      ).build(factory)

      baseJob.with {
        steps {
          batchFile('''rd %LOCALAPPDATA%\\NuGet\\Cache /s /q

C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\AzureBlobStorage.ps1; DeleteLocalBlobStorage; DownloadFromBlobStorage -BlobName ''' + this.sitefinityBlob + '''; DownloadFromBlobStorage -BlobName 'Telerik.WebTestRunner.zip' -UnzipLocation 'C:\\Tools\\Telerik.WebTestRunner.Cmd'"

C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe restore C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln -source "http://feather-ci.cloudapp.net:8088/nuget/;\\\\telerik.com\\distributions\\OfficialReleases\\Sitefinity\\nuget;http://nuget.sitefinity.com/nuget;https://www.nuget.org/api/v2" -NoCache''')
          msBuild {
            msBuildInstallation('.NET 4.0 64bit')
            buildFile('C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln')
            args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
          }
          batchFile('''"C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe" restore ".\\sitefinity-mvc\\Telerik.Sitefinity.Mvc.sln" -source "http://feather-ci.cloudapp.net:8088/nuget/;http://nuget.sitefinity.com/nuget/;https://www.nuget.org/api/v2" -NoCache

"C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe" restore ".\\Feather\\Feather.sln" -source "http://feather-ci.cloudapp.net:8088/nuget/;http://nuget.sitefinity.com/nuget/;https://www.nuget.org/api/v2" -NoCache''')
          batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies 'C:\\AzureBlobStorage\\SitefinityWebApp\\bin' '.\\sitefinity-mvc\\packages\\' -exclude @('System.Web.Mvc.dll')"

powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies 'C:\\AzureBlobStorage\\SitefinityWebApp\\bin' '.\\Feather\\packages\\' -exclude @('System.Web.Mvc.dll')"''')
          msBuild {
            msBuildInstallation('.NET 4.0 64bit')
            buildFile('.\\sitefinity-mvc\\Telerik.Sitefinity.Mvc.sln')
            args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
          }
          batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies '.\\sitefinity-mvc\\Telerik.Sitefinity.Mvc\\bin' '.\\Feather\\packages\\' 'Telerik.Sitefinity.Mvc.dll'"

powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies '.\\sitefinity-mvc\\Tests\\Telerik.Sitefinity.Mvc.TestUtilities\\bin' '.\\Feather\\packages\\' 'Telerik.Sitefinity.Mvc.TestUtilities.dll'"''')
          msBuild {
            msBuildInstallation('.NET 4.0 64bit')
            buildFile('.\\Feather\\Feather.sln')
            args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
          }
          batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies 'C:\\AzureBlobStorage\\SitefinityWebApp\\bin' '.\\FeatherWidgets\\packages\\' -exclude @('System.Web.Mvc.dll')"

powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies '.\\Feather\\Telerik.Sitefinity.Frontend\\bin' '.\\FeatherWidgets\\packages' -exclude @('System.Web.Mvc.dll')"

echo "Copy feather assembly to feather widgets"
copy .\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release\\Telerik.Sitefinity.Frontend.dll .\\FeatherWidgets\\ReferenceAssemblies /Y
copy .\\Feather\\Tests\\Telerik.Sitefinity.Frontend.TestUtilities\\bin\\Release\\Telerik.Sitefinity.Frontend.TestUtilities.dll .\\FeatherWidgets\\ReferenceAssemblies /Y''')
          msBuild {
          msBuildInstallation('.NET 4.0 64bit')
          buildFile('.\\FeatherWidgets\\FeatherWidgets.sln')
          args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
        }
        batchFile('''"C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe" restore "C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln" -source "http://feather-ci.cloudapp.net:8088/nuget/;http://nuget.sitefinity.com/nuget/;https://www.nuget.org/api/v2" -NoCache

powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\FeatherWidgets\\' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "

powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.dll' "
powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend.Data\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Frontend.*.dll' "
powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Utilities.ps1;ReplaceAssemblies -from '.\\Feather\\Telerik.Sitefinity.Frontend\\bin\\Release' -to 'C:\\AzureBlobStorage\\SitefinityWebApp\\packages' -filter 'Telerik.Sitefinity.Mvc.dll' "

powershell.exe -executionpolicy unrestricted -noninteractive -command "Remove-Item 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages\\*' -recurse; Get-ChildItem Bootstrap -path '.\\FeatherPackages' | Copy-Item -destination 'C:\\AzureBlobStorage\\SitefinityWebApp\\ResourcePackages' -force -recurse"''')
        msBuild {
          msBuildInstallation('.NET 4.0 64bit')
          buildFile('C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln')
          args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
        }
        batchFile('''if exist %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe (
   echo "Powershell path is %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe"
   %windir%\\sysnative\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\SitefinitySetup.ps1 -ArgumentList $true, $false, $true, $true, $false;import-module .\\Tooling\\Feather\\SetupScripts\\FeatherSetup.ps1;DeleteFeatherWidgets;InstallFeatherPackages .\\FeatherPackages;InstallFeatherWidgets .\\FeatherWidgets;CopyTestAssemblies .\\Feather $websiteBinariesDirectory;"
) else (
   echo "Powershell path is %windir%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe"
   %windir%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\SitefinitySetup.ps1 -ArgumentList $true, $false, $true, $true, $false;import-module .\\Tooling\\Feather\\SetupScripts\\FeatherSetup.ps1;DeleteFeatherWidgets;InstallFeatherPackages .\\FeatherPackages;InstallFeatherWidgets .\\FeatherWidgets;CopyTestAssemblies .\\Feather $websiteBinariesDirectory;"
)

echo "Recreate test results folder"
rd /S /Q TestResults
mkdir TestResults''')
        batchFile('''powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\Config.ps1;import-module .\\Tooling\\Feather\\SetupScripts\\IIS.ps1;EnsureSitefinityIsRunning $($config.SitefinitySite.sslurl)''')
        }
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
        configure {
          it / 'publishers' / 'hudson.plugins.mstest.MSTestPublisher'(plugin: 'mstest@0.19') {
            'testResultsFile'('TestResults\\*.trx')
            'buildTime'('0')
            'failOnError'('false')
            'keepLongStdio'('true')
          }
        }
      }

      return baseJob
    }
}