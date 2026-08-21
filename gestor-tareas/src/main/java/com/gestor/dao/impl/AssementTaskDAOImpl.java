package com.gestor.dao.impl;

import com.gestor.dao.AssementTaskDAO;
import com.gestor.dao.ConexionDB;
import com.gestor.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssementTaskDAOImpl implements AssementTaskDAO {

    private static final String BASE_QUERY =
            "SELECT at.id_assement_task, at.role_in_task, at.assigned_at, "
            + "t.id_task, t.title, t.description, "
            + "t.id_status_task, st.status_name, st.status_order, "
            + "t.id_team, tm.team_name, tm.description AS team_description, "
            + "t.id_created_by, p_creator.first_name AS creator_first_name, p_creator.last_name AS creator_last_name, "
            + "p_creator.email AS creator_email, "
            + "p_creator.id_type_person AS creator_id_type_person, typ_creator.type_name AS creator_type_name, "
            + "t.created_at, t.updated_at, "
            + "p.id_person, p.first_name, p.last_name, p.email, "
            + "p.id_type_person, typ.type_name "
            + "FROM assement_task at "
            + "INNER JOIN task t ON at.id_task = t.id_task "
            + "INNER JOIN status_task st ON t.id_status_task = st.id_status_task "
            + "INNER JOIN team tm ON t.id_team = tm.id_team "
            + "INNER JOIN person p_creator ON t.id_created_by = p_creator.id_person "
            + "INNER JOIN type_person typ_creator ON p_creator.id_type_person = typ_creator.id_type_person "
            + "INNER JOIN person p ON at.id_person = p.id_person "
            + "INNER JOIN type_person typ ON p.id_type_person = typ.id_type_person ";

    private AssementTask mapRow(ResultSet rs) throws SQLException {
        AssementTask at = new AssementTask();
        at.setIdAssementTask(rs.getInt("id_assement_task"));
        at.setRoleInTask(rs.getString("role_in_task"));

        Timestamp assignedAt = rs.getTimestamp("assigned_at");
        if (assignedAt != null) {
            at.setAssignedAt(assignedAt.toLocalDateTime());
        }

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
        createdBy.setIdPerson(rs.getInt("id_created_by"));
        createdBy.setFirstName(rs.getString("creator_first_name"));
        createdBy.setLastName(rs.getString("creator_last_name"));
        createdBy.setEmail(rs.getString("creator_email"));

        TypePerson typePersonCreator = new TypePerson();
        typePersonCreator.setIdTypePerson(rs.getInt("creator_id_type_person"));
        typePersonCreator.setTypeName(rs.getString("creator_type_name"));
        createdBy.setTypePerson(typePersonCreator);

        task.setCreatedBy(createdBy);

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        at.setTask(task);

        Person person = new Person();
        person.setIdPerson(rs.getInt("id_person"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmail(rs.getString("email"));

        TypePerson typePerson = new TypePerson();
        typePerson.setIdTypePerson(rs.getInt("id_type_person"));
        typePerson.setTypeName(rs.getString("type_name"));
        person.setTypePerson(typePerson);

        at.setPerson(person);

        return at;
    }

    @Override
    public List<AssementTask> findByTask(int idTask) throws Exception {
        List<AssementTask> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE at.id_task = ? ORDER BY at.assigned_at DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTask);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<AssementTask> findByPerson(int idPerson) throws Exception {
        List<AssementTask> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE at.id_person = ? ORDER BY at.assigned_at DESC";
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
    public boolean save(AssementTask entity) throws Exception {
        String sql = "INSERT INTO assement_task (id_task, id_person, role_in_task, assigned_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getTask().getIdTask());
            ps.setInt(2, entity.getPerson().getIdPerson());
            ps.setString(3, entity.getRoleInTask());
            ps.setTimestamp(4, entity.getAssignedAt() != null ? Timestamp.valueOf(entity.getAssignedAt()) : null);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int idAssementTask) throws Exception {
        String sql = "DELETE FROM assement_task WHERE id_assement_task = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAssementTask);
            return ps.executeUpdate() > 0;
        }
    }
}
