package com.gestor.dao;

import com.gestor.model.StatusTask;

import java.util.List;

/**
 * DAO específico para la entidad StatusTask (estados posibles de una tarea).
 * Hereda el CRUD genérico de {@link GenericDAO} y agrega búsquedas por nombre
 * y un listado ordenado según el campo status_order, que define el flujo
 * de estados de las tareas.
 */
public interface StatusTaskDAO extends GenericDAO<StatusTask> {
    /** Busca un estado de tarea por su nombre exacto. */
    StatusTask findByName(String name) throws Exception;
    /** Lista todos los estados ordenados por su posición en el flujo (status_order). */
    List<StatusTask> findAllOrdered() throws Exception;
}
