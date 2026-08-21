# Diagrama UML de Clases - Gestor de Tareas

## Diagrama de clases completo

```
┌─────────────────────────────────────┐
│           <<interface>>             │
│           GenericDAO<T>             │
│─────────────────────────────────────│
│ + findById(id: int): T              │
│ + findAll(): List<T>                │
│ + save(entity: T): boolean          │
│ + update(entity: T): boolean        │
│ + delete(id: int): boolean          │
└──────────────┬──────────────────────┘
               │ <<implements>>
    ┌──────────┼──────────────────────────────────────┐
    │          │                                      │
    ▼          ▼                                      ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────────┐
│PersonDAO     │  │ TeamDAO      │  │ TaskDAO          │
│──────────────│  │──────────────│  │──────────────────│
│+findByTeam() │  │(sin métodos  │  │+findByTeam()     │
│+findByType() │  │ extra)       │  │+findByStatus()   │
└──────┬───────┘  └──────────────┘  │+findByPerson()   │
       │                            │+findByTeamAnd..()│
       ▼                            │+updateStatus()   │
┌──────────────┐                    └────────┬─────────┘
│PersonDAOImpl │                             │
│──────────────│                             ▼
│mapRow()      │                    ┌──────────────────┐
└──────────────┘                    │ TaskDAOImpl      │
                                    │──────────────────│
                                    │BASE_QUERY (5 JOIN)│
                                    │mapRow()          │
                                    └──────────────────┘

┌─────────────────────────────────────┐
│           <<interface>>             │
│        TeamPersonDAO                │
│─────────────────────────────────────│
│ + findByTeam(idTeam): List          │
│ + findByPerson(idPerson): List      │
│ + save(tp): boolean                 │
│ + delete(idTeam, idPerson): boolean │
│ + exists(idTeam, idPerson): boolean │
└──────────────┬──────────────────────┘
               │ <<implements>>
               ▼
┌─────────────────────────────────────┐
│        TeamPersonDAOImpl            │
│─────────────────────────────────────│
│ mapRow() - reconstruye TeamPerson   │
│   con Team completo + Person        │
│   completo + TypePerson             │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│           <<interface>>             │
│        AssementTaskDAO              │
│─────────────────────────────────────│
│ + findByTask(idTask): List          │
│ + findByPerson(idPerson): List      │
│ + save(at): boolean                 │
│ + delete(id): boolean               │
└──────────────┬──────────────────────┘
               │ <<implements>>
               ▼
┌─────────────────────────────────────┐
│       AssementTaskDAOImpl           │
│─────────────────────────────────────│
│ BASE_QUERY (7 JOINs con alias)      │
│ mapRow() - dos personas distintas   │
│   en la misma fila (creador vs      │
│   asignada)                         │
└─────────────────────────────────────┘
```

---

## Modelo de Dominio (POJOs)

```
┌──────────────────┐       ┌──────────────────┐
│    TypePerson    │       │    StatusTask    │
│──────────────────│       │──────────────────│
│ - idTypePerson   │       │ - idStatusTask   │
│ - typeName       │       │ - statusName     │
│──────────────────│       │ - statusOrder    │
│ + getters/setters│       │──────────────────│
│ + toString()     │       │ + getters/setters│
└────────┬─────────┘       │ + toString()     │
         │ 1               └────────┬─────────┘
         │                          │ 1
         ▼ N                        ▼ N
┌──────────────────┐       ┌──────────────────┐
│     Person       │       │      Task        │
│──────────────────│       │──────────────────│
│ - idPerson       │       │ - idTask         │
│ - firstName      │◄──┐   │ - title          │
│ - lastName       │   │   │ - description    │
│ - email          │   │   │ - createdAt      │
│ - typePerson ────┼───┘   │ - updatedAt      │
│──────────────────│       │ - statusTask ────┼──→ StatusTask
│ + getters/setters│       │ - team ──────────┼──→ Team
│ + toString()     │       │ - createdBy ─────┼──→ Person
└──┬───────────┬───┘       │──────────────────│
   │           │           │ + getters/setters│
   │ 1         │ 1         │ + toString()     │
   ▼ N         ▼ N         └──────────────────┘
┌──────────┐ ┌──────────────────┐
│TeamPerson│ │  AssementTask    │
│──────────│ │──────────────────│
│- team ───┼→│ - task ──────────┼──→ Task
│- person ─┼→│ - person ────────┼──→ Person
│- joinedAt│ │ - roleInTask     │
│──────────│ │ - assignedAt     │
│toString()│ │──────────────────│
└──────────┘ │ toString()       │
             └──────────────────┘
         ▲
         │ 1
┌────────┴────────┐
│      Team       │
│─────────────────│
│ - idTeam        │
│ - teamName      │
│ - description   │
│─────────────────│
│ + getters/setters│
│ + toString()    │
└─────────────────┘
```

---

## Diagrama de Paquetes

```
com.gestor
├── Main                    ← Punto de entrada
├── model                   ← POJOs de dominio
│   ├── TypePerson
│   ├── Person
│   ├── Team
│   ├── TeamPerson
│   ├── StatusTask
│   ├── Task
│   └── AssementTask
├── dao                     ← Interfaces de acceso a datos
│   ├── ConexionDB
│   ├── GenericDAO<T>
│   ├── TypePersonDAO
│   ├── PersonDAO
│   ├── TeamDAO
│   ├── StatusTaskDAO
│   ├── TaskDAO
│   ├── TeamPersonDAO
│   ├── AssementTaskDAO
│   └── impl                ← Implementaciones SQL
│       ├── TypePersonDAOImpl
│       ├── PersonDAOImpl
│       ├── TeamDAOImpl
│       ├── StatusTaskDAOImpl
│       ├── TaskDAOImpl
│       ├── TeamPersonDAOImpl
│       └── AssementTaskDAOImpl
├── service                 ← Lógica de negocio
│   ├── TypePersonService
│   ├── PersonService
│   ├── TeamService
│   ├── TeamPersonService
│   ├── StatusTaskService
│   ├── TaskService
│   └── AssementTaskService
└── gui                     ← Interfaz gráfica Swing
    ├── MainFrame
    ├── PersonasPanel
    ├── EquiposPanel
    └── TareasPanel
```

---

## Diagrama de Componentes

```
┌─────────────────────────────────────────────────┐
│                    GUI Layer                     │
│  ┌────────────┐ ┌────────────┐ ┌──────────────┐ │
│  │ Personas   │ │ Equipos    │ │ Tareas       │ │
│  │ Panel      │ │ Panel      │ │ Panel        │ │
│  └─────┬──────┘ └─────┬──────┘ └──────┬───────┘ │
│        │              │               │          │
│  ┌─────┴──────────────┴───────────────┴───────┐  │
│  │              MainFrame                     │  │
│  │         (CardLayout + Sidebar)             │  │
│  └────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────┘
                       │ usa
┌──────────────────────┴──────────────────────────┐
│                Service Layer                     │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
│  │ Person   │ │ Team     │ │ Task             │ │
│  │ Service  │ │ Service  │ │ Service          │ │
│  └────┬─────┘ └────┬─────┘ └────────┬─────────┘ │
│       │            │                │            │
│  ┌────┴────────────┴────────────────┴─────────┐  │
│  │         Valicación + Reglas de Negocio     │  │
│  └────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────┘
                       │ delega
┌──────────────────────┴──────────────────────────┐
│                  DAO Layer                       │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
│  │ Person   │ │ Team     │ │ Task             │ │
│  │ DAOImpl  │ │ DAOImpl  │ │ DAOImpl          │ │
│  └────┬─────┘ └────┬─────┘ └────────┬─────────┘ │
│       │            │                │            │
│  ┌────┴────────────┴────────────────┴─────────┐  │
│  │           ConexionDB.getConnection()       │  │
│  └────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────┘
                       │ JDBC
┌──────────────────────┴──────────────────────────┐
│                   MySQL                         │
│              gestor_tareas                      │
└─────────────────────────────────────────────────┘
```
