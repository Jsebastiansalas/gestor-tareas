@echo off
title Compilando y Ejecutando Gestor de Tareas
cd /d "%~dp0"

echo === Compilando proyecto ===
if exist target\classes rmdir /s /q target\classes
mkdir target\classes

javac --release 21 -cp "lib\mysql-connector-j-8.2.0.jar;lib\LGoodDatePicker-11.2.1.jar" -d target\classes -sourcepath src\main\java src\main\java\com\gestor\Main.java src\main\java\com\gestor\model\*.java src\main\java\com\gestor\dao\*.java src\main\java\com\gestor\dao\impl\*.java src\main\java\com\gestor\service\*.java src\main\java\com\gestor\gui\*.java

if errorlevel 1 (
    echo.
    echo ERROR: Falló la compilacion.
    pause
    exit /b 1
)

echo === Compilacion exitosa ===
echo === Iniciando aplicacion ===
echo.
java -cp "target\classes;lib\mysql-connector-j-8.2.0.jar;lib\LGoodDatePicker-11.2.1.jar" com.gestor.Main

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo iniciar la aplicacion.
    pause
)
