package com.gestor.service;

import com.gestor.dao.PersonDAO;
import com.gestor.dao.impl.PersonDAOImpl;
import com.gestor.model.Person;
import java.util.List;
import java.util.regex.Pattern;

public class PersonService {
    private final PersonDAO dao = new PersonDAOImpl();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public List<Person> listarTodos() throws Exception {
        return dao.findAll();
    }

    public Person buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    public List<Person> buscarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    public List<Person> buscarPorTipo(int idType) throws Exception {
        return dao.findByTypePerson(idType);
    }

    public boolean guardar(Person p) throws Exception {
        if (p.getFirstName() == null || p.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (p.getLastName() == null || p.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("El apellido es obligatorio");
        if (p.getEmail() == null || p.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("El email es obligatorio");
        if (!EMAIL_PATTERN.matcher(p.getEmail().trim()).matches())
            throw new IllegalArgumentException("El formato del email no es valido");
        if (p.getTypePerson() == null || p.getTypePerson().getIdTypePerson() <= 0)
            throw new IllegalArgumentException("El tipo de persona es obligatorio");
        return dao.save(p);
    }

    public boolean actualizar(Person p) throws Exception {
        if (p.getIdPerson() <= 0)
            throw new IllegalArgumentException("El id de la persona es obligatorio");
        if (p.getFirstName() == null || p.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (p.getLastName() == null || p.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("El apellido es obligatorio");
        if (p.getEmail() == null || p.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("El email es obligatorio");
        if (!EMAIL_PATTERN.matcher(p.getEmail().trim()).matches())
            throw new IllegalArgumentException("El formato del email no es valido");
        if (p.getTypePerson() == null || p.getTypePerson().getIdTypePerson() <= 0)
            throw new IllegalArgumentException("El tipo de persona es obligatorio");
        return dao.update(p);
    }

    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id de la persona debe ser mayor a 0");
        return dao.delete(id);
    }
}
