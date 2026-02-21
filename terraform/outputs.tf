output "project_id" {
  description = "Railway project ID"
  value       = railway_project.franchise.id
}

output "mysql_service_id" {
  description = "Railway MySQL service ID"
  value       = railway_service.mysql.id
}
