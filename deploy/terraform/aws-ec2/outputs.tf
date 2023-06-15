/* 
  This file is used to output the public IP of the EC2 instance
  so that it can be used in the next step of the pipeline.
*/
output "inst_ip" {
  value = aws_instance.next_api_ec2_inst.public_ip
}

output "inst_dns" {
  value = aws_instance.next_api_ec2_inst.public_dns
}