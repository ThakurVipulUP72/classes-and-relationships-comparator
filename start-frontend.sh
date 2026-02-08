#!/bin/bash
# Start Class Comparer Frontend
# Requires Python 3 or Node.js

echo "=========================================="
echo "  Starting Class Comparer Frontend"
echo "=========================================="
echo ""

# Navigate to frontend directory
cd "$(dirname "$0")/class-comparer/frontend" || exit 1

# Check if Python 3 is available
if command -v python3 &> /dev/null; then
    echo "✅ Using Python 3"
    echo ""
    echo "🌐 Frontend will be available at: http://localhost:3000"
    echo "📝 Make sure the backend is running on port 8080"
    echo "🛑 Press Ctrl+C to stop the server"
    echo ""
    python3 -m http.server 3000
# Check if Python (2) is available
elif command -v python &> /dev/null; then
    echo "✅ Using Python 2"
    echo ""
    echo "🌐 Frontend will be available at: http://localhost:3000"
    echo "📝 Make sure the backend is running on port 8080"
    echo "🛑 Press Ctrl+C to stop the server"
    echo ""
    python -m SimpleHTTPServer 3000
# Check if Node.js is available
elif command -v node &> /dev/null || command -v npx &> /dev/null; then
    echo "✅ Using Node.js http-server"
    echo ""
    echo "🌐 Frontend will be available at: http://localhost:3000"
    echo "📝 Make sure the backend is running on port 8080"
    echo "🛑 Press Ctrl+C to stop the server"
    echo ""
    npx http-server -p 3000 -c-1 --cors
else
    echo "❌ Error: No suitable HTTP server found"
    echo ""
    echo "Please install one of the following:"
    echo "  - Python 3: https://www.python.org/downloads/"
    echo "  - Node.js: https://nodejs.org/"
    echo ""
    exit 1
fi

# This point is reached when the server stops
echo ""
echo "Frontend server stopped."
