@echo off
REM Start Class Comparer Backend
REM Assumes Java 21 is installed and available in PATH
REM Maven should be installed or use mvnw if available

echo Starting Class Comparer Backend...
echo.

cd /d "%~dp0class-comparer\backend"

echo Cleaning and compiling...
if exist mvnw (
    call mvnw clean compile
) else (
    call mvn clean compile
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Copying dependencies...
if exist mvnw (
    call mvnw org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy-dependencies -DoutputDirectory=target/lib
) else (
    call mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy-dependencies -DoutputDirectory=target/lib
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Dependency copy failed!
    pause
    exit /b 1
)

echo.
echo Starting backend server on port 8080...
echo.
echo Application will be available at: http://localhost:8080
echo Press Ctrl+C to stop the server
echo.

java -cp "target\classes;target\lib\*" com.example.classcompare.ClassCompareApp

pause
