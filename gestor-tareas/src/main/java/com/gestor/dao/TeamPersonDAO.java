package com.gestor.dao;

import com.gestor.model.TeamPerson;

import java.util.List;

public interface TeamPersonDAO {
    List<TeamPerson> findByTeam(int idTeam) throws Exception;
    List<TeamPerson> findByPerson(int idPerson) throws Exception;
    boolean save(TeamPerson tp) throws Exception;
    boolean delete(int idTeam, int idPerson) throws Exception;
    boolean exists(int idTeam, int idPerson) throws Exception;
}
