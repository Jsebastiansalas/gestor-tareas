package com.gestor.dao;

import com.gestor.model.TeamPerson;

import java.util.List;

/**
 * DAO para la entidad TeamPerson, la tabla de relación muchos-a-muchos
 * entre equipos y personas. NO extiende {@link GenericDAO} porque la tabla
 * usa una clave compuesta (id_team + id_person) en lugar de un id simple,
 * por lo que define sus propias operaciones. El método exists sirve como
 * verificación anti-duplicados antes de insertar una membresía.
 */
public interface TeamPersonDAO {
    /** Lista las membresías (personas) de un equipo. */
    List<TeamPerson> findByTeam(int idTeam) throws Exception;
    /** Lista los equipos a los que pertenece una persona. */
    List<TeamPerson> findByPerson(int idPerson) throws Exception;
    /** Inserta una nueva membresía de persona en un equipo. */
    boolean save(TeamPerson tp) throws Exception;
    /** Elimina la membresía usando la clave compuesta (equipo + persona). */
    boolean delete(int idTeam, int idPerson) throws Exception;
    /** Verifica si ya existe la relación equipo-persona (evita duplicados). */
    boolean exists(int idTeam, int idPerson) throws Exception;
}
