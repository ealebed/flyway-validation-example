# Granular restriction of network access
locals {
  net = ["0.0.0.0/0"]
}

# Create a new Google SQL Instance
resource "google_sql_database_instance" "main" {
  name             = "postgres-instance"
  region           = var.gcp_region
  project          = var.gcp_project
  database_version = "POSTGRES_14"

  settings {
    tier              = "db-f1-micro"
    disk_size         = 10
    availability_type = "ZONAL"
    ip_configuration {
      dynamic "authorized_networks" {
        for_each = local.net
        iterator = net

        content {
          name  = "dmz-${net.key}"
          value = net.value
        }
      }
    }
  }
}

# Create a new Google SQL Users on a Google SQL Instance
resource "google_sql_user" "flyway" {
  name     = "flyway"
  instance = google_sql_database_instance.main.name
  password = "changeme"
}

resource "google_sql_user" "test" {
  name     = "test"
  instance = google_sql_database_instance.main.name
  password = "test"
}

resource "google_sql_user" "ro_user" {
  name     = "ro_user"
  instance = google_sql_database_instance.main.name
  password = "123321"
}

# Create SQL database(s) inside the Cloud SQL Instance
resource "google_sql_database" "database_01" {
  name     = "database_01"
  instance = google_sql_database_instance.main.name
}

resource "google_sql_database" "database_02" {
  name     = "database_02"
  instance = google_sql_database_instance.main.name
}
