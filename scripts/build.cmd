@echo off
REM Build ProjectLibre on Windows. Requires a JDK (11+, 21 recommended) and Apache Ant on PATH.
REM Usage: scripts\build.cmd [ant targets]   (default: dist)
setlocal
cd /d "%~dp0\..\openproj_build"
if "%~1"=="" (ant dist) else (ant %*)
endlocal
