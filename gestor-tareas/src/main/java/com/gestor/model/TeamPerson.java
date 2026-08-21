package com.gestor.model;

import java.time.LocalDateTime;

/**
 * Tabla intermedia que resuelve la relación muchos a muchos entre Team y Person
 * (una persona puede pertenecer a varios equipos y un equipo tener varias personas).
 * Representa la tabla "team_person" de la base de datos.
 * Campos: idTeamPerson (clave primaria), team y person (claves foráneas)
 * y joinedAt (fecha en que la persona se unió al equipo).
 */
public class TeamPerson {

    private int idTeamPerson;
    private Team team;
    private Person person;
    private LocalDateTime joinedAt;

    public TeamPerson() {
    }

    public TeamPerson(int idTeamPerson, Team team, Person person, LocalDateTime joinedAt) {
        this.idTeamPerson = idTeamPerson;
        this.team = team;
        this.person = person;
        this.joinedAt = joinedAt;
    }

    public int getIdTeamPerson() {
        return idTeamPerson;
    }

    public void setIdTeamPerson(int idTeamPerson) {
        this.idTeamPerson = idTeamPerson;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public String toString() {
        // Devuelve todos los campos para depuración; no se usa en JComboBox porque no es un catálogo
        return "TeamPerson{" +
                "idTeamPerson=" + idTeamPerson +
                ", team=" + team +
                ", person=" + person +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
