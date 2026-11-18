@echo off
REM ============================================
REM Script d'execution du projet
REM ============================================

echo.
echo ========================================
echo    GESTION DES ABSENCES
echo ========================================
echo.

REM Vérifier que le projet est compilé
if not exist "out" (
    echo [ERREUR] Le projet n'est pas compile.
    echo Executez d'abord: compile.bat
    pause
    exit /b 1
)

REM Exécuter l'application
java -cp "out;lib/*" main.Application

echo.
pause
