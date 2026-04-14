@echo off
echo ========================================
echo   Tower Defense - Compilazione e Avvio
echo ========================================
echo.

REM Create build directories
if not exist build\classes mkdir build\classes

REM Copy resources
xcopy /s /y /q src\main\resources\* build\classes\ >nul 2>&1

echo Compilazione in corso...
javac -d build/classes src/main/java/it/unibo/towerdefense/commons/*.java src/main/java/it/unibo/towerdefense/model/*.java src/main/java/it/unibo/towerdefense/view/*.java src/main/java/it/unibo/towerdefense/controller/*.java src/main/java/it/unibo/towerdefense/application/*.java
if %errorlevel% neq 0 (
    echo.
    echo ERRORE durante la compilazione!
    pause
    exit /b 1
)

echo Compilazione completata con successo!
echo Avvio del gioco...
echo.
java -cp build/classes it.unibo.towerdefense.application.TowerDefense
pause