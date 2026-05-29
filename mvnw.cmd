@REM Maven Wrapper Script for Windows
@REM Downloads Maven if not already present

@ECHO OFF
SET MAVEN_WRAPPER_VERSION=3.9.6
SET MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_WRAPPER_VERSION%
SET MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd
SET MAVEN_ZIP_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_WRAPPER_VERSION%/apache-maven-%MAVEN_WRAPPER_VERSION%-bin.zip
SET MAVEN_ZIP=%TEMP%\maven-wrapper.zip

IF NOT EXIST "%MAVEN_BIN%" (
    ECHO Downloading Apache Maven %MAVEN_WRAPPER_VERSION%...
    powershell -Command "Invoke-WebRequest -Uri '%MAVEN_ZIP_URL%' -OutFile '%MAVEN_ZIP%'"
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists\' -Force"
    IF NOT EXIST "%MAVEN_BIN%" (
        powershell -Command "Get-ChildItem '%USERPROFILE%\.m2\wrapper\dists\' | Where-Object { $_.Name -like 'apache-maven-*' } | ForEach-Object { Rename-Item $_.FullName '%MAVEN_HOME%' }"
    )
    DEL "%MAVEN_ZIP%"
)

"%MAVEN_BIN%" %*
