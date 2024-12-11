#!/bin/bash

gcloud services enable artifactregistry.googleapis.com cloudbuild.googleapis.com run.googleapis.com

gcloud artifacts repositories create hotrip-recommendation-system-be --repository-format=docker --location=asia-southeast2 --async

gcloud builds submit --tag asia-southeast2-docker.pkg.dev/your-travel-441614/hotrip-recommendation-system-be/hotrip-recommendation

gcloud run deploy --image asia-southeast2-docker.pkg.dev/your-travel-441614/hotrip-recommendation-system-be/hotrip-recommendation