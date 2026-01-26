resource "google_cloudbuild_trigger" "default" {
  location = "global"

  // If this is set on a build, it will become pending when it is run,
  // and will need to be explicitly approved to start.
  approval_config {
    approval_required = true
  }

  include_build_logs = "INCLUDE_BUILD_LOGS_WITH_STATUS"
  name               = "apply-flyway-migrations"

  github {
    owner = var.github.organization
    name  = var.github.repo
    push {
      branch = "^master$"
    }
  }

  substitutions = {
    # JDBC url to use to connect to the database
    # Example PostgreSQL: jdbc:postgresql://<host>:<port>/<database>?<key1>=<value1>&<key2>=<value2>...
    _FLYWAY_URL = "jdbc:postgresql://${google_sql_database_instance.main.public_ip_address}:5432/${google_sql_database.database_01.name}"
    # User to use to connect to the database
    _FLYWAY_USER = google_sql_user.flyway.name
    # Password to use to connect to the database
    _FLYWAY_PASSWORD = google_sql_user.flyway.password
  }

  filename = "cloudbuild.yaml"
}
