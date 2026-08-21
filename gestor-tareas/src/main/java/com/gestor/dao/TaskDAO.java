package com.gestor.dao;

import com.gestor.model.Task;

import java.util.List;

/**
 * DAO específico para la entidad Task (tareas).
 * Hereda el CRUD genérico de {@link GenericDAO} y agrega métodos de filtrado
 * (por equipo, estado, persona o combinación de ambos) además de una
 * actualización puntual del estado de la tarea.
 */
public interface TaskDAO extends GenericDAO<Task> {
    /** Lista las tareas que pertenecen a un equipo. */
    List<Task> findByTeam(int idTeam) throws Exception;
    /** Lista las tareas según su estado. */
    List<Task> findByStatus(int idStatusTask) throws Exception;
    /** Lista las tareas creadas por una persona. */
    List<Task> findByPerson(int idPerson) throws Exception;
    /** Lista las tareas filtradas por equipo y estado a la vez. */
    List<Task> findByTeamAndStatus(int idTeam, int idStatusTask) throws Exception;
    /** Cambia únicamente el estado de una tarea (p. ej. al avanzar en el flujo). */
    boolean updateStatus(int idTask, int idStatusTask) throws Exception;
}
