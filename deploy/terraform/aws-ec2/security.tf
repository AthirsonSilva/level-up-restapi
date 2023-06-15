/* 
  This resource is used to create a VPC in AWS.
  The VPC is used to create a private network in AWS that is isolated from other networks.
*/
resource "aws_security_group" "next_api_sg" {
  name        = "next_api_sg"             // This is the name of the security group.
  description = "next_api security group" // This is the description of the security group.
  vpc_id      = aws_vpc.next_api_vpc.id   // This is the ID of the VPC.

  tags = {
    "Name" = "next_api_sg"
  }
}

/* 
  This resource is used to create a security group rule that allows all outbound traffic.
  This is required for the EC2 instance to be able to communicate with the internet.
*/
resource "aws_security_group_rule" "public_out" {
  type        = "egress"      // This is the type of traffic that is allowed. In this case, it is outbound traffic.
  from_port   = 0             // This is the port that the traffic is coming from. In this case, it is all ports.
  to_port     = 0             // This is the port that the traffic is going to. In this case, it is all ports.
  protocol    = "-1"          // This is the protocol that the traffic is using. In this case, it is all protocols.
  cidr_blocks = ["0.0.0.0/0"] // This is the IP address range that the traffic is coming from. In this case, it is all IP addresses.

  // This is the ID of the security group.
  security_group_id = aws_security_group.next_api_sg.id
}

/* 
  This resource is used to create a security group rule that allows SSH traffic from anywhere.
  This is required for the EC2 instance to be able to be accessed via SSH.
*/
resource "aws_security_group_rule" "public_in_ssh" {
  type              = "ingress"                         // This is the type of traffic that is allowed. In this case, it is inbound traffic.
  from_port         = 22                                // This is the port that the traffic is coming from. In this case, it is port 22.
  to_port           = 22                                // This is the port that the traffic is going to. In this case, it is port 22.
  protocol          = "tcp"                             // This is the protocol that the traffic is using. In this case, it is TCP.
  cidr_blocks       = ["0.0.0.0/0"]                     // This is the IP address range that the traffic is coming from. In this case, it is all IP addresses.
  security_group_id = aws_security_group.next_api_sg.id // This is the ID of the security group.
}

/* 
  This resource is used to create a security group rule that allows HTTP traffic from anywhere.
  This is required for the EC2 instance to be able to be accessed via HTTP.
*/
resource "aws_security_group_rule" "public_in_http" {
  type              = "ingress"                         // This is the type of traffic that is allowed. In this case, it is inbound traffic.
  from_port         = 80                                // This is the port that the traffic is coming from. In this case, it is port 80.
  to_port           = 80                                // This is the port that the traffic is going to. In this case, it is port 80.
  protocol          = "tcp"                             // This is the protocol that the traffic is using. In this case, it is TCP.
  cidr_blocks       = ["0.0.0.0/0"]                     // This is the IP address range that the traffic is coming from. In this case, it is all IP addresses.
  security_group_id = aws_security_group.next_api_sg.id // This is the ID of the security group.
}

/* 
  This resource is used to create a security group rule that allows HTTPS traffic from anywhere.
  This is required for the EC2 instance to be able to be accessed via HTTPS.
*/
resource "aws_security_group_rule" "public_in_https" {
  type              = "ingress"                         // This is the type of traffic that is allowed. In this case, it is inbound traffic.s
  from_port         = 443                               // This is the port that the traffic is coming from. In this case, it is port 443.
  to_port           = 443                               // This is the port that the traffic is going to. In this case, it is port 443.
  protocol          = "tcp"                             // This is the protocol that the traffic is using. In this case, it is TCP.
  cidr_blocks       = ["0.0.0.0/0"]                     // This is the IP address range that the traffic is coming from. In this case, it is all IP addresses.
  security_group_id = aws_security_group.next_api_sg.id // This is the ID of the security group.
}

