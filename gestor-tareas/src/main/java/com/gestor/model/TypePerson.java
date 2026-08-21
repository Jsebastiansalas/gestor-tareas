package com.gestor.model;

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
        return typeName;
    }
}
