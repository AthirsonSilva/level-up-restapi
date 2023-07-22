terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
    }
    aws = {
      source  = "hashicorp/aws"
    }
  }
}

# Configuring AWS as the provider
provider "aws" {
  region                   = var.aws_region
  shared_credentials_files = ["~/.aws/credentials"]
}

# Configuring Docker as the provider
provider "docker" {}
