package com.gestor.model;

/**
 * Equipo de trabajo al que pertenecen las personas y se asignan las tareas.
 * Representa la tabla "team" de la base de datos.
 * Campos: idTeam (clave primaria), teamName (nombre del equipo)
 * y description (descripción opcional del equipo).
 */
public class Team {

    private int idTeam;
    private String teamName;
    private String description;

    public Team() {
    }

    public Team(int idTeam, String teamName, String description) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.description = description;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        // Devuelve solo el nombre del equipo para que los JComboBox muestren el equipo legible en lugar del objeto
        return teamName;
    }
}
