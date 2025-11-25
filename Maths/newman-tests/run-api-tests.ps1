# API Test Runner for Lab6
Write-Host "🚀 Starting Lab6 API Tests..." -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Cyan

$baseUrl = "http://localhost:8080/lab6"
$testResults = @()

# Test Cases
$testCases = @(
    @{
        Name = "Users API - Get All"
        Method = "GET"
        Url = "/api/users"
        ExpectedStatus = 200
    },
    @{
        Name = "Users API - Get by ID"
        Method = "GET" 
        Url = "/api/users/1"
        ExpectedStatus = 200
    },
    @{
        Name = "Functions API - Get All"
        Method = "GET"
        Url = "/api/functions"
        ExpectedStatus = 200
    },
    @{
        Name = "Functions API - Get by ID"
        Method = "GET"
        Url = "/api/functions/1"
        ExpectedStatus = 200
    },
    @{
        Name = "Points API - Get by Function"
        Method = "GET"
        Url = "/api/points?functionId=1"
        ExpectedStatus = 200
    },
    @{
        Name = "Create User"
        Method = "POST"
        Url = "/api/users"
        Body = '{"username": "testuser", "passwordHash": "test123", "email": "test@test.com"}'
        ExpectedStatus = 200
    },
    @{
        Name = "Create Function"
        Method = "POST"
        Url = "/api/functions" 
        Body = '{"name": "Test Function", "expression": "x^2", "userId": 1}'
        ExpectedStatus = 200
    },
    @{
        Name = "Create Point"
        Method = "POST"
        Url = "/api/points"
        Body = '{"functionId": 1, "xValue": 5, "yValue": 25}'
        ExpectedStatus = 200
    }
)

foreach ($test in $testCases) {
    Write-Host "`nTesting: $($test.Name)" -ForegroundColor White
    Write-Host "Endpoint: $($test.Method) $($test.Url)" -ForegroundColor Gray
    
    try {
        $headers = @{"Content-Type" = "application/json"}
        
        if ($test.Method -eq "GET") {
            $response = Invoke-WebRequest -Uri ($baseUrl + $test.Url) -Method GET -Headers $headers -UseBasicParsing
        } else {
            $response = Invoke-WebRequest -Uri ($baseUrl + $test.Url) -Method POST -Headers $headers -Body $test.Body -UseBasicParsing
        }
        
        $statusMatch = $response.StatusCode -eq $test.ExpectedStatus
        $isJson = $response.Content -match '^\s*\{'
        
        if ($statusMatch -and $isJson) {
            Write-Host "✅ PASS - Status: $($response.StatusCode), JSON: Yes" -ForegroundColor Green
            $testResults += @{Test = $test.Name; Result = "PASS"; Status = $response.StatusCode}
        } else {
            Write-Host "⚠️  PARTIAL - Status: $($response.StatusCode) (expected $($test.ExpectedStatus)), JSON: $isJson" -ForegroundColor Yellow
            $testResults += @{Test = $test.Name; Result = "PARTIAL"; Status = $response.StatusCode}
        }
        
    } catch {
        Write-Host "❌ FAIL - Error: $($_.Exception.Message)" -ForegroundColor Red
        $testResults += @{Test = $test.Name; Result = "FAIL"; Status = "Error"}
    }
    
    # Небольшая задержка между запросами
    Start-Sleep -Milliseconds 100
}

# Summary
Write-Host "`n" + "="*50 -ForegroundColor Cyan
Write-Host "📊 TEST SUMMARY" -ForegroundColor Yellow
Write-Host "="*50 -ForegroundColor Cyan

$passed = ($testResults | Where-Object { $_.Result -eq "PASS" }).Count
$partial = ($testResults | Where-Object { $_.Result -eq "PARTIAL" }).Count  
$failed = ($testResults | Where-Object { $_.Result -eq "FAIL" }).Count

Write-Host "✅ PASSED: $passed" -ForegroundColor Green
Write-Host "⚠️  PARTIAL: $partial" -ForegroundColor Yellow  
Write-Host "❌ FAILED: $failed" -ForegroundColor Red
Write-Host "📈 SUCCESS RATE: $([math]::Round(($passed + $partial/2) / $testResults.Count * 100, 2))%" -ForegroundColor Cyan

Write-Host "`n🎯 Testing completed!" -ForegroundColor Yellow
