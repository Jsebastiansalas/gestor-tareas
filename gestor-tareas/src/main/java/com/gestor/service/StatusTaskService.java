package com.gestor.service;

import com.gestor.dao.StatusTaskDAO;
import com.gestor.dao.impl.StatusTaskDAOImpl;
import com.gestor.model.StatusTask;
import java.util.List;

/**
 * Servicio para el CRUD de estados de tarea.
 * El metodo listarOrdernados alimenta las columnas del tablero Kanban.
 */
public class StatusTaskService {
    private final StatusTaskDAO dao = new StatusTaskDAOImpl();

    // Retorna todos los estados sin orden especifico
    public List<StatusTask> listarTodos() throws Exception {
        return dao.findAll();
    }

    // Retorna los estados ordenados por su campo de orden (para el Kanban)
    public List<StatusTask> listarOrdernados() throws Exception {
        return dao.findAllOrdered();
    }

    // Busca un estado por su identificador
    public StatusTask buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    // Busca un estado por su nombre
    public StatusTask buscarPorNombre(String nombre) throws Exception {
        return dao.findByName(nombre);
    }

    // Guarda un estado validando nombre y orden obligatorios
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
