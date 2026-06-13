@echo off
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > sources.txt
javac --release 17 -encoding UTF-8 -d bin @sources.txt
if errorlevel 1 exit /b 1
del sources.txt
echo Compilacao concluida com sucesso.
