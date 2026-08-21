package com.gestor.dao;

import com.gestor.model.Task;

import java.util.List;

public interface TaskDAO extends GenericDAO<Task> {
    List<Task> findByTeam(int idTeam) throws Exception;
    List<Task> findByStatus(int idStatusTask) throws Exception;
    List<Task> findByPerson(int idPerson) throws Exception;
    List<Task> findByTeamAndStatus(int idTeam, int idStatusTask) throws Exception;
    boolean updateStatus(int idTask, int idStatusTask) throws Exception;
}
