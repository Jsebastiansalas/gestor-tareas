package com.gestor.model;

import java.time.LocalDateTime;

public class AssementTask {

    private int idAssementTask;
    private Task task;
    private Person person;
    private String roleInTask;
    private LocalDateTime assignedAt;

    public AssementTask() {
    }

    public AssementTask(int idAssementTask, Task task, Person person, String roleInTask, LocalDateTime assignedAt) {
        this.idAssementTask = idAssementTask;
        this.task = task;
        this.person = person;
        this.roleInTask = roleInTask;
        this.assignedAt = assignedAt;
    }

    public int getIdAssementTask() {
        return idAssementTask;
    }

    public void setIdAssementTask(int idAssementTask) {
        this.idAssementTask = idAssementTask;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRoleInTask() {
        return roleInTask;
    }

    public void setRoleInTask(String roleInTask) {
        this.roleInTask = roleInTask;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public String toString() {
        return "AssementTask{" +
                "idAssementTask=" + idAssementTask +
                ", task=" + task +
                ", person=" + person +
                ", roleInTask='" + roleInTask + '\'' +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
