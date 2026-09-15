$ErrorActionPreference = "Stop"
$raiz = $PSScriptRoot
$saida = Join-Path $raiz "out"

if (Test-Path $saida) { Remove-Item -Recurse -Force $saida }
New-Item -ItemType Directory -Force $saida | Out-Null

$fontes = Get-ChildItem -Recurse -Filter *.java (Join-Path $raiz "src") | ForEach-Object { $_.FullName }
& javac -encoding UTF-8 -d $saida $fontes
if ($LASTEXITCODE -ne 0) { throw "Falha na compilacao." }

Write-Host "Compilado em $saida"
