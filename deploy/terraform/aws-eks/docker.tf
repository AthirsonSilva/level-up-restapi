# Create an ECS cluster
# This cluster will run the ECS service
resource "aws_ecs_cluster" "ecs_cluster" {
  name = "my-ecs-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

# Create an ECS task definition
# This task definition will run the docker container created above
resource "aws_ecs_task_definition" "ecs_task_definition" {
  family                   = "service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE", "EC2"]
  cpu                      = 512
  memory                   = 2048

  container_definitions = jsonencode([
    {
      name : "nginx",
      image : "nginx:latest",
      cpu : 512,
      memory : 2048,
      essential : true,
      portMappings : [
        {
          containerPort : 80,
          hostPort : 80,
        },
      ],
    },
  ])
}

# Create an ECS service
# This service will run the task definition created above
resource "aws_ecs_service" "ecs_service" {
  name             = "my-ecs-service"
  cluster          = aws_ecs_cluster.ecs_cluster.id
  task_definition  = aws_ecs_task_definition.ecs_task_definition.arn
  desired_count    = 1
  launch_type      = "FARGATE"
  platform_version = "LATEST"

  network_configuration {
    assign_public_ip = true
    security_groups  = [aws_security_group.security_group.id]
    subnets          = [aws_subnet.demo_public_subnet.id]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.lb_target_group.arn
    container_name   = "nginx"
    container_port   = 80
  }

  lifecycle {
    ignore_changes = [task_definition]
  }
}

