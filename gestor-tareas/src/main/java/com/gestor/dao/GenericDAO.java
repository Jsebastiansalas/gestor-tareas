package com.gestor.dao;

import java.util.List;

public interface GenericDAO<T> {
    T findById(int id) throws Exception;
    List<T> findAll() throws Exception;
    boolean save(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(int id) throws Exception;
}
