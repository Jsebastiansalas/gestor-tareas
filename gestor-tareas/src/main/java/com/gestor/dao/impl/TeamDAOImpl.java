package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.TeamDAO;
import com.gestor.model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de {@link com.gestor.dao.TeamDAO}.
 * Realiza operaciones CRUD simples sobre la tabla team,
 * sin necesidad de JOINs con otras tablas.
 */
public class TeamDAOImpl implements TeamDAO {

    /** Mapea una fila del ResultSet a un objeto Team. */
    private Team mapRow(ResultSet rs) throws SQLException {
        Team t = new Team();
        t.setIdTeam(rs.getInt("id_team"));
        t.setTeamName(rs.getString("team_name"));
        t.setDescription(rs.getString("description"));
        return t;
    }

    @Override
    public Team findById(int id) throws Exception {
        // Consulta por clave primaria
        String sql = "SELECT id_team, team_name, description FROM team WHERE id_team = ?";
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
    public List<Team> findAll() throws Exception {
        List<Team> list = new ArrayList<>();
        // Consulta de todos los equipos
        String sql = "SELECT id_team, team_name, description FROM team";
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
    public boolean save(Team entity) throws Exception {
        // Inserción de un nuevo equipo
        String sql = "INSERT INTO team (team_name, description) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTeamName());
            ps.setString(2, entity.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Team entity) throws Exception {
        // Actualización de nombre y descripción según la clave primaria
        String sql = "UPDATE team SET team_name = ?, description = ? WHERE id_team = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTeamName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getIdTeam());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Eliminación por clave primaria
        String sql = "DELETE FROM team WHERE id_team = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
