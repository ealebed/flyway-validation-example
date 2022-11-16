## Overview

All changes to the database are called **migrations**. Migrations can be either *versioned* or *repeatable*.

**Versioned migrations** have a *version*, a *description* and a *checksum*. The version must be unique. The description is purely informative for you to be able to remember what each migration does. The checksum is there to detect accidental changes. Versioned migrations are the most common type of migration. They are applied in order exactly once.

**Repeatable migrations** have a *description* and a *checksum*, but no version. Instead of being run just once, they are (re-)applied every time their checksum changes. Within a single migration run, repeatable migrations are always applied last, after all pending versioned migrations have been executed. Repeatable migrations are applied in the order of their description.

By default both versioned and repeatable migrations can be written either in **SQL** or in **Java** and can consist of multiple statements.

To keep track of which migrations have already been applied when and by whom, Flyway adds a *schema history* table to your schema.

### Versioned Migrations

**Versioned migrations** are typically used for:

+ Creating/altering/dropping tables/indexes/foreign keys/enums/UDTs/…
+ Reference data updates
+ User data corrections

Here is a small example:

```postgres-sql
CREATE TABLE hero_data.hero
(
    id BIGSERIAL NOT NULL,
    name VARCHAR(250) NOT NULL,
    description TEXT NOT NULL,
    debut_year INT NOT NULL,
    appearances INT NOT NULL,
    special_powers INT NOT NULL,
    cunning INT NOT NULL,
    strength INT NOT NULL,
    technology INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

ALTER TABLE hero_data.hero ADD CONSTRAINT pk_hero_id PRIMARY KEY (id);
```

### Repeatable Migrations

**Repeatable migrations** are typically used for:
                      
+ (Re-)creating views/procedures/functions/packages/…
+ Bulk reference data reinserts

Here is an example of what a repeatable migration looks like:

```postgres-sql
CREATE OR REPLACE VIEW blue_cars AS 
    SELECT id, license_plate FROM cars WHERE color='blue';
```

### SQL-based migrations, naming 
Migrations are most commonly written in **SQL**. This makes it easy to get started and leverage any existing scripts, tools and skills. It gives you access to the full set of capabilities of your database and eliminates the need to understand any intermediate translation layer.
                                                 
SQL-based migrations are typically used for:
                                                 
+ DDL changes (CREATE/ALTER/DROP statements for TABLES,VIEWS,TRIGGERS,SEQUENCES,…)
+ Simple reference data changes (CRUD in reference data tables)
+ Simple bulk data changes (CRUD in regular data tables)

In order to be picked up by Flyway, SQL migrations must comply with the following naming pattern.

![versioned_migration](https://user-images.githubusercontent.com/8081901/44097595-ccc03b94-9fe6-11e8-8003-cc87ad98524a.png)

The file name consists of the following parts:

+ **Prefix:** `V` for versioned (configurable) and `R` for repeatable migrations (configurable)
+ **Version:** Version with dots or underscores separate as many parts as you like (Not for repeatable migrations). **In our case VERSION = timestamp, in format 20180814141830**. To get formatted timestamp run command `date '+%Y%m%d%H%M%S'`
+ **Separator:** `__` (two underscores) (configurable)
+ **Description:** Underscores or spaces separate the words
+ **Suffix:** `.sql` (configurable)

Migrations can be applied for all needed databases. For each database you should create separate directory, containing migrations, e.g.:

```bash
.
├── database_01
│   ├── V20210203000000__Base_version.sql
│   ├── V20210203150726__create_traces_table.sql
│   ├── V20210205103852__grants_for_ro_user.sql
│   └── V20210205110053__create_invoices_table.sql
└── database_02
   ├── V20210201000000__Base_version.sql
   ├── V20210201102112__added_filters_column_to_scheduled_reports_table.sql
   └── ...
```

For understanding existing database setup read this [doc](https://flywaydb.org/documentation/learnmore/existing.html). In our case, first we should:

+ create sql script that includes the entire DDL (including indexes, triggers, procedures, ...) of the database (for example with `pg_dump -s ...`)
+ create base migration from sql script. Give it a relevant version number and description (for example `V20210201000000__Base_version.sql`)
+ give database a baseline version (use version and description from previous step). To do this, run command in docker container:
```bash
docker run \
  --rm
  -e FLYWAY_USER=${FLYWAY_USER} \
  -e FLYWAY_PASSWORD=${FLYWAY_PASSWORD} \
  -e FLYWAY_URL=${FLYWAY_URL} \
  flyway/flyway:9.8.1 baseline -baselineVersion=20210201000000 -baselineDescription="Base version"
```

Now we can create migrations and apply them to needed database. I recommend use for creation plugin [Flyway Migration Creation](https://plugins.jetbrains.com/plugin/8597-flyway-migration-creation).

For applying migrations run command in docker container:

```bash
docker run \
  --rm \
  -v <path_to_database_directory>:/flyway/sql \
  -e FLYWAY_USER=${FLYWAY_USER} \
  -e FLYWAY_PASSWORD=${FLYWAY_PASSWORD} \
  -e FLYWAY_URL=${FLYWAY_URL} \
  flyway/flyway:9.8.1 migrate
```

View details and status information about all the migrations:

```bash
docker run \
  --rm \
  -e FLYWAY_USER=${FLYWAY_USER} \
  -e FLYWAY_PASSWORD=${FLYWAY_PASSWORD} \
  -e FLYWAY_URL=${FLYWAY_URL} \
  flyway/flyway:9.8.1 info
```

:warning::warning::warning:

To avoid table locking, you should (sometimes) write migrations in the following format (use this wisely):

```sql
-- Revoke connection privilege to users
REVOKE CONNECT ON DATABASE database_01 FROM PUBLIC, apireports, clickhouse;

-- Close all active connections to db
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE
  -- don't kill my own connection!
    pid <> pg_backend_pid()
  -- don't kill the connections to other databases
  AND datname = 'database_01';

-- Your migration(s)
alter table public.statistic add column if not exists io_budget int8;
alter table public.statistic add column if not exists io_budget_local int8;
alter table public.statistic add column if not exists io_impressions int4;

-- Grants connection privilege to users from first step
GRANT CONNECT ON DATABASE database_01 TO PUBLIC, apireports, clickhouse;
```

For Terraform configuration details refer proper [README.md](./terraform/README.md)
