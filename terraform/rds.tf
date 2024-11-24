# RDS 모듈 설정
module "rds" {
  source  = "terraform-aws-modules/rds/aws"
  version = "6.10.0"

  # 기존 설정 유지
  snapshot_identifier = "database-moheng-snapshot"
  identifier          = "moheng-db"
  engine              = "mysql"
  engine_version      = "8.0"
  port                = 3306

  major_engine_version = var.major_engine_version
  family               = var.family

  instance_class = "db.t3.large"

  allocated_storage = 20


  # 데이터베이스 변수
  db_name  = var.db_name
  username = var.db_username
  password = var.db_password

  create_db_subnet_group = true
  subnet_ids             = module.vpc.private_subnets
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  availability_zone      = module.vpc.azs[0]

  publicly_accessible = false
  skip_final_snapshot = true

  tags = {
    Name = "moheng-db"
  }
}

