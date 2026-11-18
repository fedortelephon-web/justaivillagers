@echo off
REM Builds the mod. Requires a JDK 21+ on PATH or JAVA_HOME set.
REM Loom (the Fabric build plugin) needs Java 21 to run the compiler and Java 21 to target.

setlocal

if defined JAVA_HOME (
    set "JAVA=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA=java"
)

"%JAVA%" -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found. Install JDK 21 and set JAVA_HOME.
    exit /b 1
)

if not exist "gradlew.bat" (
    echo [ERROR] gradlew.bat is missing. Regenerate the wrapper with: gradle wrapper
    exit /b 1
)

call gradlew.bat --no-daemon build
if errorlevel 1 (
    echo.
    echo [ERROR] Build failed. See the output above.
    exit /b 1
)

echo.
echo [OK] Built jars:
dir /b build\libs\*.jar
endlocal
