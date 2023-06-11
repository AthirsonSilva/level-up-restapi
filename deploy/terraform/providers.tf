/* 
  This file is used to configure the AWS provider for Terraform. 
  It is used to specify the region and the profile to use. 
  The profile is used to specify the AWS credentials to use. 
  The credentials are stored
*/
terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  region                   = "us-east-1"
  shared_credentials_files = ["~/.aws/credentials"]
  profile                  = "default"
}