output "inst_ip" {
  value = aws_instance.spring_ec2_inst.public_ip
}

output "inst_dns" {
  value = aws_instance.spring_ec2_inst.public_dns
}

