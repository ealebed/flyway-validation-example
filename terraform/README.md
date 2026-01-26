# Prerequisites:

- You will need a [Google Cloud Platform](https://cloud.google.com/) project(s) and a Google Cloud Service Account with enough permissions to manage resources in related GCP project(s).
- You will need to connect GitHub repository to Cloud Build in GCP project.

## Getting started with Google Cloud Platform (optional)

Install the [Google Cloud CLI](https://cloud.google.com/sdk/docs/install-sdk)

### Create project (optional)
```bash
PROJECT_ID=new-project-id
PROJECT_NAME="New project name"

gcloud projects create ${PROJECT_ID} --name=${PROJECT_NAME}
```

If project already exist, you can get `project id`:
```bash
export PROJECT_ID=$(gcloud config get-value project 2> /dev/null)
```
---

### Enable necessary API's in GCP

Enable API
```bash
gcloud services enable \
  serviceusage.googleapis.com \
  servicemanagement.googleapis.com \
  cloudresourcemanager.googleapis.com \
  sqladmin.googleapis.com \
  storage-api.googleapis.com \
  storage.googleapis.com \
  iam.googleapis.com \
  --project ${PROJECT_ID}
```

---

### Create service account

Define Service Account name under environment variable SA_NAME:
```bash
export SA_NAME=sa-terraform
```

Create Service Account:
```bash
gcloud iam service-accounts create ${SA_NAME} \
  --display-name "Terraform Admin Account"
```

### Grant service account necessary roles

Run script `scripts/gcp_sa_role_assignment.sh`:
```bash
bash scripts/gcp_sa_role_assignment.sh
```

---
<details>
<summary style="font-size:14px">Script</summary>
<p>

```bash
#!/usr/bin/env bash

DIR=$( dirname "${BASH_SOURCE[0]}" )
ROLES_LIST=$(cat ${DIR}/${SA_NAME}.roles.list)

for EACH in ${ROLES_LIST}
do
  gcloud projects add-iam-policy-binding ${PROJECT_ID} \
    --member serviceAccount:${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com \
    --role ${EACH}
done
```
</p></details>

---

### Create and download JSON credentials

Define Service Account keyfile name under environment variable SA_KEYFILE_NAME:
```bash
export SA_KEYFILE_NAME=credentials
```

Create and download Service Account Key:
```bash
gcloud iam service-accounts keys create ${SA_KEYFILE_NAME}.json \
  --iam-account ${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com
```

### Activate service account in CLI (optional)

To work with GCP Project from local CLI unders Service account, activate it
```bash
gcloud auth activate-service-account \
  ${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com \
  --key-file=./${SA_KEYFILE_NAME}.json \
  --project=$PROJECT_ID
```

Now you should create Cloud Storage bucket for storing Terraform state and change the backend configuration (`backend.tf` file). Process well-described in this article: [Store Terraform state in a Cloud Storage bucket](https://cloud.google.com/docs/terraform/resource-management/store-state).

Next you can manage (create/update/delete) all other terraform resources.

## Connect GitHub repository to Cloud Build in GCP
- Go to [Cloud Build](https://console.cloud.google.com/cloud-build/triggers) page in your GCP Project.
- Press the button "Connect Repository".
- Choose Github -> Authenticate in it -> select proper GitHub account and repository.
- Mark security agreement checkbox and press "Connect" button.
- Finish this process without creating triggers.

## CI/CD Validation

This repository includes automated validation for Terraform code through GitHub Actions:

### Terraform Validation Workflow

When Terraform files are modified in a pull request, the following checks run automatically:

1. **Terraform Format Check** - Ensures all `.tf` files are properly formatted using `terraform fmt`
2. **Terraform Init** - Initializes Terraform with providers (without backend)
3. **Terraform Validate** - Validates syntax and configuration correctness
4. **Checkov Security Scan** - Scans for security misconfigurations and compliance issues

The validation workflow runs on:
- Pull requests targeting the `master` branch
- Any changes to files in the `terraform/` directory

### Checkov Security Scanning

[Checkov](https://www.checkov.io/) is a static code analysis tool that scans Terraform configurations for security and compliance issues. The workflow uses `bridgecrewio/checkov-action@v12` to automatically scan all Terraform files.

**Skipped Checks:**
- `CKV_SECRET_4` - Passwords are managed via variables, not hardcoded
- `CKV_GCP_55` - PostgreSQL log levels (known false positive)
- `CKV_GCP_109` - PostgreSQL database flags (known false positive)
- `CKV_GCP_125` - GitHub Actions OIDC Trust Policy (check has implementation issues)

To skip additional checks inline in your code, use:
```hcl
# checkov:skip=CKV_GCP_XX:Reason for skipping
```

For more information, see [Checkov documentation](https://www.checkov.io/2.Basics/Suppressing%20and%20Skipping%20Policies.html).

### Running Validation Locally

You can run the same validation checks locally:

```bash
# Format check
terraform fmt -check -recursive

# Format files automatically
terraform fmt -recursive

# Initialize (without backend)
terraform init -backend=false

# Validate
terraform validate
```

For Checkov scanning:
```bash
# Install Checkov
pip install checkov

# Run scan
checkov -d ./terraform --framework terraform
```

## Useful links:
- [Terraform resource samples](https://github.com/terraform-google-modules/terraform-docs-samples)
- [Terraform blueprints catalog](https://cloud.google.com/docs/terraform/blueprints/terraform-blueprints)
- [Best practices for using Terraform](https://cloud.google.com/docs/terraform/best-practices-for-terraform)
- [Google Cloud Platform Provider](https://registry.terraform.io/providers/hashicorp/google/latest/docs)
- [Checkov Documentation](https://www.checkov.io/)


## TODO: 
- Move Terraform state to public [Terraform Cloud server](https://app.terraform.io).
- Configure "native" [Connect from Cloud Build](https://cloud.google.com/sql/docs/postgres/connect-build?authuser=1)
