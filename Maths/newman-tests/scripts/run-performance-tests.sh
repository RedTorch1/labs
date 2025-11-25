#!/bin/bash

echo "📊 Running Performance Comparison Tests"

cd "$(dirname "$0")/.."

mkdir -p reports/performance

echo "Testing Manual implementation..."
newman run collections/lab6-api-tests.postman_collection.json \
    -e environments/lab6-env.postman_environment.json \
    --env-var "baseUrl=http://localhost:8080/lab6-manual" \
    -n 3 \
    --reporters json \
    --reporter-json-export reports/performance/manual-performance.json

echo "Testing Framework implementation..."
newman run collections/lab6-api-tests.postman_collection.json \
    -e environments/lab6-env.postman_environment.json \
    --env-var "baseUrl=http://localhost:8080/lab6-framework" \
    -n 3 \
    --reporters json \
    --reporter-json-export reports/performance/framework-performance.json

echo "📈 Performance tests completed"
echo "📊 Reports: reports/performance/"