package com.gestor.dao;

import com.gestor.model.AssementTask;

import java.util.List;

/**
 * DAO para la entidad AssementTask, la tabla de relación que asigna personas
 * a tareas con un rol específico. NO extiende {@link GenericDAO} porque sus
 * consultas se centran en la tarea o en la persona asignada, no en un CRUD
 * por identificador simple.
 */
public interface AssementTaskDAO {
    /** Lista las asignaciones (personas y roles) de una tarea. */
    List<AssementTask> findByTask(int idTask) throws Exception;
    /** Lista las tareas asignadas a una persona. */
    List<AssementTask> findByPerson(int idPerson) throws Exception;
    /** Inserta una nueva asignación de persona a tarea. */
    boolean save(AssementTask at) throws Exception;
    /** Elimina una asignación por su identificador. */
    boolean delete(int idAssementTask) throws Exception;
}
