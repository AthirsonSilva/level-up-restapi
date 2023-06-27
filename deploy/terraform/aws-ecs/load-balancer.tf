# Create a AWS Load balancer
# This load balancer will be used by the ECS service
resource "aws_lb" "ecs_load_balancer" {
  name               = "my-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.ecs_sg.id]
  subnets            = [aws_subnet.public_subnet.id, aws_subnet.public_subnet_b.id]
}

# Create a AWS Load balancer target group
# This target group will be used by the load balancer
resource "aws_lb_target_group" "ecs_lb_target_group" {
  name        = "my-lb-target-group"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "ip"

  health_check {
    path                = "/"
    protocol            = "HTTP"
    matcher             = "200-399"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 3
    unhealthy_threshold = 3
  }
}

# Create a AWS Load balancer listener rule
# This rule will forward all the traffic to the target group
resource "aws_lb_listener" "ecs_http_listener" {
  load_balancer_arn = aws_lb.ecs_load_balancer.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.ecs_lb_target_group.arn
  }
}