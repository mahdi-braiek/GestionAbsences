@echo off
echo ================================================
echo     COMPILATION DE L'APPLICATION GUI
echo ================================================
echo.

REM Vérification du répertoire
if not exist "src" (
    echo [ERREUR] Le répertoire src n'existe pas.
    pause
    exit /b 1
)

if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo [ERREUR] Le driver MySQL n'est pas presente dans lib\
    echo Veuillez telecharger mysql-connector-j-8.0.33.jar
    pause
    exit /b 1
)

REM Création du répertoire de sortie
if not exist "out" mkdir out

echo [1/2] Compilation des fichiers Java...
javac -encoding UTF-8 -d out -cp "lib\mysql-connector-j-8.0.33.jar" src\model\*.java src\util\*.java src\dao\*.java src\gui\*.java src\main\*.java

if %ERRORLEVEL% EQU 0 (
    echo [OK] Compilation reussie!
    echo.
    echo ================================================
    echo Pour lancer l'application graphique:
    echo   runGUI.bat
    echo.
    echo Pour lancer l'application console:
    echo   run.bat
    echo ================================================
) else (
    echo [ERREUR] Echec de la compilation.
)

pause
