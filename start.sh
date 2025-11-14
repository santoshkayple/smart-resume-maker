#!/bin/bash

echo "🚀 Smart Resume Builder - Quick Start Script"
echo "============================================"
echo ""

# Check Java version
echo "📋 Checking Java version..."
java -version 2>&1 | head -n 1

if [ $? -ne 0 ]; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo ""
echo "📦 Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""
echo "🎯 Starting the application..."
echo "   Server will be available at: http://localhost:8080"
echo "   H2 Console at: http://localhost:8080/h2-console"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

mvn spring-boot:run
