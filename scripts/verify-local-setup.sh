#!/bin/bash

# Local CI/CD verification script for MineMod
# This script helps developers verify their local setup before pushing changes

set -e

echo "🚀 MineMod CI/CD Local Verification"
echo "======================================"
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ "$2" == "success" ]; then
        echo -e "${GREEN}✅ $1${NC}"
    elif [ "$2" == "warning" ]; then
        echo -e "${YELLOW}⚠️  $1${NC}"
    elif [ "$2" == "error" ]; then
        echo -e "${RED}❌ $1${NC}"
    else
        echo "ℹ️  $1"
    fi
}

# Check Java version
echo "🔍 Checking Java version..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "?\K[0-9]+')
    if [ "$JAVA_VERSION" -ge 21 ]; then
        print_status "Java $JAVA_VERSION detected" "success"
    else
        print_status "Java $JAVA_VERSION detected, but Java 21+ is required" "warning"
        echo "   Install Java 21 from: https://adoptium.net/"
    fi
else
    print_status "Java not found" "error"
    echo "   Install Java 21 from: https://adoptium.net/"
    exit 1
fi

# Check Gradle wrapper
echo ""
echo "🔍 Checking Gradle wrapper..."
if [ -f "./gradlew" ]; then
    if [ -x "./gradlew" ]; then
        print_status "Gradle wrapper is executable" "success"
    else
        print_status "Gradle wrapper exists but is not executable" "warning"
        chmod +x ./gradlew
        print_status "Fixed: Made gradlew executable" "success"
    fi
else
    print_status "Gradle wrapper not found" "error"
    exit 1
fi

# Test Gradle build
echo ""
echo "🔨 Testing Gradle build..."
print_status "Running: ./gradlew clean compileJava..." 
if ./gradlew clean compileJava --no-daemon --quiet; then
    print_status "Gradle compilation successful" "success"
else
    print_status "Gradle compilation failed" "error"
    echo "   Check the error messages above and fix compilation issues"
    exit 1
fi

# Test running tests
echo ""
echo "🧪 Testing JUnit tests..."
print_status "Running: ./gradlew test..."
if ./gradlew test --no-daemon --quiet; then
    print_status "All tests passed" "success"
else
    print_status "Some tests failed" "warning"
    echo "   Check test reports in: build/reports/tests/test/index.html"
fi

# Check workflow files
echo ""
echo "🔍 Validating workflow files..."
if [ -d ".github/workflows" ]; then
    WORKFLOW_COUNT=$(find .github/workflows -name "*.yml" -o -name "*.yaml" | wc -l)
    print_status "Found $WORKFLOW_COUNT workflow files" "success"
    
    # List workflow files
    for workflow in .github/workflows/*.yml .github/workflows/*.yaml; do
        if [ -f "$workflow" ]; then
            print_status "  - $(basename "$workflow")" 
        fi
    done
else
    print_status "No .github/workflows directory found" "warning"
fi

# Check for common issues
echo ""
echo "🔍 Checking for common issues..."

# Check gitignore
if [ -f ".gitignore" ]; then
    if grep -q "build/" .gitignore && grep -q ".gradle" .gitignore; then
        print_status "Build artifacts properly ignored" "success"
    else
        print_status "Build artifacts may not be properly ignored" "warning"
    fi
else
    print_status "No .gitignore file found" "warning"
fi

# Check mod properties
if [ -f "gradle.properties" ]; then
    if grep -q "mod_id" gradle.properties && grep -q "mod_version" gradle.properties; then
        print_status "Mod properties configured" "success"
    else
        print_status "Mod properties may be incomplete" "warning"
    fi
fi

# Final summary
echo ""
echo "📋 Verification Summary"
echo "========================"
print_status "Local setup verification complete!"

echo ""
echo "🚀 Next steps:"
echo "   1. Fix any warnings shown above"
echo "   2. Commit your changes"
echo "   3. Push to trigger CI/CD pipeline"
echo "   4. Create a release tag (v1.0.0) to test CD workflow"

echo ""
print_status "Ready for CI/CD! 🎉" "success"