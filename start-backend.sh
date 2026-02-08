#!/bin/bash
# Start Class Comparer Backend
# Assumes Java 21 is installed

echo "=========================================="
echo "  Starting Class Comparer Backend"
echo "=========================================="
echo ""

# Navigate to backend directory
cd "$(dirname "$0")/class-comparer/backend" || exit 1

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "Please install Java 21 from: https://adoptium.net/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "⚠️  Warning: Java version is less than 21"
    echo "Current version: $(java -version 2>&1 | head -1)"
    echo "Required version: Java 21 or higher"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "✅ Java version: $(java -version 2>&1 | head -1)"
echo ""

# Build and run with Maven or mvnw
if [ -f "./mvnw" ]; then
    echo "📦 Building backend with Maven wrapper..."
    ./mvnw clean spring-boot:run
else
    if ! command -v mvn &> /dev/null; then
        echo "❌ Error: Maven is not installed and mvnw not found"
        echo "Please install Maven from: https://maven.apache.org/download.cgi"
        exit 1
    fi
    echo "📦 Building backend with Maven..."
    mvn clean spring-boot:run
fi

# This point is reached when the server stops
echo ""
echo "Backend server stopped."
