package com.gestor.service;

import com.gestor.dao.TypePersonDAO;
import com.gestor.dao.impl.TypePersonDAOImpl;
import com.gestor.model.TypePerson;
import java.util.List;

public class TypePersonService {
    private final TypePersonDAO dao = new TypePersonDAOImpl();

    public List<TypePerson> listarTodos() throws Exception {
        return dao.findAll();
    }

    public TypePerson buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public TypePerson buscarPorNombre(String nombre) throws Exception {
        return dao.findByName(nombre);
    }

    public boolean guardar(TypePerson tp) throws Exception {
        if (tp.getTypeName() == null || tp.getTypeName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del tipo es obligatorio");
        return dao.save(tp);
    }

    public boolean eliminar(int id) throws Exception {
        return dao.delete(id);
    }
}
