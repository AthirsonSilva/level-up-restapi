// Create a TLS private key for the EC2 instance
resource "tls_private_key" "next_api_tls" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

// Create a key pair for the EC2 instance using the TLS private key
resource "aws_key_pair" "next_api_keys" {
  key_name   = "next_api_key"
  public_key = tls_private_key.next_api_tls.public_key_openssh
}
