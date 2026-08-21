package com.gestor.service;

import com.gestor.dao.TeamDAO;
import com.gestor.dao.impl.TeamDAOImpl;
import com.gestor.model.Team;
import java.util.List;

/**
 * Servicio para el CRUD de equipos.
 * Actua como capa intermedia entre la GUI y el DAO.
 */
public class TeamService {
    private final TeamDAO dao = new TeamDAOImpl();

    // Retorna todos los equipos registrados
    public List<Team> listarTodos() throws Exception {
        return dao.findAll();
    }

    // Busca un equipo por su identificador
    public Team buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    // Guarda un nuevo equipo validando que el nombre no este vacio
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

    // Elimina un equipo por su id
    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id del equipo debe ser mayor a 0");
        return dao.delete(id);
    }
}
