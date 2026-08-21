package com.gestor.dao;

import com.gestor.model.Person;

import java.util.List;

public interface PersonDAO extends GenericDAO<Person> {
    List<Person> findByTeam(int idTeam) throws Exception;
    List<Person> findByTypePerson(int idTypePerson) throws Exception;
}
