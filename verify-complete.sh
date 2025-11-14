#!/bin/bash

echo "🔍 Complete Project Verification"
echo "=================================="
echo ""

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

backend_files=(
  "src/main/java/com/resume/builder/ResumeBuilderApplication.java"
  "src/main/java/com/resume/builder/controller/ResumeController.java"
  "src/main/java/com/resume/builder/controller/JobDescriptionController.java"
  "src/main/java/com/resume/builder/service/ResumeParserService.java"
  "src/main/java/com/resume/builder/service/JDAnalyzerService.java"
  "src/main/java/com/resume/builder/service/MatchingService.java"
  "src/main/java/com/resume/builder/service/OptimizationService.java"
  "src/main/java/com/resume/builder/service/PDFGeneratorService.java"
  "src/main/java/com/resume/builder/model/Resume.java"
  "src/main/java/com/resume/builder/model/JobDescription.java"
  "src/main/java/com/resume/builder/model/MatchResult.java"
  "src/main/java/com/resume/builder/repository/ResumeRepository.java"
  "src/main/java/com/resume/builder/repository/JobDescriptionRepository.java"
  "src/main/java/com/resume/builder/repository/MatchResultRepository.java"
  "src/main/java/com/resume/builder/dto/ResumeDTO.java"
  "src/main/java/com/resume/builder/dto/MatchScoreDTO.java"
  "src/main/java/com/resume/builder/dto/OptimizationSuggestionDTO.java"
  "src/main/java/com/resume/builder/config/CorsConfig.java"
  "src/main/resources/application.properties"
  "pom.xml"
)

frontend_files=(
  "frontend/src/App.jsx"
  "frontend/src/main.jsx"
  "frontend/src/index.css"
  "frontend/src/services/api.js"
  "frontend/src/pages/Dashboard.jsx"
  "frontend/src/pages/ResumeUpload.jsx"
  "frontend/src/pages/ResumeBuilder.jsx"
  "frontend/src/pages/JobDescriptions.jsx"
  "frontend/src/pages/MatchingPage.jsx"
  "frontend/index.html"
  "frontend/package.json"
  "frontend/vite.config.js"
  "frontend/tailwind.config.js"
  "frontend/postcss.config.js"
)

pass=0
fail=0

echo "📦 BACKEND FILES"
echo "----------------"
for file in "${backend_files[@]}"; do
  if [ -f "$file" ]; then
    size=$(wc -l < "$file" 2>/dev/null || echo "0")
    if [ "$size" -gt 10 ]; then
      echo -e "${GREEN}✓${NC} $file ($size lines)"
      ((pass++))
    else
      echo -e "${YELLOW}⚠${NC} $file (only $size lines - might be incomplete)"
      ((pass++))
    fi
  else
    echo -e "${RED}✗${NC} $file (missing)"
    ((fail++))
  fi
done

echo ""
echo "🎨 FRONTEND FILES"
echo "-----------------"
for file in "${frontend_files[@]}"; do
  if [ -f "$file" ]; then
    size=$(wc -l < "$file" 2>/dev/null || echo "0")
    if [ "$size" -gt 5 ]; then
      echo -e "${GREEN}✓${NC} $file ($size lines)"
      ((pass++))
    else
      echo -e "${YELLOW}⚠${NC} $file (only $size lines - might be incomplete)"
      ((pass++))
    fi
  else
    echo -e "${RED}✗${NC} $file (missing)"
    ((fail++))
  fi
done

echo ""
echo "📊 SUMMARY"
echo "----------"
echo -e "${GREEN}Passed: $pass${NC}"
echo -e "${RED}Failed: $fail${NC}"

if [ $fail -eq 0 ]; then
  echo ""
  echo -e "${GREEN}✅ All files are present!${NC}"
  echo ""
  echo "Next steps:"
  echo "1. Start backend: ./start.sh"
  echo "2. Start frontend: cd frontend && npm run dev"
  echo "3. Open: http://localhost:3000"
else
  echo ""
  echo -e "${RED}⚠️ Some files are missing. Please check above.${NC}"
fi
