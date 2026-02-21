variable "railway_token" {
  description = "Railway API token. Get it from railway.app > Account Settings > Tokens"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "MySQL root password"
  type        = string
  sensitive   = true
  default     = "UFWxzDhTZLtmEcZEqEbBrdNvjClGViWb"
}

variable "db_name" {
  description = "MySQL database name"
  type        = string
  default     = "railway"
}
