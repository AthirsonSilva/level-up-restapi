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
  instance_type          = "t2.micro"                             // This is the instance type that we want to use for the EC2 instance
  ami                    = data.aws_ami.next_api_server_ami.id    // This is the AMI ID that we want to use for the EC2 instance
  key_name               = aws_key_pair.next_api_keys.id          // This is the key pair that we want to use for the EC2 instance
  vpc_security_group_ids = [aws_security_group.next_api_sg.id]    // This is the security group that we want to use for the EC2 instance
  subnet_id              = aws_subnet.next_api_public_subnet.id   // This is the subnet that we want to use for the EC2 instance
  user_data              = file("./templates/docker-userdata.sh") // This is the user data that we want to use for the EC2 instance

  // This is the root block device that we want to use for the EC2 instance
  root_block_device {
    volume_size = 8
  }

  tags = {
    "Name" = "next_api_ec2_inst"
  }

  /* 
    The provisioner block will execute the script to create the SSH config file
    in the EC2 instance created above.
  */
  provisioner "local-exec" {
    command = templatefile("./templates/ssh-config.tpl", {
      hostname     = self.public_ip,
      user         = "ubuntu",
      identityfile = "~/.ssh/next_api_key"
    })
    interpreter = ["bash", "-c"]
  }
}
