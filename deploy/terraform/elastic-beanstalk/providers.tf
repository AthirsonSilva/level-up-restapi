terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  region                   = "sa-east-1"
  shared_credentials_files = ["~/.aws/credentials"]
  profile                  = "default"
}
