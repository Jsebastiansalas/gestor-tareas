package com.gestor.dao;

import com.gestor.model.Team;

/**
 * DAO específico para la entidad Team (equipos de trabajo).
 * Solo hereda las operaciones CRUD genéricas de {@link GenericDAO},
 * sin agregar métodos adicionales.
 */
public interface TeamDAO extends GenericDAO<Team> {
}
