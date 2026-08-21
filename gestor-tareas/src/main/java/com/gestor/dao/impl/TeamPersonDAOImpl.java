package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.TeamPersonDAO;
import com.gestor.model.Person;
import com.gestor.model.Team;
import com.gestor.model.TeamPerson;
import com.gestor.model.TypePerson;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TeamPersonDAOImpl implements TeamPersonDAO {

    private TeamPerson mapRow(ResultSet rs) throws SQLException {
        TeamPerson tp = new TeamPerson();
        tp.setIdTeamPerson(rs.getInt("id_team_person"));

        Team team = new Team();
        team.setIdTeam(rs.getInt("id_team"));
        team.setTeamName(rs.getString("team_name"));
        team.setDescription(rs.getString("team_description"));
        tp.setTeam(team);

        Person person = new Person();
        person.setIdPerson(rs.getInt("id_person"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmail(rs.getString("email"));

        TypePerson typePerson = new TypePerson();
        typePerson.setIdTypePerson(rs.getInt("id_type_person"));
        typePerson.setTypeName(rs.getString("type_name"));
        person.setTypePerson(typePerson);

        tp.setPerson(person);

        Timestamp ts = rs.getTimestamp("joined_at");
        if (ts != null) {
            tp.setJoinedAt(ts.toLocalDateTime());
        }

        return tp;
    }

    @Override
    public List<TeamPerson> findByTeam(int idTeam) throws Exception {
        List<TeamPerson> list = new ArrayList<>();
        String sql = "SELECT tp.id_team_person, tp.id_team, t.team_name, t.description AS team_description, "
                + "tp.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, typ.type_name, tp.joined_at "
                + "FROM team_person tp "
                + "INNER JOIN team t ON tp.id_team = t.id_team "
                + "INNER JOIN person p ON tp.id_person = p.id_person "
                + "INNER JOIN type_person typ ON p.id_type_person = typ.id_type_person "
                + "WHERE tp.id_team = ?";
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
    public List<TeamPerson> findByPerson(int idPerson) throws Exception {
        List<TeamPerson> list = new ArrayList<>();
        String sql = "SELECT tp.id_team_person, tp.id_team, t.team_name, t.description AS team_description, "
                + "tp.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, typ.type_name, tp.joined_at "
                + "FROM team_person tp "
                + "INNER JOIN team t ON tp.id_team = t.id_team "
                + "INNER JOIN person p ON tp.id_person = p.id_person "
                + "INNER JOIN type_person typ ON p.id_type_person = typ.id_type_person "
                + "WHERE tp.id_person = ?";
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
    public boolean save(TeamPerson tp) throws Exception {
        String sql = "INSERT INTO team_person (id_team, id_person, joined_at) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tp.getTeam().getIdTeam());
            ps.setInt(2, tp.getPerson().getIdPerson());
            ps.setTimestamp(3, tp.getJoinedAt() != null ? Timestamp.valueOf(tp.getJoinedAt()) : null);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int idTeam, int idPerson) throws Exception {
        String sql = "DELETE FROM team_person WHERE id_team = ? AND id_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTeam);
            ps.setInt(2, idPerson);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean exists(int idTeam, int idPerson) throws Exception {
        String sql = "SELECT COUNT(*) FROM team_person WHERE id_team = ? AND id_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTeam);
            ps.setInt(2, idPerson);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
