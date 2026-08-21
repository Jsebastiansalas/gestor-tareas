package com.gestor.model;

/**
 * Persona que participa en el proyecto (miembro del equipo).
 * Representa la tabla "person" de la base de datos.
 * Campos: idPerson (clave primaria), firstName, lastName, email
 * y typePerson (relación N:1 con TypePerson que define su rol Scrum).
 */
public class Person {

    private int idPerson;
    private String firstName;
    private String lastName;
    private String email;
    private TypePerson typePerson;

    public Person() {
    }

    public Person(int idPerson, String firstName, String lastName, String email, TypePerson typePerson) {
        this.idPerson = idPerson;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.typePerson = typePerson;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypePerson getTypePerson() {
        return typePerson;
    }

    public void setTypePerson(TypePerson typePerson) {
        this.typePerson = typePerson;
    }

    @Override
    public String toString() {
        // Devuelve "nombre apellido" para que los JComboBox muestren a la persona de forma legible
        return firstName + " " + lastName;
    }
}
