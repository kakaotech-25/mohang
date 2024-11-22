
# RDS 모듈 설정
module "rds" {
  source  = "terraform-aws-modules/rds/aws"
  version = "6.10.0"

  identifier     = "moheng-db"
  engine         = "mysql"
  engine_version = "8.0"
  port           = 3306

  major_engine_version = var.major_engine_version
  family               = var.family

  instance_class    = "db.t3.medium"
  allocated_storage = 20

  db_name  = "moheng"
  username = var.db_username
  password = var.db_password

  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  subnet_ids             = module.vpc.private_subnets

  publicly_accessible = false
  skip_final_snapshot = true

  tags = {
    Name = "moheng-db"
  }
}

variable "db_username" {
  description = "value of the database name"
  type        = string
}

variable "db_password" {
  description = "value of the database password"
  type        = string
}

variable "family" {
  description = "The family of the DB parameter group (e.g., mysql8.0)"
  type        = string
  default     = "mysql8.0"
}

variable "major_engine_version" {
  description = "The major version number of the database engine (e.g., 8.0)"
  type        = string
  default     = "8.0"
}
