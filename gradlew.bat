@echo off
setlocal
set "GRADLE_VERSION=9.5.1"
set "BASE=%USERPROFILE%\.gradle\universal-template"
set "DIST=%BASE%\gradle-%GRADLE_VERSION%"
set "ZIP=%BASE%\gradle-%GRADLE_VERSION%-bin.zip"
set "URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip"
if not exist "%DIST%\bin\gradle.bat" (
  if not exist "%BASE%" mkdir "%BASE%"
  echo Bootstrapping Gradle %GRADLE_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -UseBasicParsing '%URL%' -OutFile '%ZIP%';" ^
    "if (Test-Path '%BASE%\tmp') { Remove-Item -Recurse -Force '%BASE%\tmp' };" ^
    "New-Item -ItemType Directory -Force '%BASE%\tmp' | Out-Null;" ^
    "Expand-Archive -Force '%ZIP%' '%BASE%\tmp';" ^
    "if (Test-Path '%DIST%') { Remove-Item -Recurse -Force '%DIST%' };" ^
    "Move-Item '%BASE%\tmp\gradle-%GRADLE_VERSION%' '%DIST%';" ^
    "Remove-Item -Recurse -Force '%BASE%\tmp'"
  if errorlevel 1 exit /b %errorlevel%
)
call "%DIST%\bin\gradle.bat" %*
exit /b %errorlevel%
