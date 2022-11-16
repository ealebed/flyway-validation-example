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
  }
}

# Create a new Google SQL User on a Google SQL Instance
resource "google_sql_user" "users" {
  name     = "flyway"
  instance = google_sql_database_instance.main.name
  password = "changeme"
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
