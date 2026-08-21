package com.gestor.service;

import com.gestor.dao.TeamPersonDAO;
import com.gestor.dao.impl.TeamPersonDAOImpl;
import com.gestor.model.Person;
import com.gestor.model.TeamPerson;
import java.util.ArrayList;
import java.util.List;

public class TeamPersonService {
    private final TeamPersonDAO dao = new TeamPersonDAOImpl();

    public List<TeamPerson> listarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    public List<TeamPerson> listarPorPersona(int idPerson) throws Exception {
        return dao.findByPerson(idPerson);
    }

    public boolean asignar(TeamPerson tp) throws Exception {
        if (tp.getTeam() == null || tp.getTeam().getIdTeam() <= 0)
            throw new IllegalArgumentException("El id del equipo es obligatorio");
        if (tp.getPerson() == null || tp.getPerson().getIdPerson() <= 0)
            throw new IllegalArgumentException("El id de la persona es obligatorio");
        if (dao.exists(tp.getTeam().getIdTeam(), tp.getPerson().getIdPerson()))
            throw new IllegalArgumentException("La persona ya esta asignada a este equipo");
        return dao.save(tp);
    }

    public boolean remover(int idTeam, int idPerson) throws Exception {
        if (idTeam <= 0)
            throw new IllegalArgumentException("El id del equipo debe ser mayor a 0");
        if (idPerson <= 0)
            throw new IllegalArgumentException("El id de la persona debe ser mayor a 0");
        return dao.delete(idTeam, idPerson);
    }

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
