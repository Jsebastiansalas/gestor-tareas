# Normalización de la Base de Datos hasta 4FN

## Estado inicial (relación no normalizada)

Supongamos que los datos se收纳en en una sola tabla:

```
proyecto_personal (id, nombre_persona, apellido, email, rol_persona,
                   nombre_equipo, desc_equipo, fecha_ingreso_equipo,
                   titulo_tarea, desc_tarea, estado_tarea, orden_estado,
                   fecha_creacion, fecha_modificacion,
                   nombre_asignado, apellido_asignado, rol_en_tarea, fecha_asignacion)
```

**Problemas:**
- Datos repetidos (un equipo con 5 personas aparece 5 veces con la misma info del equipo)
- Datos de persona duplicados (una persona en 3 equipos aparece 3 veces)
- Datos de tarea duplicados (una tarea asignada a 3 personas aparece 3 veces)
- Imposible mantener integridad (si cambia el nombre de un equipo, hay que actualizarlo en todas las filas)

---

## 1FN (Primera Forma Normal) — Celdas atómicas, sin grupos repetidos

**Regla:** Cada celda debe contener un solo valor. No deben existir grupos de columnas repetidas.

**Acción:** Separamos los datos en tablas donde cada campo tiene un solo valor.

```
type_person (id_type_person, type_name)
person (id_person, first_name, last_name, email, id_type_person)
team (id_team, team_name, description)
status_task (id_status_task, status_name, status_order)
task (id_task, title, description, id_status_task, id_team, id_created_by, created_at, updated_at)
team_person (id_team_person, id_team, id_person, joined_at)
assement_task (id_assement_task, id_task, id_person, role_in_task, assigned_at)
```

**Resultado:** ✅ Todas las tablas tienen celdas atómicas. No hay listas ni valores múltiples en una celda.

---

## 2FN (Segunda Forma Normal) — Sin dependencias parciales

**Regla:** Cada atributo no-PK debe depender de TODA la PK, no solo de una parte. (Aplica a tablas con PK compuesta.)

**Análisis de tablas con PK simple (ya cumplen 2FN):**
- `type_person` → PK = `id_type_person` (simple) ✅
- `person` → PK = `id_person` (simple) ✅
- `team` → PK = `id_team` (simple) ✅
- `status_task` → PK = `id_status_task` (simple) ✅
- `task` → PK = `id_task` (simple) ✅

**Análisis de tablas con PK compuesta:**
- `team_person` → PK compuesta = (`id_team`, `id_person`)
  - `joined_at` depende de la combinación completa (cuándo se unió esa persona a ese equipo) ✅
  - No hay dependencias parciales.

- `assement_task` → PK compuesta = (`id_task`, `id_person`)
  - `role_in_task` depende de la combinación completa (qué rol tiene esa persona en esa tarea) ✅
  - `assigned_at` depende de la combinación completa ✅

**Resultado:** ✅ Todas las tablas cumplen 2FN. No hay atributos que dependan solo de parte de una PK compuesta.

---

## 3FN (Tercera Forma Normal) — Sin dependencias transitivas

**Regla:** Cada atributo no-PK debe depender DIRECTAMENTE de la PK, no a través de otro atributo no-PK.

**Análisis por tabla:**

### type_person
- `type_name` depende de `id_type_person` (PK) → directamente ✅

### person
- `first_name` depende de `id_person` (PK) → directamente ✅
- `last_name` depende de `id_person` (PK) → directamente ✅
- `email` depende de `id_person` (PK) → directamente ✅
- `id_type_person` depende de `id_person` (PK) → directamente ✅
- No hay transitividad (el tipo de persona NO determina el nombre, etc.) ✅

### team
- `team_name` depende de `id_team` (PK) → directamente ✅
- `description` depende de `id_team` (PK) → directamente ✅

### status_task
- `status_name` depende de `id_status_task` (PK) → directamente ✅
- `status_order` depende de `id_status_task` (PK) → directamente ✅

### task
- `title` depende de `id_task` (PK) → directamente ✅
- `description` depende de `id_task` (PK) → directamente ✅
- `id_status_task` depende de `id_task` (PK) → directamente ✅
- `id_team` depende de `id_task` (PK) → directamente ✅
- `id_created_by` depende de `id_task` (PK) → directamente ✅
- `created_at` depende de `id_task` (PK) → directamente ✅
- `updated_at` depende de `id_task` (PK) → directamente ✅
- No hay transitividad (el estado NO determina el equipo, el creador NO determina el título, etc.) ✅

### team_person
- `joined_at` depende de (`id_team`, `id_person`) (PK) → directamente ✅

### assement_task
- `role_in_task` depende de (`id_task`, `id_person`) (PK) → directamente ✅
- `assigned_at` depende de (`id_task`, `id_person`) (PK) → directamente ✅

**Resultado:** ✅ Todas las tablas cumplen 3FN. No hay dependencias transitivas.

---

## 4FN (Cuarta Forma Normal) — Sin dependencias multivaloradas independientes

**Regla:** No deben existir dos o más dependencias multivaloradas independientes en la misma tabla. Es decir, si una tabla tiene dos relaciones M:N independientes, deben separarse en tablas distintas.

**Análisis:**

### ¿Hay tablas con más de una relación M:N independiente?

Revisamos si alguna tabla tiene dos o más claves foráneas que apunten a entidades diferentes y que representen relaciones M:N independientes entre sí.

- `task` tiene 3 FKs: `id_status_task`, `id_team`, `id_created_by`
  - Pero estas son relaciones **1:N** (una tarea tiene UN estado, UN equipo, UN creador), no M:N.
  - Además, estas dependencias NO son independientes: el equipo y el creador se relacionan con la tarea como un todo.
  - **No viola 4FN** ✅

- `team_person` tiene 2 FKs: `id_team`, `id_person`
  - Pero estas forman la PK compuesta de la relación M:N entre team y person. Es UNA relación, no dos independientes.
  - **No viola 4FN** ✅

- `assement_task` tiene 2 FKs: `id_task`, `id_person`
  - Mismo caso: forman la PK compuesta de la relación M:N entre task y person. Es UNA relación.
  - **No viola 4FN** ✅

### ¿Por qué las tablas intermedias están bien diseñadas?

`team_person` y `assement_task` son **tablas de unión** (junction tables) que resuelven exactamente UNA relación M:N cada una:
- `team_person` → relación Equipo ↔ Persona
- `assement_task` → relación Tarea ↔ Persona

Si por ejemplo intentáramos combinar ambas en una sola tabla `(id_team, id_person, id_task, ...)`, entonces SÍ violaríamos 4FN porque tendríamos dos dependencias multivaloradas independientes.

**Resultado:** ✅ Todas las tablas cumplen 4FN. Las relaciones M:N están correctamente separadas en tablas de unión independientes.

---

## Resumen del Proceso de Normalización

```
1FN → Celdas atómicas, sin grupos repetidos
       └→ Separamos en 7 tablas individuales

2FN → Sin dependencias parciales
       └→ Las PKs compuestas (team_person, assement_task) no tienen
          atributos que dependan solo de parte de la PK

3FN → Sin dependencias transitivas
       └→ Cada atributo depende directamente de su PK,
          no a través de otro atributo

4FN → Sin dependencias multivaloradas independientes
       └→ Cada tabla tiene como máximo UNA relación M:N,
          resuelta con su propia tabla de unión
```
