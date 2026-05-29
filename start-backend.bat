@ECHO OFF
TITLE CollegeFinder Backend Startup
COLOR 0A
ECHO.
ECHO ============================================================
ECHO   CollegeFinder - Spring Boot Backend Startup
ECHO ============================================================
ECHO.

REM Check Java
java -version >NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    ECHO [ERROR] Java is not installed or not in PATH!
    ECHO Please install Java 17 from https://adoptium.net/
    PAUSE
    EXIT /B 1
)

ECHO [OK] Java found.

REM Set MySQL path
SET MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe

REM Create database if not exists
ECHO [INFO] Ensuring college_db database exists...
"%MYSQL_BIN%" -u root "-p220298158ns#NS" -e "CREATE DATABASE IF NOT EXISTS college_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>NUL
ECHO [OK] Database ready.

ECHO.
ECHO [INFO] Looking for Maven...

REM Try system mvn first
mvn -version >NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
    ECHO [OK] Maven found in PATH.
    SET MVN_CMD=mvn
    GOTO :START_BACKEND
)

REM Try common install locations
IF EXIST "C:\Program Files\Apache Maven\bin\mvn.cmd" (
    SET MVN_CMD=C:\Program Files\Apache Maven\bin\mvn.cmd
    ECHO [OK] Maven found at: %MVN_CMD%
    GOTO :START_BACKEND
)

IF EXIST "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd" (
    SET MVN_CMD=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd
    ECHO [OK] Maven wrapper found.
    GOTO :START_BACKEND
)

REM Download Maven
ECHO [INFO] Maven not found. Downloading Apache Maven 3.9.6...
SET MAVEN_ZIP=%TEMP%\apache-maven-3.9.6-bin.zip
SET MAVEN_DIR=%USERPROFILE%\.m2\wrapper\dists
powershell -Command "Write-Host 'Downloading Maven...' ; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile '%MAVEN_ZIP%'"
powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_DIR%' -Force"
SET MVN_CMD=%MAVEN_DIR%\apache-maven-3.9.6\bin\mvn.cmd
DEL "%MAVEN_ZIP%"
ECHO [OK] Maven downloaded.

:START_BACKEND
ECHO.
ECHO ============================================================
ECHO   Starting Spring Boot application on port 8080...
ECHO   Press CTRL+C to stop.
ECHO ============================================================
ECHO.

CD /D "%~dp0"
"%MVN_CMD%" spring-boot:run

PAUSE
