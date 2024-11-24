# VPC 모듈 사용
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.16.0"

  name = "moheng"
  cidr = "192.168.0.0/24" # VPC 1의 CIDR 블록

  azs                  = ["ap-northeast-2a", "ap-northeast-2b"]
  public_subnets       = ["192.168.0.0/26", "192.168.0.128/26"]
  private_subnets      = ["192.168.0.64/26", "192.168.0.192/26"]
  enable_dns_hostnames = true
  enable_dns_support   = true
}

# 보안 그룹 생성
resource "aws_security_group" "web_sg" {
  name        = "web-sg"
  description = "SG for Web Servers"
  vpc_id      = module.vpc.vpc_id
  tags = {
    Name = "moheng-vpc"
  }

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1" # 모든 프로토콜
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "rds-sg"
  description = "Security Group for RDS"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description = "Allow MySQL from EC2"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [
      aws_security_group.web_sg.id # EC2 인스턴스의 보안 그룹 ID
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1" # All protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "moheng-vpc"
  }
}
