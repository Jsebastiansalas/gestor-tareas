# Diagrama Entidad-Relación (E-R) - Gestor de Tareas

## Entidades

### Entidades Fuertes (tienen PK propio)
| Entidad | Descripción | Atributos clave |
|---|---|---|
| **type_person** | Catálogo de roles Scrum | id, nombre |
| **person** | Persona del proyecto | id, nombre, apellido, email |
| **team** | Equipo de trabajo | id, nombre, descripción |
| **status_task** | Catálogo de estados Kanban | id, nombre, orden |
| **task** | Tarea del proyecto | id, título, descripción, fechas |

### Entidades Débiles / De Relación
| Entidad | Descripción | Atributos clave |
|---|---|---|
| **team_person** | Asociación equipo↔persona | fecha de ingreso |
| **assement_task** | Asociación tarea↔persona | rol en tarea, fecha de asignación |

---

## Relaciones

### 1. type_person ↔ person (1:N)

```
type_person 1 ──────────── N person
```

- **Cardinalidad:** Un tipo de persona puede tener muchas personas. Una persona tiene un solo tipo.
- **FK:** person.id_type_person → type_person.id_type_person
- **ON DELETE RESTRICT:** No se puede borrar un rol si hay personas que lo usan.

### 2. team ↔ person (M:N) → team_person

```
team 1 ──── N team_person N ──── 1 person
```

- **Cardinalidad:** Un equipo tiene muchas personas. Una persona puede estar en muchos equipos.
- **Tabla intermedia:** team_person resuelve la relación M:N.
- **FK team_person.id_team → team.id_team** (ON DELETE CASCADE)
- **FK team_person.id_person → person.id_person** (ON DELETE CASCADE)
- **UNIQUE (id_team, id_person):** No duplicar persona en el mismo equipo.

### 3. status_task ↔ task (1:N)

```
status_task 1 ──────────── N task
```

- **Cardinalidad:** Un estado puede tener muchas tareas. Una tarea tiene un solo estado.
- **FK:** task.id_status_task → status_task.id_status_task
- **ON DELETE RESTRICT:** No se puede borrar un estado si tiene tareas asociadas.

### 4. team ↔ task (1:N)

```
team 1 ──────────── N task
```

- **Cardinalidad:** Un equipo puede tener muchas tareas. Una tarea pertenece a un solo equipo.
- **FK:** task.id_team → team.id_team
- **ON DELETE CASCADE:** Al borrar un equipo se borran sus tareas.

### 5. person ↔ task (1:N) — Creador

```
person 1 ──────────── N task (como creador)
```

- **Cardinalidad:** Una persona puede crear muchas tareas. Una tarea es creada por una sola persona.
- **FK:** task.id_created_by → person.id_person
- **ON DELETE RESTRICT:** No se puede borrar una persona si creó tareas.

### 6. task ↔ person (M:N) → assement_task

```
task 1 ──── N assement_task N ──── 1 person
```

- **Cardinalidad:** Una tarea puede ser asignada a muchas personas. Una persona puede tener muchas tareas asignadas.
- **Tabla intermedia:** assement_task resuelve la relación M:N.
- **FK assement_task.id_task → task.id_task** (ON DELETE CASCADE)
- **FK assement_task.id_person → person.id_person** (ON DELETE CASCADE)
- **UNIQUE (id_task, id_person):** No duplicar persona en la misma tarea.
- **Atributo adicional:** role_in_task (rol de la persona dentro de la tarea).

---

## Diagrama E-R (Representación Textual)

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│ type_person  │ 1     N │    person    │ N     1 │    team      │
│──────────────│────────→│──────────────│←────────│──────────────│
│ id (PK)      │         │ id (PK)      │         │ id (PK)      │
│ type_name    │         │ first_name   │         │ team_name    │
│              │         │ last_name    │         │ description  │
│              │         │ email        │         │              │
│              │         │ id_type (FK) │         │              │
└──────────────┘         └──────┬───────┘         └──────┬───────┘
                                │                        │
                                │ N                      │ 1
                                │                        │
                         ┌──────┴───────┐         ┌──────┴───────┐
                         │asassement_task│         │    task      │
                         │──────────────│         │──────────────│
                         │ id (PK)      │←────────│ id (PK)      │
                         │ id_task (FK) │    N   1│ title        │
                         │ id_person(FK)│         │ description  │
                         │ role_in_task │         │ id_status(FK)│
                         │ assigned_at  │         │ id_team (FK) │
                         └──────────────┘         │ id_creator   │
                                                  │ created_at   │
                         ┌──────────────┐         │ updated_at   │
                         │ status_task  │ 1     N │              │
                         │──────────────│────────→│              │
                         │ id (PK)      │         └──────────────┘
                         │ status_name  │
                         │ status_order │
                         └──────────────┘

                         ┌──────────────┐
                         │ team_person  │
                         │──────────────│
                         │ id (PK)      │
                         │ id_team (FK) │ ←→ team
                         │ id_person(FK)│ ←→ person
                         │ joined_at    │
                         └──────────────┘
```

---

## Resumen de Relaciones

| Relación | Tipo | Tabla intermedia | FK constraints |
|---|---|---|---|
| type_person → person | 1:N | No (FK directo) | RESTRICT |
| team ↔ person | M:N | team_person | CASCADE |
| status_task → task | 1:N | No (FK directo) | RESTRICT |
| team → task | 1:N | No (FK directo) | CASCADE |
| person → task (creador) | 1:N | No (FK directo) | RESTRICT |
| task ↔ person (asignación) | M:N | assement_task | CASCADE |
