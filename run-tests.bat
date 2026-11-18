@echo off
REM Runs the pure-Java core tests: no Gradle, no Minecraft, no network.
REM Gson is on the classpath because ConfigStore and the backends use it in game, where
REM Minecraft already ships it; libs\gson-2.11.0.jar only exists so this harness can run.

setlocal EnableDelayedExpansion

set "OUT=build\test-classes"

if defined JAVA_HOME (
    set "JAVAC=%JAVA_HOME%\bin\javac.exe"
    set "JAVA=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVAC=javac"
    set "JAVA=java"
)

if not exist "libs\gson-2.11.0.jar" (
    echo [ERROR] libs\gson-2.11.0.jar is missing.
    exit /b 1
)

if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%OUT%" 2>nul

REM javac argument files treat a backslash as an escape character, so write forward slashes.
REM Paths are quoted because the workspace path contains spaces.
> "%OUT%\..\sources.txt" (
    for /r "src\main\java\com\justaivillagers\core" %%f in (*.java) do (
        set "P=%%f"
        echo "!P:\=/!"
    )
    for /r "src\test\java" %%f in (*.java) do (
        set "P=%%f"
        echo "!P:\=/!"
    )
)

"%JAVAC%" -encoding UTF-8 -cp "libs\gson-2.11.0.jar" -d "%OUT%" "@%OUT%\..\sources.txt"
if errorlevel 1 (
    echo [ERROR] Compilation failed.
    exit /b 1
)

"%JAVA%" -cp "%OUT%;libs\gson-2.11.0.jar" justaivillagers.test.CoreTests
if errorlevel 1 exit /b 1

echo.
echo [OK] Core tests passed.
endlocal
