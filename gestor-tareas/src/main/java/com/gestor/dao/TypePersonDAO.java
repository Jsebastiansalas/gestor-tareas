package com.gestor.dao;

import com.gestor.model.TypePerson;

/**
 * DAO específico para la entidad TypePerson (tipos de persona, p. ej. administrador o colaborador).
 * Hereda el CRUD genérico de {@link GenericDAO} y agrega una búsqueda por nombre,
 * útil para localizar un tipo de persona sin conocer su identificador.
 */
public interface TypePersonDAO extends GenericDAO<TypePerson> {
    /** Busca un tipo de persona por su nombre exacto. */
    TypePerson findByName(String name) throws Exception;
}
