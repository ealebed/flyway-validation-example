resource "google_storage_bucket" "default" {
  # checkov:skip=CKV_GCP_62:Access logging not needed for state bucket
  name          = "${var.gcp_project}-flyway-tfstate"
  force_destroy = false
  location      = var.gcp_region
  storage_class = "STANDARD"

  uniform_bucket_level_access = true
  public_access_prevention    = "enforced"

  versioning {
    enabled = true
  }
}
