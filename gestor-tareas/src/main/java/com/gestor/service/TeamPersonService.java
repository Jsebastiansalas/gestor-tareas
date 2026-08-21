package com.gestor.service;

import com.gestor.dao.TeamPersonDAO;
import com.gestor.dao.impl.TeamPersonDAOImpl;
import com.gestor.model.Person;
import com.gestor.model.TeamPerson;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para la asignacion de personas a equipos (relacion muchos a muchos).
 * Incluye validacion anti-duplicado para evitar asignaciones repetidas.
 */
public class TeamPersonService {
    private final TeamPersonDAO dao = new TeamPersonDAOImpl();

    // Lista las asignaciones de un equipo
    public List<TeamPerson> listarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    // Lista los equipos a los que pertenece una persona
    public List<TeamPerson> listarPorPersona(int idPerson) throws Exception {
        return dao.findByPerson(idPerson);
    }

    // Asigna una persona a un equipo verificando que no exista duplicado
    public boolean asignar(TeamPerson tp) throws Exception {
        if (tp.getTeam() == null || tp.getTeam().getIdTeam() <= 0)
            throw new IllegalArgumentException("El id del equipo es obligatorio");
        if (tp.getPerson() == null || tp.getPerson().getIdPerson() <= 0)
            throw new IllegalArgumentException("El id de la persona es obligatorio");
        if (dao.exists(tp.getTeam().getIdTeam(), tp.getPerson().getIdPerson()))
            throw new IllegalArgumentException("La persona ya esta asignada a este equipo");
        return dao.save(tp);
    }

    // Remueve la asignacion de una persona en un equipo
    public boolean remover(int idTeam, int idPerson) throws Exception {
        if (idTeam <= 0)
            throw new IllegalArgumentException("El id del equipo debe ser mayor a 0");
        if (idPerson <= 0)
            throw new IllegalArgumentException("El id de la persona debe ser mayor a 0");
        return dao.delete(idTeam, idPerson);
    }

    // Convierte las asignaciones en una lista de personas (miembros del equipo)
    public List<Person> obtenerMiembrosEquipo(int idTeam) throws Exception {
        List<TeamPerson> asignaciones = dao.findByTeam(idTeam);
        List<Person> miembros = new ArrayList<>();
        for (TeamPerson tp : asignaciones) {
            if (tp.getPerson() != null) {
                miembros.add(tp.getPerson());
            }
        }
        return miembros;
    }
}
