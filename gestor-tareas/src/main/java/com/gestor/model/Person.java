package com.gestor.model;

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
        return firstName + " " + lastName;
    }
}
