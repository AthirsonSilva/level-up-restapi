output "public_ip" {
  value = aws_ecs_task_definition.task.container_definitions
}

# Output Container ID
output "container_ip" {
  value = aws_ecs_service.service.load_balancer
}

# Output ECR cluster ID
output "cluster_id" {
  value = aws_ecs_cluster.cluster.id
}

output "load_balancer" {
  value = aws_lb.ecs_load_balancer.dns_name
}