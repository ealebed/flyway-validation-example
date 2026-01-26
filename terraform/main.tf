terraform {
  required_version = "~> 1.0"

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }
  }
}

provider "google" {
  credentials = file("../credentials.json")

  project = var.gcp_project
  region  = var.gcp_region
  zone    = var.gcp_zone
}

data "google_project" "project" {}
