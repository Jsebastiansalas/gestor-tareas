CREATE DATABASE IF NOT EXISTS gestor_tareas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestor_tareas;

-- ============================================================
-- CATALOGO: Tipos de persona (roles Scrum)
-- ============================================================
CREATE TABLE type_person (
    id_type_person INT AUTO_INCREMENT PRIMARY KEY,
    type_name      VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

INSERT INTO type_person (type_name) VALUES
    ('Scrum Master'),
    ('Product Owner'),
    ('Developer');

-- ============================================================
-- PERSONA
-- ============================================================
CREATE TABLE person (
    id_person       INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    id_type_person  INT NOT NULL,
    CONSTRAINT fk_person_type FOREIGN KEY (id_type_person)
        REFERENCES type_person(id_type_person)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================================
-- EQUIPO
-- ============================================================
CREATE TABLE team (
    id_team     INT AUTO_INCREMENT PRIMARY KEY,
    team_name   VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
) ENGINE=InnoDB;

-- ============================================================
-- RELACION Equipo <-> Persona (M-M)
-- ============================================================
CREATE TABLE team_person (
    id_team_person INT AUTO_INCREMENT PRIMARY KEY,
    id_team        INT NOT NULL,
    id_person      INT NOT NULL,
    joined_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tp_team   FOREIGN KEY (id_team)   REFERENCES team(id_team)   ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_tp_person FOREIGN KEY (id_person)  REFERENCES person(id_person) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_team_person UNIQUE (id_team, id_person)
) ENGINE=InnoDB;

-- ============================================================
-- CATALOGO: Estados de tarea
-- ============================================================
CREATE TABLE status_task (
    id_status_task INT AUTO_INCREMENT PRIMARY KEY,
    status_name    VARCHAR(50) NOT NULL UNIQUE,
    status_order   INT NOT NULL
) ENGINE=InnoDB;

INSERT INTO status_task (status_name, status_order) VALUES
    ('To Do',        1),
    ('In Progress',  2),
    ('Done',         3);

-- ============================================================
-- TAREA
-- ============================================================
CREATE TABLE task (
    id_task         INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    id_status_task  INT NOT NULL,
    id_team         INT NOT NULL,
    created_by      INT NOT NULL,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_status FOREIGN KEY (id_status_task)
        REFERENCES status_task(id_status_task) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_task_team   FOREIGN KEY (id_team)
        REFERENCES team(id_team) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_task_creator FOREIGN KEY (created_by)
        REFERENCES person(id_person) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================================
-- ASIGNACION Persona <-> Tarea (M-M)
-- ============================================================
CREATE TABLE assement_task (
    id_assement_task INT AUTO_INCREMENT PRIMARY KEY,
    id_task          INT NOT NULL,
    id_person        INT NOT NULL,
    role_in_task     VARCHAR(50) NOT NULL DEFAULT 'Assigned',
    assigned_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_at_task   FOREIGN KEY (id_task)   REFERENCES task(id_task)     ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_at_person FOREIGN KEY (id_person) REFERENCES person(id_person) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_task_person UNIQUE (id_task, id_person)
) ENGINE=InnoDB;
