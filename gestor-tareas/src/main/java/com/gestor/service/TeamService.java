package com.gestor.service;

import com.gestor.dao.TeamDAO;
import com.gestor.dao.impl.TeamDAOImpl;
import com.gestor.model.Team;
import java.util.List;

public class TeamService {
    private final TeamDAO dao = new TeamDAOImpl();

    public List<Team> listarTodos() throws Exception {
        return dao.findAll();
    }

    public Team buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public boolean guardar(Team t) throws Exception {
        if (t.getTeamName() == null || t.getTeamName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        return dao.save(t);
    }

    public boolean actualizar(Team t) throws Exception {
        if (t.getIdTeam() <= 0)
            throw new IllegalArgumentException("El id del equipo es obligatorio");
        if (t.getTeamName() == null || t.getTeamName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        return dao.update(t);
    }

    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id del equipo debe ser mayor a 0");
        return dao.delete(id);
    }
}
