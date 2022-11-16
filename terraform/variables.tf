variable "gcp_project" {
  type        = string
  description = "GCP project name"
  default     = "ylebi-rnd"
}

variable "gcp_region" {
  type        = string
  description = "GCP region name"
  default     = "europe-west1"
}

variable "gcp_zone" {
  type        = string
  description = "GCP zone name"
  default     = "europe-west1-b"
}

variable "github" {
  description = "GitHub info"
  default = {
    organization = "ealebed"
    repo         = "flyway-validation-example"
  }
}
