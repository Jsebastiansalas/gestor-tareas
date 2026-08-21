package com.gestor.model;

/**
 * Catálogo de estados Kanban de una tarea (To Do, In Progress, Done).
 * Representa la tabla "status_task" de la base de datos.
 * Campos: idStatusTask (clave primaria), statusName (nombre del estado)
 * y statusOrder (orden que define la columna en el tablero Kanban).
 */
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
        // Devuelve solo el nombre del estado para que los JComboBox muestren el estado legible en lugar del objeto
        return statusName;
    }
}
