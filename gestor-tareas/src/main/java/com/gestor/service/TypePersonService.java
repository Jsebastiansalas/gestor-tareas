package com.gestor.service;

import com.gestor.dao.TypePersonDAO;
import com.gestor.dao.impl.TypePersonDAOImpl;
import com.gestor.model.TypePerson;
import java.util.List;

/**
 * Servicio para el CRUD del catalogo de roles (tipos de persona).
 * Actua como capa intermedia entre la GUI y el DAO.
 */
public class TypePersonService {
    private final TypePersonDAO dao = new TypePersonDAOImpl();

    // Retorna todos los roles registrados
    public List<TypePerson> listarTodos() throws Exception {
        return dao.findAll();
    }

    // Busca un rol por su identificador
    public TypePerson buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    // Busca un rol por su nombre
    public TypePerson buscarPorNombre(String nombre) throws Exception {
        return dao.findByName(nombre);
    }

    // Guarda un nuevo rol validando que el nombre no este vacio
    public boolean guardar(TypePerson tp) throws Exception {
        if (tp.getTypeName() == null || tp.getTypeName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del tipo es obligatorio");
        return dao.save(tp);
    }

    // Elimina un rol por su id
    public boolean eliminar(int id) throws Exception {
        return dao.delete(id);
    }
}
