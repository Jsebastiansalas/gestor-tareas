package com.gestor.service;

import com.gestor.dao.AssementTaskDAO;
import com.gestor.dao.impl.AssementTaskDAOImpl;
import com.gestor.model.AssementTask;
import java.util.List;

public class AssementTaskService {
    private final AssementTaskDAO dao = new AssementTaskDAOImpl();

    public List<AssementTask> listarPorTarea(int idTask) throws Exception {
        return dao.findByTask(idTask);
    }

    public List<AssementTask> listarPorPersona(int idPerson) throws Exception {
        return dao.findByPerson(idPerson);
    }

    public boolean asignar(AssementTask at) throws Exception {
        if (at.getTask() == null || at.getTask().getIdTask() <= 0)
            throw new IllegalArgumentException("El id de la tarea es obligatorio");
        if (at.getPerson() == null || at.getPerson().getIdPerson() <= 0)
            throw new IllegalArgumentException("El id de la persona es obligatorio");
        if (at.getRoleInTask() == null || at.getRoleInTask().trim().isEmpty())
            throw new IllegalArgumentException("El rol en la tarea es obligatorio");
        return dao.save(at);
    }

    public boolean remover(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id de la asignacion debe ser mayor a 0");
        return dao.delete(id);
    }
}
