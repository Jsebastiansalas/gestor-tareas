package com.gestor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexiones JDBC hacia la base de datos MySQL "gestor_tareas".
 * Forma parte de la capa DAO: todas las implementaciones obtienen aquí
 * su conexión en lugar de crearlas manualmente.
 */
public class ConexionDB {
    // Datos de conexión a la base de datos (URL JDBC, usuario y contraseña)
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/gestor_tareas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "sebastian1129.,";

    // Bloque estático: se ejecuta una sola vez al cargar la clase para registrar el driver MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado", e);
        }
    }

    /**
     * Entrega una nueva conexión JDBC usando las credenciales configuradas.
     * El llamador es responsable de cerrarla (idealmente con try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
