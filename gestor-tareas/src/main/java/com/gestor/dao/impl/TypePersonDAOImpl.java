package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.TypePersonDAO;
import com.gestor.model.TypePerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de {@link com.gestor.dao.TypePersonDAO}.
 * Realiza operaciones CRUD simples sobre la tabla type_person,
 * sin necesidad de JOINs con otras tablas.
 */
public class TypePersonDAOImpl implements TypePersonDAO {

    /** Mapea una fila del ResultSet a un objeto TypePerson. */
    private TypePerson mapRow(ResultSet rs) throws SQLException {
        TypePerson tp = new TypePerson();
        tp.setIdTypePerson(rs.getInt("id_type_person"));
        tp.setTypeName(rs.getString("type_name"));
        return tp;
    }

    @Override
    public TypePerson findById(int id) throws Exception {
        // Consulta por clave primaria
        String sql = "SELECT id_type_person, type_name FROM type_person WHERE id_type_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<TypePerson> findAll() throws Exception {
        List<TypePerson> list = new ArrayList<>();
        // Consulta de todos los tipos de persona
        String sql = "SELECT id_type_person, type_name FROM type_person";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public boolean save(TypePerson entity) throws Exception {
        // Inserción de un nuevo tipo de persona
        String sql = "INSERT INTO type_person (type_name) VALUES (?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTypeName());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(TypePerson entity) throws Exception {
        // Actualización del nombre según la clave primaria
        String sql = "UPDATE type_person SET type_name = ? WHERE id_type_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTypeName());
            ps.setInt(2, entity.getIdTypePerson());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Eliminación por clave primaria
        String sql = "DELETE FROM type_person WHERE id_type_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public TypePerson findByName(String name) throws Exception {
        // Búsqueda por nombre exacto del tipo de persona
        String sql = "SELECT id_type_person, type_name FROM type_person WHERE type_name = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }
}
