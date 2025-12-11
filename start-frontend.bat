@echo off
REM Start Class Comparer Frontend
REM Assumes Node.js is installed and available in PATH

echo Starting Class Comparer Frontend...
echo.

cd /d "%~dp0class-comparer\frontend"

echo Frontend will be available at: http://localhost:3000
echo Make sure the backend is running on port 8080
echo Press Ctrl+C to stop the server
echo.

REM Use npx to run http-server (will auto-download if needed)
npx http-server -p 3000 -c-1 --cors

pause
