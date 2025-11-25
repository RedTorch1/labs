#!/bin/bash

echo "🚀 Starting Newman API Tests for Lab6"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Navigate to script directory
cd "$(dirname "$0")/.."

# Check if Newman is installed
if ! command -v newman &> /dev/null; then
    echo -e "${YELLOW}Newman not found, installing...${NC}"
    npm install -g newman newman-reporter-htmlextra newman-reporter-json
fi

# Create reports directory
mkdir -p reports

echo -e "${YELLOW}Running API tests for Manual implementation...${NC}"

# Run tests for Manual implementation
newman run collections/lab6-api-tests.postman_collection.json \
    -e environments/lab6-env.postman_environment.json \
    --env-var "baseUrl=http://localhost:8080/lab6-manual" \
    -r htmlextra,json,cli \
    --reporter-htmlextra-export reports/manual-html-report.html \
    --reporter-json-export reports/manual-json-report.json \
    --delay-request 100

MANUAL_EXIT_CODE=$?

echo -e "${YELLOW}Running API tests for Framework implementation...${NC}"

# Run tests for Framework implementation
newman run collections/lab6-api-tests.postman_collection.json \
    -e environments/lab6-env.postman_environment.json \
    --env-var "baseUrl=http://localhost:8080/lab6-framework" \
    -r htmlextra,json,cli \
    --reporter-htmlextra-export reports/framework-html-report.html \
    --reporter-json-export reports/framework-json-report.json \
    --delay-request 100

FRAMEWORK_EXIT_CODE=$?

# Generate comparison report
echo -e "${YELLOW}📊 Generating comparison report...${NC}"

cat > reports/comparison.md << EOF
# API Tests Comparison Report

## Manual Implementation
- Exit Code: $MANUAL_EXIT_CODE
- Report: [manual-html-report.html](manual-html-report.html)

## Framework Implementation
- Exit Code: $FRAMEWORK_EXIT_CODE
- Report: [framework-html-report.html](framework-html-report.html)

## Test Results
$(if [ $MANUAL_EXIT_CODE -eq 0 ] && [ $FRAMEWORK_EXIT_CODE -eq 0 ]; then
  echo "✅ Both implementations passed all tests"
elif [ $MANUAL_EXIT_CODE -eq 0 ]; then
  echo "✅ Manual passed, ❌ Framework failed"
elif [ $FRAMEWORK_EXIT_CODE -eq 0 ]; then
  echo "❌ Manual failed, ✅ Framework passed"
else
  echo "❌ Both implementations have failures"
fi)

Generated: $(date)
EOF

echo -e "${GREEN}📄 Reports generated in newman-tests/reports/ folder${NC}"
ls -la reports/