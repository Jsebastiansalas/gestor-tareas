package com.gestor.dao;

import com.gestor.model.AssementTask;

import java.util.List;

public interface AssementTaskDAO {
    List<AssementTask> findByTask(int idTask) throws Exception;
    List<AssementTask> findByPerson(int idPerson) throws Exception;
    boolean save(AssementTask at) throws Exception;
    boolean delete(int idAssementTask) throws Exception;
}
