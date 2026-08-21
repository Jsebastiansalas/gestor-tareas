package com.gestor.service;

import com.gestor.dao.TaskDAO;
import com.gestor.dao.impl.TaskDAOImpl;
import com.gestor.model.Task;
import java.util.List;

public class TaskService {
    private final TaskDAO dao = new TaskDAOImpl();

    public List<Task> listarTodos() throws Exception {
        return dao.findAll();
    }

    public Task buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public List<Task> listarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    public List<Task> listarPorEstado(int idStatus) throws Exception {
        return dao.findByStatus(idStatus);
    }

    public List<Task> listarPorPersona(int idPerson) throws Exception {
        return dao.findByPerson(idPerson);
    }

    public List<Task> listarPorEquipoYEstado(int idTeam, int idStatus) throws Exception {
        return dao.findByTeamAndStatus(idTeam, idStatus);
    }

    public boolean guardar(Task t) throws Exception {
        if (t.getTitle() == null || t.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("El titulo de la tarea es obligatorio");
        if (t.getStatusTask() == null || t.getStatusTask().getIdStatusTask() <= 0)
            throw new IllegalArgumentException("El estado de la tarea es obligatorio");
        if (t.getTeam() == null || t.getTeam().getIdTeam() <= 0)
            throw new IllegalArgumentException("El equipo es obligatorio");
        if (t.getCreatedBy() == null || t.getCreatedBy().getIdPerson() <= 0)
            throw new IllegalArgumentException("El creador de la tarea es obligatorio");
        return dao.save(t);
    }

    public boolean actualizar(Task t) throws Exception {
        if (t.getIdTask() <= 0)
            throw new IllegalArgumentException("El id de la tarea es obligatorio");
        if (t.getTitle() == null || t.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("El titulo de la tarea es obligatorio");
        if (t.getStatusTask() == null || t.getStatusTask().getIdStatusTask() <= 0)
            throw new IllegalArgumentException("El estado de la tarea es obligatorio");
        if (t.getTeam() == null || t.getTeam().getIdTeam() <= 0)
            throw new IllegalArgumentException("El equipo es obligatorio");
        if (t.getCreatedBy() == null || t.getCreatedBy().getIdPerson() <= 0)
            throw new IllegalArgumentException("El creador de la tarea es obligatorio");
        return dao.update(t);
    }

    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id de la tarea debe ser mayor a 0");
        return dao.delete(id);
    }

    public boolean cambiarEstado(int idTask, int idStatusTask) throws Exception {
        if (idTask <= 0)
            throw new IllegalArgumentException("El id de la tarea debe ser mayor a 0");
        if (idStatusTask <= 0)
            throw new IllegalArgumentException("El id del estado debe ser mayor a 0");
        return dao.updateStatus(idTask, idStatusTask);
    }
}
