/* 
  Create an S3 bucket access control list (ACL)
*/
resource "aws_s3_bucket_acl" "s3_bucket_acl_myapp" {
  bucket = "myapp-prod"
  acl    = "private"
}

/* 
  Create an S3 bucket to store the application version
*/
resource "aws_s3_bucket" "s3_bucket_myapp" {
  bucket = "myapp-prod"
  acl    = aws_s3_bucket_acl.s3_bucket_acl_myapp.id
}

/* 
  Upload the application version to the S3 bucket
  
  The source attribute is the path to the application version
*/
resource "aws_s3_object" "s3_bucket_object_myapp" {
  bucket = aws_s3_bucket.s3_bucket_myapp.id
  key    = "beanstalk/next-spring-2.0.0.jar"
  source = "../../../target/next-spring-2.0.0.jar"
}
