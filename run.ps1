$ErrorActionPreference = "Stop"
$raiz = $PSScriptRoot
$saida = Join-Path $raiz "out"

if (-not (Test-Path (Join-Path $saida "me\dio\banco\Main.class"))) {
    & (Join-Path $raiz "build.ps1")
}

chcp 65001 > $null
& java "-Dfile.encoding=UTF-8" -cp $saida me.dio.banco.Main @args
