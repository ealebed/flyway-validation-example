resource "google_storage_bucket" "default" {
  name          = "${var.gcp_project}-flyway-tfstate"
  force_destroy = true
  location      = var.gcp_region
  storage_class = "STANDARD"
  versioning {
    enabled = true
  }
}
