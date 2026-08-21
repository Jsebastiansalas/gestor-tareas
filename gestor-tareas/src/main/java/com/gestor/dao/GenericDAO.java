package com.gestor.dao;

import java.util.List;

/**
 * Interfaz genérica del patrón DAO que define las operaciones CRUD básicas
 * (crear, leer, actualizar y eliminar) para cualquier entidad del sistema.
 * Las interfaces específicas (PersonDAO, TeamDAO, etc.) la extienden
 * indicando su tipo de entidad concreto mediante el genérico {@code <T>}.
 */
public interface GenericDAO<T> {
    /** Busca una entidad por su identificador primario. */
    T findById(int id) throws Exception;
    /** Recupera todas las entidades de la tabla. */
    List<T> findAll() throws Exception;
    /** Inserta una nueva entidad en la base de datos. */
    boolean save(T entity) throws Exception;
    /** Actualiza los datos de una entidad existente. */
    boolean update(T entity) throws Exception;
    /** Elimina una entidad por su identificador primario. */
    boolean delete(int id) throws Exception;
}
