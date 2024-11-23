
output "rds_endpoint" {
  value = module.rds.db_instance_endpoint
}

output "rds_db_name" {
  value = module.rds.db_instance_identifier
}