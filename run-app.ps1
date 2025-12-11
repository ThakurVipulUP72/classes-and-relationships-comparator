#!/usr/bin/env pwsh

# Set environment variables
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:MAVEN_HOME = "$(Get-Location)\tools\apache-maven-3.9.6"
$env:NODE_PATH = "$(Get-Location)\tools\nodejs\node-v20.18.1-win-x64"

# Add to PATH
$env:PATH = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:NODE_PATH;$env:PATH"

Write-Host "Starting Class Comparer Application..." -ForegroundColor Green
Write-Host "Java Home: $env:JAVA_HOME"
Write-Host "Maven Home: $env:MAVEN_HOME"
Write-Host "Node Path: $env:NODE_PATH"
Write-Host ""

# Navigate to backend and start it
Write-Host "Starting Backend..." -ForegroundColor Cyan
$backendPath = "$(Get-Location)\class-comparer\backend"
Set-Location $backendPath

Write-Host "Cleaning and compiling backend..."
& "$env:MAVEN_HOME\bin\mvn.cmd" clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Copying dependencies..."
& "$env:MAVEN_HOME\bin\mvn.cmd" org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy-dependencies -DoutputDirectory=target/lib

if ($LASTEXITCODE -ne 0) {
    Write-Host "Dependency copy failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Starting backend server on port 8080..." -ForegroundColor Green
& java -cp "target\classes;target\lib\*" com.example.classcompare.ClassCompareApp
