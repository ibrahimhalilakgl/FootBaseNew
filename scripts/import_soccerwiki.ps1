# Usage:
#   powershell -ExecutionPolicy Bypass -File scripts/import_soccerwiki.ps1 -JsonPath "C:\path\SoccerWiki_2025-12-10_1765357154.json" -DbUrl "jdbc:postgresql://localhost:5432/footbasenew" -DbUser "postgres" -DbPass "12345"
#
# Gerekenler: psql (PATH içinde) ve yazma izni

param(
  [Parameter(Mandatory=$true)][string]$JsonPath,
  [Parameter(Mandatory=$true)][string]$DbUrl,
  [Parameter(Mandatory=$true)][string]$DbUser,
  [Parameter(Mandatory=$true)][string]$DbPass
)

if (!(Test-Path $JsonPath)) {
  Write-Error "JSON bulunamadı: $JsonPath"
  exit 1
}

$ErrorActionPreference = "Stop"

Write-Host "JSON okunuyor..." -ForegroundColor Cyan
$json = Get-Content -Raw $JsonPath | ConvertFrom-Json

# Geçici CSV dosyaları
$clubCsv = [System.IO.Path]::GetTempFileName() + "_clubs.csv"
$playerCsv = [System.IO.Path]::GetTempFileName() + "_players.csv"

Write-Host "Kulüp CSV yazılıyor: $clubCsv" -ForegroundColor Cyan
$clubLines = @()
$clubLines += "external_id,name,short_name,logo_url"
foreach ($c in $json.ClubData) {
  $id = $c.ID
  $name = '"' + ($c.Name -replace '"','""') + '"'
  $short = '"' + ($c.ShortName -replace '"','""') + '"'
  $logo = '"' + ($c.ImageURL -replace '"','""') + '"'
  $clubLines += "$id,$name,$short,$logo"
}
Set-Content -Path $clubCsv -Value $clubLines -Encoding UTF8

Write-Host "Oyuncu CSV yazılıyor: $playerCsv" -ForegroundColor Cyan
$playerLines = @()
$playerLines += "external_id,full_name,image_url"
foreach ($p in $json.PlayerData) {
  $id = $p.ID
  $full = '"' + (($p.Forename + " " + $p.Surname) -replace '"','""') + '"'
  $img = '"' + ($p.ImageURL -replace '"','""') + '"'
  $playerLines += "$id,$full,$img"
}
Set-Content -Path $playerCsv -Value $playerLines -Encoding UTF8

Write-Host "Staging tablolarına COPY yapılıyor..." -ForegroundColor Cyan
$env:PGPASSWORD = $DbPass

psql -d $DbUrl -U $DbUser -c "TRUNCATE club_staging RESTART IDENTITY; TRUNCATE player_staging RESTART IDENTITY;" 
psql -d $DbUrl -U $DbUser -c "\copy club_staging (external_id,name,short_name,logo_url) FROM '$clubCsv' WITH (FORMAT csv, HEADER true)" 
psql -d $DbUrl -U $DbUser -c "\copy player_staging (external_id,full_name,image_url) FROM '$playerCsv' WITH (FORMAT csv, HEADER true)" 

Write-Host "Staging doldu. Uygulamayı çalıştırdığınızda Flyway V15 upsert işlemini yapacak." -ForegroundColor Green

Remove-Item $clubCsv, $playerCsv -ErrorAction SilentlyContinue
Write-Host "Bitti." -ForegroundColor Green

