@echo off
echo ========================================
echo   TowerSiege - Build e Avvio via JAR
echo ========================================
echo.

echo Costruzione del JAR in corso (Gradle shadowJar)...
call gradlew.bat shadowJar
if %errorlevel% neq 0 (
    echo.
    echo ERRORE durante la build Gradle!
    pause
    exit /b 1
)

echo.
echo Build completata con successo!
echo Avvio del gioco...
echo.

for /f "delims=" %%f in ('dir /b /s build\libs\*-all.jar 2^>nul') do set JAR=%%f
if not defined JAR (
    echo ERRORE: JAR non trovato in build\libs\
    pause
    exit /b 1
)

java -jar "%JAR%"
pause