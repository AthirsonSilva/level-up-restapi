terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
    docker = {
      source = "kreuzwerker/docker"
    }
  }
}

# Define provider and region
provider "aws" {
  region                   = "sa-east-1" # Update with your desired AWS region
  shared_credentials_files = ["~/.aws/credentials"]
  profile                  = "default"
}

# Define provider
provider "docker" {}