package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.StatusTaskDAO;
import com.gestor.model.StatusTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de {@link com.gestor.dao.StatusTaskDAO}.
 * Realiza operaciones CRUD sobre la tabla status_task y agrega
 * findAllOrdered, que ordena los estados según status_order para
 * respetar el flujo de estados de las tareas.
 */
public class StatusTaskDAOImpl implements StatusTaskDAO {

    /** Mapea una fila del ResultSet a un objeto StatusTask. */
    private StatusTask mapRow(ResultSet rs) throws SQLException {
        StatusTask st = new StatusTask();
        st.setIdStatusTask(rs.getInt("id_status_task"));
        st.setStatusName(rs.getString("status_name"));
        st.setStatusOrder(rs.getInt("status_order"));
        return st;
    }

    @Override
    public StatusTask findById(int id) throws Exception {
        // Consulta por clave primaria
        String sql = "SELECT id_status_task, status_name, status_order FROM status_task WHERE id_status_task = ?";
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
    public List<StatusTask> findAll() throws Exception {
        List<StatusTask> list = new ArrayList<>();
        // Consulta de todos los estados sin orden específico
        String sql = "SELECT id_status_task, status_name, status_order FROM status_task";
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
    public boolean save(StatusTask entity) throws Exception {
        // Inserción de un nuevo estado de tarea
        String sql = "INSERT INTO status_task (status_name, status_order) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getStatusName());
            ps.setInt(2, entity.getStatusOrder());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(StatusTask entity) throws Exception {
        // Actualización de nombre y orden según la clave primaria
        String sql = "UPDATE status_task SET status_name = ?, status_order = ? WHERE id_status_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getStatusName());
            ps.setInt(2, entity.getStatusOrder());
            ps.setInt(3, entity.getIdStatusTask());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Eliminación por clave primaria
        String sql = "DELETE FROM status_task WHERE id_status_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public StatusTask findByName(String name) throws Exception {
        // Búsqueda por nombre exacto del estado
        String sql = "SELECT id_status_task, status_name, status_order FROM status_task WHERE status_name = ?";
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

    @Override
    public List<StatusTask> findAllOrdered() throws Exception {
        List<StatusTask> list = new ArrayList<>();
        // Ordena los estados según su posición en el flujo de tareas
        String sql = "SELECT id_status_task, status_name, status_order FROM status_task ORDER BY status_order ASC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
}
