#!/usr/bin/env bash
set -e
rm -rf bin
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d bin
echo "Compilação concluída com sucesso."
