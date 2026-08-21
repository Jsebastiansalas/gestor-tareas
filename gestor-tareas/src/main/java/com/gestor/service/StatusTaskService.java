package com.gestor.service;

import com.gestor.dao.StatusTaskDAO;
import com.gestor.dao.impl.StatusTaskDAOImpl;
import com.gestor.model.StatusTask;
import java.util.List;

public class StatusTaskService {
    private final StatusTaskDAO dao = new StatusTaskDAOImpl();

    public List<StatusTask> listarTodos() throws Exception {
        return dao.findAll();
    }

    public List<StatusTask> listarOrdernados() throws Exception {
        return dao.findAllOrdered();
    }

    public StatusTask buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public StatusTask buscarPorNombre(String nombre) throws Exception {
        return dao.findByName(nombre);
    }

    public boolean guardar(StatusTask s) throws Exception {
        if (s.getStatusName() == null || s.getStatusName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del estado es obligatorio");
        if (s.getStatusOrder() <= 0)
            throw new IllegalArgumentException("El orden del estado debe ser mayor a 0");
        return dao.save(s);
    }

    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id del estado debe ser mayor a 0");
        return dao.delete(id);
    }
}
