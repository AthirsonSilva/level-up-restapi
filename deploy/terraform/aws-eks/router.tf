resource "aws_route53_zone" "dns_zone" {
  name = "itm_marketing_demo.com"
}

resource "aws_route53_record" "dns_record" {
  zone_id = aws_route53_zone.dns_zone.zone_id
  name    = "itm_marketing_demo"
  type    = "A"

  alias {
    name                   = aws_lb.load_balancer.dns_name
    zone_id                = aws_lb.load_balancer.zone_id
    evaluate_target_health = true
  }
}