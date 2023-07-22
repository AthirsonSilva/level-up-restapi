# Output ECS cluster name
output "ecs_cluster_name" {
  value = aws_ecs_cluster.ecs_cluster.name
}

# Output ECS service name
output "ecs_service_name" {
  value = aws_ecs_service.ecs_service.name
}

# Output DNS record name
output "dns_record" {
  value = aws_route53_record.dns_record.name
}

# Output DNS zone ID
output "dns_zone_id" {
  value = aws_route53_zone.dns_zone.zone_id
}

# Output DNS zone name
output "dns_zone_name" {
  value = aws_route53_zone.dns_zone.name
}

output "load_balancer" {
  value = aws_lb.load_balancer.dns_name
}