package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.StatusTaskDAO;
import com.gestor.model.StatusTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusTaskDAOImpl implements StatusTaskDAO {

    private StatusTask mapRow(ResultSet rs) throws SQLException {
        StatusTask st = new StatusTask();
        st.setIdStatusTask(rs.getInt("id_status_task"));
        st.setStatusName(rs.getString("status_name"));
        st.setStatusOrder(rs.getInt("status_order"));
        return st;
    }

    @Override
    public StatusTask findById(int id) throws Exception {
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
        String sql = "DELETE FROM status_task WHERE id_status_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public StatusTask findByName(String name) throws Exception {
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
