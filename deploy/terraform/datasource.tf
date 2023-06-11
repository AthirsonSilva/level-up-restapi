/* 
  Datasource is a read-only data source that fetches data from an external source.
  In this case, we are fetching the latest AMI ID from AWS.
*/
data "aws_ami" "next_api_server_ami" {
  most_recent = true             # This will fetch the latest AMI ID
  owners      = ["099720109477"] # This is the owner ID of Canonical

  filter {
    name   = "name"                                                      # This is the name of the AMI
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"] # This is the name of the AMI that we want to fetch
  }
}