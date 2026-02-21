terraform {
  required_providers {
    railway = {
      source  = "railwayapp/railway"
      version = "~> 0.3"
    }
  }
}

provider "railway" {
  token = var.railway_token
}

# Proyecto en Railway
resource "railway_project" "franchise" {
  name = "franchise-api"
}

# Servicio MySQL
resource "railway_service" "mysql" {
  project_id = railway_project.franchise.id
  name       = "mysql"
}

# Variable de entorno con la imagen de MySQL
resource "railway_variable" "mysql_image" {
  project_id      = railway_project.franchise.id
  environment_id  = railway_project.franchise.default_environment.id
  service_id      = railway_service.mysql.id
  name            = "RAILWAY_SERVICE_IMAGE"
  value           = "mysql:8.0"
}

resource "railway_variable" "mysql_root_password" {
  project_id     = railway_project.franchise.id
  environment_id = railway_project.franchise.default_environment.id
  service_id     = railway_service.mysql.id
  name           = "MYSQL_ROOT_PASSWORD"
  value          = var.db_password
}

resource "railway_variable" "mysql_database" {
  project_id     = railway_project.franchise.id
  environment_id = railway_project.franchise.default_environment.id
  service_id     = railway_service.mysql.id
  name           = "MYSQL_DATABASE"
  value          = var.db_name
}
