try {
    $response = Invoke-RestMethod -Method Post -Uri "http://localhost:8082/api/booking" -ContentType "application/json" -Body '{"flightId": 101, "passengerId": 202, "seatSelection": "12A"}' -ErrorAction Stop
    $response
} catch {
    Write-Host "Status Code:" $_.Exception.Response.StatusCode.value__
    $stream = $_.Exception.Response.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $body = $reader.ReadToEnd()
    Write-Host "Error Body:" $body
}
