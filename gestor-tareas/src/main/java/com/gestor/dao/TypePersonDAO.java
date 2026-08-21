package com.gestor.dao;

import com.gestor.model.TypePerson;

public interface TypePersonDAO extends GenericDAO<TypePerson> {
    TypePerson findByName(String name) throws Exception;
}
