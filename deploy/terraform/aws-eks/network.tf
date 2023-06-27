// Create a VPC for the EC2 instance to live in
resource "aws_vpc" "demo_vpc" {
  cidr_block           = "10.123.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "dev"
  }
}

// Create a public subnet to distribute the EC2 instance across
resource "aws_subnet" "demo_public_subnet" {
  vpc_id                  = aws_vpc.demo_vpc.id
  cidr_block              = "10.123.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "sa-east-1a"

  tags = {
    Name = "dev-public"
  }
}

// Create a public subnet to distribute the EC2 instance across
resource "aws_subnet" "demo_public_subnet_b" {
  vpc_id                  = aws_vpc.demo_vpc.id
  cidr_block              = "10.123.2.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "sa-east-1b"

  tags = {
    Name = "dev-public-b"
  }
}

// Create a security group to allow inbound traffic
// This will allow SSH access and HTTP traffic
resource "aws_internet_gateway" "demo_internet_gateway" {
  vpc_id = aws_vpc.demo_vpc.id

  tags = {
    Name = "dev-igw"
  }
}

// Create a route table for the public subnet
// This will allow internet access for the EC2 instance
resource "aws_route_table" "demo_public_rt" {
  vpc_id = aws_vpc.demo_vpc.id

  tags = {
    Name = "dev_public_rt"
  }
}

// Create a route table and a public route
// This will allow internet access for the EC2 instance
resource "aws_route" "default_route" {
  route_table_id         = aws_route_table.demo_public_rt.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.demo_internet_gateway.id
}

// Associate the public subnet with the route table
// This associates the route table with the subnet
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.demo_public_subnet.id
  route_table_id = aws_route_table.demo_public_rt.id
}

// Associate the public subnet with the route table
// This associates the route table with the subnet
resource "aws_route_table_association" "public_assoc_b" {
  subnet_id      = aws_subnet.demo_public_subnet_b.id
  route_table_id = aws_route_table.demo_public_rt.id
}