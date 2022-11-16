# Flyway connection URL
output "sql_ip_addr" {
  value = "jdbc:postgresql://${google_sql_database_instance.main.public_ip_address}:5432/${google_sql_database.database_01.name}"
}
