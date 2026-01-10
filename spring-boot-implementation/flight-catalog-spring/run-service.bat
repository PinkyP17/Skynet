@echo off
echo ========================================
echo Flight Catalog Spring Boot Service
echo ========================================
echo.

set MAVEN_HOME=C:\Users\msimo\Downloads\apache-maven-3.9.12-bin\apache-maven-3.9.12
set MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd

echo Checking for Maven...
if exist "%MAVEN_BIN%" (
    echo Maven found at %MAVEN_HOME%
    echo Starting Spring Boot service...
    echo.
    call "%MAVEN_BIN%" spring-boot:run
) else (
    where mvn >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo Maven found in PATH! Starting service...
        mvn spring-boot:run
    ) else (
        echo Maven not found.
        echo.
        echo Please check if Maven is at: %MAVEN_HOME%
        echo Or add Maven to your PATH environment variable.
        echo.
        pause
    )
)
