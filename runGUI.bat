@echo off
echo ================================================
echo     GESTION DES ABSENCES - Interface Graphique
echo ================================================
echo.

REM Vérification de la compilation
if not exist "out\gui\LoginFrame.class" (
    echo [ERREUR] L'application n'est pas compilee.
    echo Veuillez executer compile.bat d'abord.
    pause
    exit /b 1
)

REM Vérification du driver MySQL
if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo [ERREUR] Le driver MySQL n'est pas present.
    pause
    exit /b 1
)

echo Demarrage de l'application graphique...
echo.

java -cp "out;lib\mysql-connector-j-8.0.33.jar" gui.LoginFrame

pause
