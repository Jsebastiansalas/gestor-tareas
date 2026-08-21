package com.gestor.model;

public class StatusTask {

    private int idStatusTask;
    private String statusName;
    private int statusOrder;

    public StatusTask() {
    }

    public StatusTask(int idStatusTask, String statusName, int statusOrder) {
        this.idStatusTask = idStatusTask;
        this.statusName = statusName;
        this.statusOrder = statusOrder;
    }

    public int getIdStatusTask() {
        return idStatusTask;
    }

    public void setIdStatusTask(int idStatusTask) {
        this.idStatusTask = idStatusTask;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(int statusOrder) {
        this.statusOrder = statusOrder;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
