package com.gestor.service;

import com.gestor.dao.PersonDAO;
import com.gestor.dao.impl.PersonDAOImpl;
import com.gestor.model.Person;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para el CRUD de personas con validacion de datos,
 * incluyendo formato de email mediante expresion regular.
 */
public class PersonService {
    private final PersonDAO dao = new PersonDAOImpl();
    // Patron regex para validar el formato del email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Retorna todas las personas registradas
    public List<Person> listarTodos() throws Exception {
        return dao.findAll();
    }

    // Busca una persona por su identificador
    public Person buscarPorId(int id) throws Exception {
        return dao.findById(id);
    }

    // Lista las personas pertenecientes a un equipo
    public List<Person> buscarPorEquipo(int idTeam) throws Exception {
        return dao.findByTeam(idTeam);
    }

    // Lista las personas segun su rol (tipo de persona)
    public List<Person> buscarPorTipo(int idType) throws Exception {
        return dao.findByTypePerson(idType);
    }

    // Guarda una persona validando campos obligatorios y formato de email
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

    // Elimina una persona por su id
    public boolean eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El id de la persona debe ser mayor a 0");
        return dao.delete(id);
    }
}
