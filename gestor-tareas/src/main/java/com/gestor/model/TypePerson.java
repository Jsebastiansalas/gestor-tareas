package com.gestor.model;

/**
 * Catálogo de roles Scrum que puede tener una persona en el proyecto
 * (por ejemplo: Product Owner, Scrum Master, Developer).
 * Representa la tabla "type_person" de la base de datos.
 * Campos: idTypePerson (clave primaria) y typeName (nombre del rol).
 */
public class TypePerson {

    private int idTypePerson;
    private String typeName;

    public TypePerson() {
    }

    public TypePerson(int idTypePerson, String typeName) {
        this.idTypePerson = idTypePerson;
        this.typeName = typeName;
    }

    public int getIdTypePerson() {
        return idTypePerson;
    }

    public void setIdTypePerson(int idTypePerson) {
        this.idTypePerson = idTypePerson;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        // Devuelve solo el nombre del rol para que los JComboBox muestren el tipo legible en lugar del objeto
        return typeName;
    }
}
