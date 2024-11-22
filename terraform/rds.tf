
# RDS 모듈 사용
module "rds" {
  source  = "terraform-aws-modules/rds/aws"
  version = "6.10.0"

  identifier     = "moheng-db"
  engine         = "mysql"
  engine_version = "8.0"

  instance_class    = "db.t3.medium"
  allocated_storage = 20

  db_name  = "moheng"
  username = var.db_username
  password = var.db_password

  vpc_security_group_ids = [aws_security_group.web_sg.id]
  subnet_ids             = module.vpc.private_subnets

  publicly_accessible = false
  skip_final_snapshot = true

  tags = {
    Name = "moheng-db"
  }
}

variable "db_username" {
  description = "데이터베이스 사용자 이름"
  type        = string
}

variable "db_password" {
  description = "데이터베이스 비밀번호"
  type        = string
  sensitive   = true
}
