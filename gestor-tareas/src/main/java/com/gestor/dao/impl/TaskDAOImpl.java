package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.TaskDAO;
import com.gestor.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    private static final String BASE_QUERY =
            "SELECT t.id_task, t.title, t.description, "
            + "t.id_status_task, st.status_name, st.status_order, "
            + "t.id_team, tm.team_name, tm.description AS team_description, "
            + "t.created_by, p.first_name, p.last_name, p.email, "
            + "p.id_type_person, typ.type_name, "
            + "t.created_at, t.updated_at "
            + "FROM task t "
            + "INNER JOIN status_task st ON t.id_status_task = st.id_status_task "
            + "INNER JOIN team tm ON t.id_team = tm.id_team "
            + "INNER JOIN person p ON t.created_by = p.id_person "
            + "INNER JOIN type_person typ ON p.id_type_person = typ.id_type_person ";

    private Task mapRow(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setIdTask(rs.getInt("id_task"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));

        StatusTask statusTask = new StatusTask();
        statusTask.setIdStatusTask(rs.getInt("id_status_task"));
        statusTask.setStatusName(rs.getString("status_name"));
        statusTask.setStatusOrder(rs.getInt("status_order"));
        task.setStatusTask(statusTask);

        Team team = new Team();
        team.setIdTeam(rs.getInt("id_team"));
        team.setTeamName(rs.getString("team_name"));
        team.setDescription(rs.getString("team_description"));
        task.setTeam(team);

        Person createdBy = new Person();
        createdBy.setIdPerson(rs.getInt("created_by"));
        createdBy.setFirstName(rs.getString("first_name"));
        createdBy.setLastName(rs.getString("last_name"));
        createdBy.setEmail(rs.getString("email"));

        TypePerson typePerson = new TypePerson();
        typePerson.setIdTypePerson(rs.getInt("id_type_person"));
        typePerson.setTypeName(rs.getString("type_name"));
        createdBy.setTypePerson(typePerson);

        task.setCreatedBy(createdBy);

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return task;
    }

    @Override
    public Task findById(int id) throws Exception {
        String sql = BASE_QUERY + "WHERE t.id_task = ?";
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
    public List<Task> findAll() throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = BASE_QUERY + "ORDER BY t.created_at DESC";
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
    public boolean save(Task entity) throws Exception {
        String sql = "INSERT INTO task (title, description, id_status_task, id_team, created_by, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getStatusTask().getIdStatusTask());
            ps.setInt(4, entity.getTeam().getIdTeam());
            ps.setInt(5, entity.getCreatedBy().getIdPerson());
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : now));
            ps.setTimestamp(7, Timestamp.valueOf(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : now));
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Task entity) throws Exception {
        String sql = "UPDATE task SET title = ?, description = ?, id_status_task = ?, id_team = ?, "
                + "created_by = ?, created_at = ?, updated_at = ? WHERE id_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getStatusTask().getIdStatusTask());
            ps.setInt(4, entity.getTeam().getIdTeam());
            ps.setInt(5, entity.getCreatedBy().getIdPerson());
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : now));
            ps.setTimestamp(7, Timestamp.valueOf(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : now));
            ps.setInt(8, entity.getIdTask());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM task WHERE id_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Task> findByTeam(int idTeam) throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE t.id_team = ? ORDER BY t.created_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTeam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Task> findByStatus(int idStatusTask) throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE t.id_status_task = ? ORDER BY t.created_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idStatusTask);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Task> findByPerson(int idPerson) throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE t.created_by = ? ORDER BY t.created_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerson);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Task> findByTeamAndStatus(int idTeam, int idStatusTask) throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE t.id_team = ? AND t.id_status_task = ? ORDER BY t.created_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTeam);
            ps.setInt(2, idStatusTask);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int idTask, int idStatusTask) throws Exception {
        String sql = "UPDATE task SET id_status_task = ?, updated_at = CURRENT_TIMESTAMP WHERE id_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idStatusTask);
            ps.setInt(2, idTask);
            return ps.executeUpdate() > 0;
        }
    }
}
