package com.gestor.dao.impl;

import com.gestor.dao.ConexionDB;
import com.gestor.dao.PersonDAO;
import com.gestor.model.Person;
import com.gestor.model.TypePerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {

    private Person mapRow(ResultSet rs) throws SQLException {
        Person p = new Person();
        p.setIdPerson(rs.getInt("id_person"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setEmail(rs.getString("email"));

        TypePerson tp = new TypePerson();
        tp.setIdTypePerson(rs.getInt("id_type_person"));
        tp.setTypeName(rs.getString("type_name"));
        p.setTypePerson(tp);

        return p;
    }

    @Override
    public Person findById(int id) throws Exception {
        String sql = "SELECT p.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, tp.type_name "
                + "FROM person p "
                + "INNER JOIN type_person tp ON p.id_type_person = tp.id_type_person "
                + "WHERE p.id_person = ?";
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
    public List<Person> findAll() throws Exception {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT p.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, tp.type_name "
                + "FROM person p "
                + "INNER JOIN type_person tp ON p.id_type_person = tp.id_type_person";
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
    public boolean save(Person entity) throws Exception {
        String sql = "INSERT INTO person (first_name, last_name, email, id_type_person) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setInt(4, entity.getTypePerson().getIdTypePerson());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Person entity) throws Exception {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, email = ?, id_type_person = ? WHERE id_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setInt(4, entity.getTypePerson().getIdTypePerson());
            ps.setInt(5, entity.getIdPerson());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM person WHERE id_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Person> findByTeam(int idTeam) throws Exception {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT p.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, tp.type_name "
                + "FROM person p "
                + "INNER JOIN type_person tp ON p.id_type_person = tp.id_type_person "
                + "INNER JOIN team_person tsp ON p.id_person = tsp.id_person "
                + "WHERE tsp.id_team = ?";
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
    public List<Person> findByTypePerson(int idTypePerson) throws Exception {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT p.id_person, p.first_name, p.last_name, p.email, "
                + "p.id_type_person, tp.type_name "
                + "FROM person p "
                + "INNER JOIN type_person tp ON p.id_type_person = tp.id_type_person "
                + "WHERE p.id_type_person = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTypePerson);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
