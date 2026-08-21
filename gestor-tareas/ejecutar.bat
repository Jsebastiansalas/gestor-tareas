@echo off
title Gestor de Tareas - Scrum
java -cp "target\classes;lib\mysql-connector-j-8.2.0.jar;lib\LGoodDatePicker-11.2.1.jar" com.gestor.Main
if errorlevel 1 (
    echo.
    echo ERROR: No se pudo iniciar la aplicacion.
    echo Verifique que Java este instalado y que la BD exista.
    pause
)
