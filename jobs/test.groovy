job('Codebase_FeatherIntegrationTests') {
  logRotator(-1, 50, -1, -1)
  label('IntegrationTests')
  multiscm {
    git {
      remote {
        github('Sitefinity/feather')
        credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
      }
      branch('*/CodeBaseIntegration')
      extensions {
        relativeTargetDirectory('Feather')
      }
    }
    git {
      remote {
        github('Sitefinity/feather-packages')
        credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
      }
      branch('*/CodeBaseIntegration')
      extensions {
        relativeTargetDirectory('FeatherPackages')
      }
    }
    git {
      remote {
        github('Sitefinity/Tooling')
        credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
      }
      branch('*/master')
      extensions {
        relativeTargetDirectory('Tooling')
      }
    }
    git {
      remote {
        github('Sitefinity/sitefinity-mvc')
        credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
      }
      branch('*/CodeBaseIntegration')
      extensions {
        relativeTargetDirectory('sitefinity-mvc')
      }
    }
    git {
      remote {
        github('Sitefinity/feather-widgets')
        credentials('db15f140-2fb2-427a-bde2-ae2c940b4e98')
      }
      branch('*/CodeBaseIntegration')
      extensions {
        relativeTargetDirectory('FeatherWidgets')
      }
    }
  }
  wrappers {
    preBuildCleanup()
  }
  triggers {
    cron("H 22 * * 1-5")
  }
  steps {
    batchFile('rd %LOCALAPPDATA%\\NuGet\\Cache /s /q & C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe -executionpolicy unrestricted -noninteractive -command "import-module .\\Tooling\\Feather\\SetupScripts\\AzureBlobStorage.ps1; DeleteLocalBlobStorage; DownloadFromBlobStorage -BlobName "SitefinityWebApp_10.0.6400.0_Feather.zip"; DownloadFromBlobStorage -BlobName "Telerik.WebTestRunner.zip" -UnzipLocation "C:\\Tools\\Telerik.WebTestRunner.Cmd" & C:\\AzureBlobStorage\\SitefinityWebApp\\.nuget\\NuGet.exe restore C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln -source "http://feather-ci.cloudapp.net:8088/nuget/;\\\\telerik.com\\distributions\\OfficialReleases\\Sitefinity\\nuget;http://nuget.sitefinity.com/nuget;https://www.nuget.org/api/v2" -NoCache')
    msBuild {
      msBuildInstallation('.NET 4.0 64bit')
      buildFile('C:\\AzureBlobStorage\\SitefinityWebApp\\SitefinityWebApp.sln')
      args('/p:Configuration="Release" /p:Platform="Any CPU" /t:Rebuild')
    }
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
  publishers {
    mailer('tihomir.petrov@telerik.com', false, true)
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