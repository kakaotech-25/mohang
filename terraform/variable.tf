variable "AWS_ACCESS_KEY_ID" {
  description = "AWS Access Key ID"
  type        = string
}

variable "AWS_SECRET_ACCESS_KEY" {
  description = "AWS Secret Access Key"
  type        = string
}

variable "db_name" {
  description = "value of the database name"
  type        = string
  default     = "moheng"
}

variable "db_username" {
  description = "value of the database user name"
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
