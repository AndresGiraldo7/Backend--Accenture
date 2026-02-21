# Franchise API

REST API reactiva para gestión de franquicias, sucursales y productos. Desarrollada con **Spring Boot 3 + WebFlux + R2DBC + MySQL**.

## Tecnologías

- Java 17
- Spring Boot 3.2
- Spring WebFlux (programación reactiva)
- R2DBC (acceso reactivo a base de datos)
- MySQL 8 (Railway en la nube)
- Flyway (migraciones de BD)
- Docker & Docker Compose

---

## Endpoints

### Franquicias

| Método | Ruta                        | Descripción                     |
|--------|-----------------------------|---------------------------------|
| POST   | `/api/franchises`           | Crear franquicia                |
| PATCH  | `/api/franchises/{id}/name` | Actualizar nombre de franquicia |

### Sucursales

| Método | Ruta                                     | Descripción                   |
|--------|------------------------------------------|-------------------------------|
| POST   | `/api/franchises/{franchiseId}/branches` | Agregar sucursal a franquicia |
| PATCH  | `/api/branches/{branchId}/name`          | Actualizar nombre de sucursal |

### Productos

| Método | Ruta                                              | Descripción                          |
|--------|-------------------------------------------------- |--------------------------------------|
| POST   | `/api/branches/{branchId}/products`               | Agregar producto a sucursal          |
| DELETE | `/api/branches/{branchId}/products/{productId}`   | Eliminar producto                    |
| PATCH  | `/api/products/{productId}/stock`                 | Modificar stock                      |
| PATCH  | `/api/products/{productId}/name`                  | Actualizar nombre de producto        |
| GET    | `/api/franchises/{franchiseId}/products/top-stock`| Producto con mayor stock por sucursal|

## Cómo correr localmente

### Prerrequisitos

- Docker y Docker Compose instalados

### Opción 1 — Docker Compose (recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/franchise-api.git
cd Backend--Accenture

# Levantar todo (MySQL + App)
docker-compose up --build
```

La API estará disponible en `http://localhost:8080`

### Opción 2 — Maven local (requiere MySQL corriendo)

```bash
# Configurar variables de entorno
cp .env.example .env
# Editar .env con tus valores

# Correr
./mvnw spring-boot:run
```

---

## Despliegue en Railway (nube)

1. Crear cuenta en [railway.app](https://railway.app)
2. Crear un nuevo proyecto → Add Service → MySQL
3. Copiar las variables de conexión del panel de Railway
4. Crear otro servicio → Deploy from GitHub repo
5. Agregar las variables de entorno:
   - `DB_HOST`
   - `DB_PORT`
   - `DB_NAME`
   - `DB_USER`
   - `DB_PASSWORD`
6. Railway detectará el `Dockerfile` automáticamente y desplegará la app

---

## Ejemplos de uso

### Crear franquicia

```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "Monald'\''s"}'
```

### Agregar sucursal

```bash
curl -X POST http://localhost:8080/api/franchises/1/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Norte"}'
```

### Agregar producto

```bash
curl -X POST http://localhost:8080/api/branches/1/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Big Mac", "stock": 100}'
```

### Modificar stock

```bash
curl -X PATCH http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": 250}'
```

### Producto con mayor stock por sucursal

```bash
curl http://localhost:8080/api/franchises/1/products/top-stock
```

Respuesta:

```json
[
  {
    "productId": 3,
    "productName": "Big Mac",
    "stock": 250,
    "branchId": 1,
    "branchName": "Sucursal Norte"
  },
  {
    "productId": 7,
    "productName": "McPollo",
    "stock": 180,
    "branchId": 2,
    "branchName": "Sucursal Sur"
  }
]
```

---

## Infrastructure as Code (Terraform)

La base de datos en Railway se puede aprovisionar automáticamente con Terraform.

### Prerrequisitos opcion 2

- [Terraform instalado](https://developer.hashicorp.com/terraform/install)
- Token de Railway: ve a `railway.app > Account Settings > Tokens > New Token`

### Pasos

```bash
 terraform

# Copia el archivo de variables
cp terraform.tfvars.example terraform.tfvars

# Edita terraform.tfvars con tu token de Railway
nano terraform.tfvars

# Inicializa Terraform (descarga el proveedor de Railway)
terraform init

# Previsualiza los recursos que se van a crear
terraform plan

# Aprovisiona la infraestructura
terraform apply
```

> ⚠️ Nunca subas `terraform.tfvars` a Git. Ya está en el `.gitignore`.
