#!/bin/bash

echo "🔍 Smart Resume Builder - Project Verification"
echo "=============================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

success_count=0
fail_count=0

check_item() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $2"
        ((success_count++))
    else
        echo -e "${RED}✗${NC} $2"
        ((fail_count++))
    fi
}

echo "📋 Checking Prerequisites..."
echo ""

# Check Java
java -version > /dev/null 2>&1
check_item $? "Java is installed"

# Check Maven
mvn -version > /dev/null 2>&1
check_item $? "Maven is installed"

echo ""
echo "📁 Checking Project Structure..."
echo ""

# Check main application file
[ -f "src/main/java/com/resume/builder/ResumeBuilderApplication.java" ]
check_item $? "Main application class exists"

# Check pom.xml
[ -f "pom.xml" ]
check_item $? "pom.xml exists"

# Check application.properties
[ -f "src/main/resources/application.properties" ]
check_item $? "application.properties exists"

# Check controllers
[ -d "src/main/java/com/resume/builder/controller" ]
check_item $? "Controller package exists"

# Check services
[ -d "src/main/java/com/resume/builder/service" ]
check_item $? "Service package exists"

# Check models
[ -d "src/main/java/com/resume/builder/model" ]
check_item $? "Model package exists"

# Check repositories
[ -d "src/main/java/com/resume/builder/repository" ]
check_item $? "Repository package exists"

echo ""
echo "🔧 Checking Service Files..."
echo ""

[ -f "src/main/java/com/resume/builder/service/ResumeParserService.java" ]
check_item $? "ResumeParserService exists"

[ -f "src/main/java/com/resume/builder/service/JDAnalyzerService.java" ]
check_item $? "JDAnalyzerService exists"

[ -f "src/main/java/com/resume/builder/service/MatchingService.java" ]
check_item $? "MatchingService exists"

[ -f "src/main/java/com/resume/builder/service/OptimizationService.java" ]
check_item $? "OptimizationService exists"

[ -f "src/main/java/com/resume/builder/service/PDFGeneratorService.java" ]
check_item $? "PDFGeneratorService exists"

echo ""
echo "🎮 Checking Controllers..."
echo ""

[ -f "src/main/java/com/resume/builder/controller/ResumeController.java" ]
check_item $? "ResumeController exists"

[ -f "src/main/java/com/resume/builder/controller/JobDescriptionController.java" ]
check_item $? "JobDescriptionController exists"

echo ""
echo "📊 Checking Models..."
echo ""

[ -f "src/main/java/com/resume/builder/model/Resume.java" ]
check_item $? "Resume model exists"

[ -f "src/main/java/com/resume/builder/model/JobDescription.java" ]
check_item $? "JobDescription model exists"

[ -f "src/main/java/com/resume/builder/model/MatchResult.java" ]
check_item $? "MatchResult model exists"

echo ""
echo "📝 Checking Documentation..."
echo ""

[ -f "README.md" ]
check_item $? "README.md exists"

[ -f "QUICKSTART.md" ]
check_item $? "QUICKSTART.md exists"

[ -f "PROJECT-SUMMARY.md" ]
check_item $? "PROJECT-SUMMARY.md exists"

echo ""
echo "🧪 Checking Sample Files..."
echo ""

[ -f "sample-resume.txt" ]
check_item $? "Sample resume exists"

[ -f "sample-job-description.txt" ]
check_item $? "Sample job description exists"

echo ""
echo "=============================================="
echo -e "${GREEN}✓ Passed: $success_count${NC}"
echo -e "${RED}✗ Failed: $fail_count${NC}"
echo ""

if [ $fail_count -eq 0 ]; then
    echo -e "${GREEN}🎉 All checks passed! Project is ready.${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Run: ./start.sh"
    echo "2. Access: http://localhost:8080"
    echo "3. Read: QUICKSTART.md for testing"
    exit 0
else
    echo -e "${RED}⚠️  Some checks failed. Please review the errors above.${NC}"
    exit 1
fi
