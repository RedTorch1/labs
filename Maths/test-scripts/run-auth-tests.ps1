param([string]$BaseUrl = "http://localhost:8080/lab6")

Write-Host "=== Lab6 Auth Tests ===" -ForegroundColor Yellow
Write-Host "Base URL: $BaseUrl" -ForegroundColor Cyan

function Get-BasicAuthHeader {
    param([string]$username, [string]$password)
    $bytes = [System.Text.Encoding]::UTF8.GetBytes("${username}:${password}")
    $base64 = [Convert]::ToBase64String($bytes)
    return @{"Authorization" = "Basic $base64"}
}

$results = @()
$allTestsPassed = $true

Write-Host "`n1. Testing Registration (JSON)..." -ForegroundColor White
try {
    # Используем уникальное имя пользователя для каждого теста
    $timestamp = [DateTime]::Now.ToString("yyyyMMddHHmmss")
    $testUsername = "testuser$timestamp"

    $body = @{
        username = $testUsername
        password = "testpass123"
        role = "USER"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "$BaseUrl/api/auth/register" -Method POST -Body $body -ContentType "application/json" -UseBasicParsing
    Write-Host "   PASS - Status: $($response.StatusCode)" -ForegroundColor Green
    $results += @{Test="Registration"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="Registration"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n2. Testing Unauthorized Access..." -ForegroundColor White
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users" -UseBasicParsing
    Write-Host "   FAIL - Should require auth" -ForegroundColor Red
    $results += @{Test="Unauthorized"; Success=$false; Status=$response.StatusCode}
    $allTestsPassed = $false
} catch {
    if ($_.Exception.Response.StatusCode -eq 401) {
        Write-Host "   PASS - Correctly requires auth" -ForegroundColor Green
        $results += @{Test="Unauthorized"; Success=$true; Status=401}
    } else {
        Write-Host "   FAIL - Wrong status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        $results += @{Test="Unauthorized"; Success=$false; Status=$_.Exception.Response.StatusCode}
        $allTestsPassed = $false
    }
}

Write-Host "`n3. Testing VIEWER Role..." -ForegroundColor White
$viewerHeaders = Get-BasicAuthHeader "viewer" "viewer123"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users" -Headers $viewerHeaders -UseBasicParsing
    Write-Host "   PASS - Can read users" -ForegroundColor Green
    $results += @{Test="VIEWER Read"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - Cannot read: $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="VIEWER Read"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n4. Testing USER Role..." -ForegroundColor White
$userHeaders = Get-BasicAuthHeader "user1" "user123"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users" -Headers $userHeaders -UseBasicParsing
    Write-Host "   PASS - Can read users" -ForegroundColor Green
    $results += @{Test="USER Read"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - Cannot read: $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="USER Read"; Success=$false; Status=$_.Exception.Response.StatusCode}
    $allTestsPassed = $false
}

Write-Host "`n5. Testing ADMIN Role..." -ForegroundColor White
$adminHeaders = Get-BasicAuthHeader "admin" "admin123"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/users" -Headers $adminHeaders -UseBasicParsing
    Write-Host "   PASS - Can read users" -ForegroundColor Green
    $results += @{Test="ADMIN Read"; Success=$true; Status=$response.StatusCode}
} catch {
    Write-Host "   FAIL - Cannot read: $($_.Exception.Message)" -ForegroundColor Red
    $results += @{Test="ADMIN Read"; Success=$false; Status=$_.Exception.Response.StatusCode}
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
    Write-Host "`nAll auth tests PASSED! ?" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`nSome auth tests FAILED! ?" -ForegroundColor Red
    exit 1
}