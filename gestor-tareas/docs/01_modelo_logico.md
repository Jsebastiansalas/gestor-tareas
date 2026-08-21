# Modelo Lógico - Gestor de Tareas

El modelo lógico describe la estructura de las tablas sin depender de un SGBD específico.

---

## type_person (Catálogo de Roles Scrum)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_type_person | Entero | NO | PK |
| type_name | Texto(50) | NO | UNIQUE |

**Restricción:** type_name no se puede repetir (catálogo de roles fijos).

---

## person (Persona)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_person | Entero | NO | PK |
| first_name | Texto(100) | NO | |
| last_name | Texto(100) | NO | |
| email | Texto(150) | NO | UNIQUE |
| id_type_person | Entero | NO | FK → type_person |

**Relación:** Cada persona tiene UN solo tipo/rol (N:1 con type_person).

---

## team (Equipo)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_team | Entero | NO | PK |
| team_name | Texto(100) | NO | UNIQUE |
| description | Texto largo | SÍ | |

**Restricción:** team_name no se puede repetir.

---

## team_person (Relación Equipo ↔ Persona)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_team_person | Entero | NO | PK |
| id_team | Entero | NO | FK → team |
| id_person | Entero | NO | FK → person |
| joined_at | Fecha y hora | NO | |

**Relación Muchos-a-Muchos:** Resuelve que una persona puede estar en muchos equipos y un equipo tiene muchas personas.
**Restricción UNIQUE:** (id_team, id_person) — no se puede duplicar la misma persona en el mismo equipo.

---

## status_task (Catálogo de Estados Kanban)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_status_task | Entero | NO | PK |
| status_name | Texto(50) | NO | UNIQUE |
| status_order | Entero | NO | |

**Relación:** Cada estado define una columna del tablero Kanban. El status_order define el flujo (1=To Do, 2=In Progress, 3=Done).

---

## task (Tarea)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_task | Entero | NO | PK |
| title | Texto(200) | NO | |
| description | Texto largo | SÍ | |
| id_status_task | Entero | NO | FK → status_task |
| id_team | Entero | NO | FK → team |
| id_created_by | Entero | NO | FK → person |
| created_at | Fecha y hora | NO | |
| updated_at | Fecha y hora | SÍ | |

**Relaciones:**
- Cada tarea tiene UN estado (N:1 con status_task).
- Cada tarea pertenece a UN equipo (N:1 con team).
- Cada tarea es creada por UNA persona (N:1 con person).

---

## assement_task (Asignación Persona ↔ Tarea)

| Campo | Tipo | Nulo | Llave |
|---|---|---|---|
| id_assement_task | Entero | NO | PK |
| id_task | Entero | NO | FK → task |
| id_person | Entero | NO | FK → person |
| role_in_task | Texto(50) | NO | |
| assigned_at | Fecha y hora | NO | |

**Relación Muchos-a-Muchos:** Resuelve que una tarea puede ser asignada a muchas personas y una persona puede tener muchas tareas.
**Restricción UNIQUE:** (id_task, id_person) — no se puede asignar la misma persona dos veces a la misma tarea.
