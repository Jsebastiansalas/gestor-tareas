package com.gestor.dao;

import com.gestor.model.StatusTask;

import java.util.List;

public interface StatusTaskDAO extends GenericDAO<StatusTask> {
    StatusTask findByName(String name) throws Exception;
    List<StatusTask> findAllOrdered() throws Exception;
}
