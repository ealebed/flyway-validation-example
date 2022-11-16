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

## Useful links:
- [Terraform resource samples](https://github.com/terraform-google-modules/terraform-docs-samples)
- [Terraform blueprints catalog](https://cloud.google.com/docs/terraform/blueprints/terraform-blueprints)
- [Best practices for using Terraform](https://cloud.google.com/docs/terraform/best-practices-for-terraform)
- [Google Cloud Platform Provider](https://registry.terraform.io/providers/hashicorp/google/latest/docs)


## TODO: 
- Move Terraform state to public [Terraform Cloud server](https://app.terraform.io).
- Configure "native" [Connect from Cloud Build](https://cloud.google.com/sql/docs/postgres/connect-build?authuser=1)
