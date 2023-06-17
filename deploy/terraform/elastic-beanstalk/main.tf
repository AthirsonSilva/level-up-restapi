
/* 
  Create an Elastic Beanstalk application
*/
resource "aws_elastic_beanstalk_application" "beanstalk_myapp" {
  name        = "myapp"
  description = "Java application deployed with Terraform"
}

/* 
  Create an Elastic Beanstalk application version

  The bucket and key attributes are the S3 bucket and object created above
*/
resource "aws_elastic_beanstalk_application_version" "beanstalk_myapp_version" {
  application = aws_elastic_beanstalk_application.beanstalk_myapp.name
  bucket      = aws_s3_bucket.s3_bucket_myapp.id
  key         = aws_s3_object.s3_bucket_object_myapp.id
  name        = "myapp-1.0.0"
}

/* 
  Create an Elastic Beanstalk environment

  The application and version_label attributes are the application and version created above

  The solution_stack_name attribute is the platform to deploy the application to (Java 17)

  The settings attribute is a list of configuration options for the environment
*/
resource "aws_elastic_beanstalk_environment" "beanstalk_myapp_env" {
  name                = "myapp-prod"
  application         = aws_elastic_beanstalk_application.beanstalk_myapp.name
  solution_stack_name = "64bit Amazon Linux 2 v3.1.7 running Corretto 17"
  version_label       = aws_elastic_beanstalk_application_version.beanstalk_myapp_version.name

  setting {
    name      = "SERVER_PORT"
    namespace = "aws:elasticbeanstalk:application:environment"
    value     = "8080"
  }

  setting {
    namespace = "aws:ec2:instances"
    name      = "InstanceTypes"
    value     = "t2.micro"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "aws-elasticbeanstalk-ec2-role"
  }
}