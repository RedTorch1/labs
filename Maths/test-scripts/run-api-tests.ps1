param([string]$BaseUrl = "http://localhost:8080/lab6")

Write-Host "=== Lab6 API Tests ===" -ForegroundColor Yellow
Write-Host "Base URL: $BaseUrl" -ForegroundColor Cyan

function Get-BasicAuthHeader {
    param([string]$username, [string]$password)
    $bytes = [System.Text.Encoding]::UTF8.GetBytes("${username}:${password}")
    $base64 = [Convert]::ToBase64String($bytes)
    return @{"Authorization" = "Basic $base64"}
}

$results = @()
$allTestsPassed = $true
$userHeaders = Get-BasicAuthHeader "user1" "user123"

Write-Host "`n1. Testing Users API..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Get all users" -ForegroundColor Green
    $results += @{Test="Get Users"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Get Users"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n2. Testing Functions API..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/functions" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Get all functions" -ForegroundColor Green
    $results += @{Test="Get Functions"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Get Functions"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n3. Testing Points API..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/points?functionId=1" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Get points" -ForegroundColor Green
    $results += @{Test="Get Points"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Get Points"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n4. Testing User by ID..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users/1" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Get user by ID" -ForegroundColor Green
    $results += @{Test="Get User by ID"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Get User by ID"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n5. Testing Function by ID..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/functions/1" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Get function by ID" -ForegroundColor Green
    $results += @{Test="Get Function by ID"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Get Function by ID"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n=== SUMMARY ===" -ForegroundColor Yellow
$passed = ($results | Where-Object { $_.Success -eq $true }).Count
$failed = ($results | Where-Object { $_.Success -eq $false }).Count
$total = $results.Count

Write-Host "PASSED: $passed" -ForegroundColor Green
Write-Host "FAILED: $failed" -ForegroundColor Red

if ($total -gt 0) {
    $rate = [math]::Round($passed / $total * 100, 2)
    Write-Host "SUCCESS RATE: $rate%" -ForegroundColor Cyan
}

# Возвращаем корректный код выхода
if ($allTestsPassed) {
    Write-Host "`nAll API tests PASSED! ?" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`nSome API tests FAILED! ?" -ForegroundColor Red
    exit 1
}