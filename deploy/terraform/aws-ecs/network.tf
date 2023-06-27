resource "aws_vpc" "main" {
  cidr_block = var.vpc_cidr[0]

  tags = {
    Name = "main"
  }
}

// Create a public subnet to distribute the EC2 instance across
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = var.vpc_cidr[1]
  map_public_ip_on_launch = true
  availability_zone       = var.availability_zones[0]

  tags = {
    Name = "dev-public"
  }
}

// Create a public subnet to distribute the EC2 instance across
resource "aws_subnet" "public_subnet_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = var.vpc_cidr[2]
  map_public_ip_on_launch = true
  availability_zone       = var.availability_zones[1]

  tags = {
    Name = "dev-public-b"
  }
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "igw"
  }
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "public-route-table"
  }
}

resource "aws_route_table_association" "public_subnet_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}