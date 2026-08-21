package com.gestor.model;

import java.time.LocalDateTime;

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
        return "TeamPerson{" +
                "idTeamPerson=" + idTeamPerson +
                ", team=" + team +
                ", person=" + person +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
