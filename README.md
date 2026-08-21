# Gestor de Tareas - Scrum

Sistema de gestión de tareas tipo Scrum con interfaz gráfica (Java Swing) y base de datos MySQL.

## Arquitectura

```
com.gestor
├── Main.java                  ← Punto de entrada
├── model/                     ← POJOs (7 clases)
│   ├── TypePerson.java        ← Catálogo de roles Scrum
│   ├── Person.java            ← Persona del equipo
│   ├── Team.java              ← Equipo de trabajo
│   ├── TeamPerson.java        ← Relación M:M equipo-persona
│   ├── StatusTask.java        ← Catálogo de estados Kanban
│   ├── Task.java              ← Tarea del proyecto
│   └── AssementTask.java      ← Asignación persona-tarea
├── dao/                       ← Acceso a datos (9 archivos)
│   ├── ConexionDB.java        ← Conexión JDBC a MySQL
│   ├── GenericDAO.java        ← Interfaz genérica CRUD
│   ├── PersonDAO.java         ← Interfaz persona
│   ├── TeamDAO.java           ← Interfaz equipo
│   ├── TaskDAO.java           ← Interfaz tarea
│   ├── StatusTaskDAO.java     ← Interfaz estado
│   ├── TypePersonDAO.java     ← Interfaz tipo persona
│   ├── TeamPersonDAO.java     ← Interfaz relación equipo-persona
│   ├── AssementTaskDAO.java   ← Interfaz asignación tarea
│   └── impl/                  ← Implementaciones SQL (7 clases)
│       ├── PersonDAOImpl.java
│       ├── TeamDAOImpl.java
│       ├── TaskDAOImpl.java
│       ├── StatusTaskDAOImpl.java
│       ├── TypePersonDAOImpl.java
│       ├── TeamPersonDAOImpl.java
│       └── AssementTaskDAOImpl.java
├── service/                   ← Lógica del trabajo (7 clases)
│   ├── TypePersonService.java
│   ├── PersonService.java
│   ├── TeamService.java
│   ├── TeamPersonService.java
│   ├── StatusTaskService.java
│   ├── TaskService.java
│   └── AssementTaskService.java
└── gui/                       ← Interfaz gráfica Swing (4 clases)
    ├── MainFrame.java         ← Ventana principal + sidebar
    ├── PersonasPanel.java     ← CRUD de personas
    ├── EquiposPanel.java      ← Gestión de equipos y miembros
    └── TareasPanel.java       ← Tablero Kanban de tareas
```

## Base de Datos

7 tablas en MySQL (`gestor_tareas`):

| Tabla | Descripción | Relación |
|---|---|---|
| `type_person` | Catálogo: Scrum Master, Product Owner, Developer | 1→N con `person` |
| `person` | Persona con nombre, email y rol | M:N con `team`, M:N con `task` |
| `team` | Equipo de trabajo | 1→N con `task` |
| `team_person` | Unión equipo↔persona (M:M) | UNIQUE(`id_team`,`id_person`) |
| `status_task` | Catálogo: To Do, In Progress, Done | 1→N con `task` |
| `task` | Tarea con título, estado, equipo, creador | 3 FKs |
| `assement_task` | Asignación persona↔tarea (M:M) | UNIQUE(`id_task`,`id_person`) |

## Requisitos

- JDK 17 o superior
- MySQL Server en `127.0.0.1:3306`
- Ejecutar `sql/schema.sql` para crear la BD

## Instalación y Ejecución

### Paso 1: Crear la base de datos

```bash
mysql -u root -p < sql/schema.sql
```

O ejecutar el contenido de `sql/schema.sql` desde MySQL Workbench.

### Paso 2: Configurar conexión

Editar `src/main/java/com/gestor/dao/ConexionDB.java` con tus credenciales:

```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/gestor_tareas?...";
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña";
```

### Paso 3: Ejecutar

**Opción A** — Doble clic en `ejecutar.bat`

**Opción B** — Línea de comandos:
```bash
cd gestor-tareas
java -cp "target\classes;lib\mysql-connector-j-8.2.0.jar;lib\LGoodDatePicker-11.2.1.jar" com.gestor.Main
```

### Compilar desde código fuente

```bash
javac --release 21 -cp "lib/*" -d target/classes -sourcepath src/main/java src/main/java/com/gestor/**/*.java src/main/java/com/gestor/*.java
```

## Uso de la Aplicación

### 1. Crear personas
Ir a pestaña **Personas** → "+ Nueva Persona" → llenar nombre, apellido, email, rol (Scrum Master/PO/Developer)

### 2. Crear equipos
Ir a pestaña **Equipos** → "+ Nuevo Equipo" → nombre y descripción

### 3. Agregar miembros a equipos
Seleccionar equipo en la tabla → "+ Agregar Miembro" → elegir persona del combo

### 4. Crear tareas
Ir a pestaña **Tareas** → "+ Nueva Tarea" → título, descripción, equipo, estado, creador

### 5. Asignar personas a tareas
Seleccionar tarea → "Editar" → sección "Personas Asignadas" → "+ Asignar Persona" → elegir miembro del equipo + rol en tarea

### 6. Avanzar estados (Kanban)
Clic en **"Move Right →"** en cualquier tarjeta para mover: To Do → In Progress → Done

### 7. Filtrar tareas
Usar los combos superiores para filtrar por Equipo, Estado o Persona

## Patrones de Diseño

| Patrón | Aplicación |
|---|---|
| DAO (Data Access Object) | Interfaces + implementaciones separan SQL del resto |
| Genéricos Java | `GenericDAO<T>` reutiliza CRUD para 5 entidades |
| MVC (variante) | Modelo=POJOs+DAO, Vista=Swing, Controlador=Services |
| Programación defensiva | Validaciones con `IllegalArgumentException` en Services |

## Dependencias

| Librería | Versión | Uso |
|---|---|---|
| MySQL Connector/J | 8.2.0 | Driver JDBC para MySQL |
| LGoodDatePicker | 11.2.1 | Componentes de fecha para Swing |
