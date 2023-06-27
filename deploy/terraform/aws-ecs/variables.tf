# Provider configuration variables
variable "aws_region" {
  description = "AWS region to use"
  type        = string
  default     = "sa-east-1"
}

# VPC configuration variables
variable "vpc_cidr" {
  description = "CIDR block for main VPC"
  type        = list(string)
  default     = ["10.123.0.0/16", "10.123.1.0/24", "10.123.2.0/24"]
}

# Availability zones configuration variable
variable "availability_zones" {
  description = "AWS availability zones to use"
  type        = list(string)
  default     = ["sa-east-1a", "sa-east-1b"]
}