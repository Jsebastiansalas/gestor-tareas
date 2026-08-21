package com.gestor.service;

import com.gestor.dao.TaskDAO;
import com.gestor.dao.impl.TaskDAOImpl;
import com.gestor.model.Task;
import java.util.List;

/**
 * Servicio para el CRUD de tareas.
 * Incluye cambiarEstado para mover tareas en el flujo del tablero Kanban.
 */
public class TaskService {
    private final TaskDAO dao = new TaskDAOImpl();

    // Retorna todas las tareas registradas
    public List<Task> listarTodos() throws Exception {
        return dao.findAll();
    }

    // Busca una tarea por su identificador
    public Task buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    // Lista las tareas de un equipo
    public List<Task> listarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    // Lista las tareas segun su estado
    public List<Task> listarPorEstado(int idStatus) throws Exception {
        return dao.findByStatus(idStatus);
    }

    // Lista las tareas asignadas a una persona
    public List<Task> listarPorPersona(int idPerson) throws Exception {
        return dao.findByPerson(idPerson);
    }

    // Lista las tareas combinando filtros de equipo y estado
    public List<Task> listarPorEquipoYEstado(int idTeam, int idStatus) throws Exception {
        return dao.findByTeamAndStatus(idTeam, idStatus);
    }

    // Guarda una tarea validando titulo, estado, equipo y creador
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

    // Elimina una tarea por su id
    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id de la tarea debe ser mayor a 0");
        return dao.delete(id);
    }

    // Cambia el estado de una tarea (movimiento entre columnas del Kanban)
    public boolean cambiarEstado(int idTask, int idStatusTask) throws Exception {
        if (idTask <= 0)
            throw new IllegalArgumentException("El id de la tarea debe ser mayor a 0");
        if (idStatusTask <= 0)
            throw new IllegalArgumentException("El id del estado debe ser mayor a 0");
        return dao.updateStatus(idTask, idStatusTask);
    }
}
