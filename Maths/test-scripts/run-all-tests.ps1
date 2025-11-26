param([string]$BaseUrl = "http://localhost:8080/lab6")

Write-Host "=== Lab6 Complete Test Suite ===" -ForegroundColor Yellow
Write-Host "Base URL: $BaseUrl" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

Write-Host "`n--- AUTHENTICATION TESTS ---" -ForegroundColor Magenta
& "$PSScriptRoot\run-auth-tests.ps1" -BaseUrl $BaseUrl
$authCode = $LASTEXITCODE

Write-Host "`n--- API FUNCTIONALITY TESTS ---" -ForegroundColor Magenta
& "$PSScriptRoot\run-api-tests.ps1" -BaseUrl $BaseUrl
$apiCode = $LASTEXITCODE

Write-Host "`n=== FINAL RESULTS ===" -ForegroundColor Yellow
Write-Host "=================================" -ForegroundColor Cyan

if ($authCode -eq 0 -and $apiCode -eq 0) {
    Write-Host "ALL TESTS PASSED! ?" -ForegroundColor Green
    exit 0
} else {
    Write-Host "SOME TESTS FAILED! ?" -ForegroundColor Red
    Write-Host "Auth Tests: $(if ($authCode -eq 0) { 'PASS' } else { 'FAIL' })" -ForegroundColor $(if ($authCode -eq 0) { 'Green' } else { 'Red' })
    Write-Host "API Tests: $(if ($apiCode -eq 0) { 'PASS' } else { 'FAIL' })" -ForegroundColor $(if ($apiCode -eq 0) { 'Green' } else { 'Red' })
    exit 1
}