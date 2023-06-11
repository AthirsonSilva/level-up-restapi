resource "aws_vpc" "next_api_vpc_1" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    "Name" = "next_api_vpc_1"
  }
}


resource "aws_subnet" "next_api_subnet_pub_1a" {
  vpc_id                  = aws_vpc.next_api_vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name" = "next_api_subnet_pub_1a"
  }

}

resource "aws_internet_gateway" "next_api_igw_1a" {
  vpc_id = aws_vpc.next_api_vpc_1.id

  tags = {
    "Name" = "next_api_igw_1a"
  }
}
/* 
  Route table is used to route traffic from the subnet to the internet gateway.
  This is done by creating a route in the route table that points to the internet gateway.
*/
resource "aws_route_table" "next_api_rtb_pub" {
  vpc_id = aws_vpc.next_api_vpc_1.id // This is the VPC ID that we want to associate the route table with

  tags = {
    "Name" = "next_api_rtb_pub"
  }
}

/* 
  The route is used to route traffic from the subnet to the internet gateway.
  This is done by creating a route in the route table that points to the internet gateway.

  The difference between the route and the route table is that the route table is used to route traffic from the subnet to the internet gateway.
*/
resource "aws_route" "next_api_default_rtb" {
  route_table_id         = aws_route_table.next_api_rtb_pub.id     // This is the route table ID that we want to associate the route with
  destination_cidr_block = "0.0.0.0/0"                             // This is the destination CIDR block that we want to route to
  gateway_id             = aws_internet_gateway.next_api_igw_1a.id // This is the internet gateway ID that we want to route to
}

/* 
  The route table association is used to associate the subnet with the route table.
  This is done by creating a route table association in the route table that points to the subnet.
*/
resource "aws_route_table_association" "next_api_rtba_pub_1a" {
  route_table_id = aws_route_table.next_api_rtb_pub.id  // This is the route table ID that we want to associate the subnet with
  subnet_id      = aws_subnet.next_api_subnet_pub_1a.id // This is the subnet ID that we want to associate with the route table
}

/* 
  Finally, the AWS EC2 instance is created.
  This is done by creating an EC2 instance in the subnet.
  The EC2 instance is created using the AMI ID that we fetched in the previous step.
  The EC2 instance is also created using the key pair that we created in the previous step.
  The EC2 instance is also created using the security group that we created in the previous step.
  The EC2 instance is also created using the subnet that we created in the previous step.
  The EC2 instance is also created using the user data that we created in the previous step.
*/
resource "aws_instance" "next_api_ec2_inst" {
  instance_type          = "t2.micro"                           // This is the instance type that we want to use for the EC2 instance
  ami                    = data.aws_ami.next_api_server_ami.id  // This is the AMI ID that we want to use for the EC2 instance
  key_name               = aws_key_pair.next_api_key.id         // This is the key pair that we want to use for the EC2 instance
  vpc_security_group_ids = [aws_security_group.next_api_sg.id]  // This is the security group that we want to use for the EC2 instance
  subnet_id              = aws_subnet.next_api_subnet_pub_1a.id // This is the subnet that we want to use for the EC2 instance
  user_data              = file("userdata.tpl")                 // This is the user data that we want to use for the EC2 instance

  // This is the root block device that we want to use for the EC2 instance
  root_block_device {
    volume_size = 8
  }

  tags = {
    "Name" = "next_api_ec2_inst"
  }
}
