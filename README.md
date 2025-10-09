# Inventory Management Backend

## Overview
This is a Spring Boot backend application for managing inventory.  
It provides REST APIs to handle products, increasing or decreasing stock quantity of the products, and audit logs.

**Tech Stack:** Java, Spring Boot, MySQL

## Features
- **Product Management:** Add, update, delete, and fetch products.
- **Stock Management:** Increase or decrease product stock quantity.
- **Low Stock Alerts:** Fetch products that are below a specified stock threshold.
- **Audit Logs:** Track all stock changes with pagination and product-specific queries based on the number of increased or decreased quantity.
- **Swagger Documentation:** Explore APIs interactively and if want to test API's in postman, we can also get the required `curl` from swagger doc itself.
- **Profiles implementation:** This project also contains a simple demo of the profiles, through which we can actually change the environment in which the application is running. But right now it just changes the server ports for a demo purpose.
- **Backend validations:** Validations for the request body is also implemented from backend using Spring validations.
- **User and Order API's are present to enhance the Inventrory Management Demonestration.

## Setup and Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/Supppiee/InventoryManagement-backend.git
cd InventoryManagement-backend/inventory
```

## 2. Setup MySQL database
I have added the dependencies of MySQL, So please connect the MySQL database config on application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```
## 3.Build and run project
```
./mvnw clean install
./mvnw spring-boot:run
```
The server will start at: http://localhost:8081 as I have used 8081 as server port.

## Access Swagger documentation
You can access the swagger doc of the whole backend using ->
http://localhost:8081/swagger-ui/index.html#/

## Folder structure
```
inventoryManagement/
└── inventory/
    ├── .idea/                         
    ├── .mvn/                          
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/im/inventory/
    │   │   │       ├── config/        # Configuration classes 
    │   │   │       ├── constants/     # Application constants
    │   │   │       ├── controller/    # REST API controllers
    │   │   │       ├── dto/           # Data Transfer Objects (Requests & Responses)
    │   │   │       ├── entity/        # JPA entities mapped to DB tables
    │   │   │       ├── exceptions/    # Custom exception handlers and classes
    │   │   │       ├── repository/    # Spring Data JPA repositories
    │   │   │       ├── service/       # Service layer (business logic)
    │   │   │       └── InventoryApplication.java  # Main Spring Boot entry point
    │   │   └── resources/
    │   │       ├── static/            # Static resources (if any)
    │   │       ├── templates/         # HTML templates (if using Thymeleaf)
    │   │       ├── application.properties
    │   │       ├── application-dev.properties
    │   │       └── application-test.properties
    │   └── test/                      # Unit and integration tests
    ├── target/                        # Compiled files (auto-generated)
    ├── .gitignore                     # Git ignore configuration
    ├── .gitattributes                 # Git attributes for line endings
    ├── HELP.md                        # Optional Maven help file
    └── mvnw                           # Maven wrapper script

```


# API Endpoints
## Product APIs
| Method     | Endpoint                          | Description                  | Body / Params                                                     |
| ---------- | --------------------------------- | ---------------------------- | ----------------------------------------------------------------- |
| **POST**   | `/api/product`                    | Create a new product         | JSON: `name`, `description`, `stockQuantity`, `lowStockThreshold` |
| **GET**    | `/api/product/{id}`               | Get product by ID            | Path Variable: `id`                                               |
| **PUT**    | `/api/product/{id}`               | Update product               | Path Variable: `id`, JSON body same as create                     |
| **DELETE** | `/api/product/{id}`               | Delete product               | Path Variable: `id`                                               |
| **PATCH**  | `/api/product/{id}/increaseStock` | Increase stock               | Query Param: `quantity`                                           |
| **PATCH**  | `/api/product/{id}/decreaseStock` | Decrease stock               | Query Param: `quantity`                                           |
| **GET**    | `/api/product/lowStock`           | Get low stock products       | None                                                              |
| **GET**    | `/api/product/all`                | Get all products (paginated) | Query Params: `page`, `size`, `sortBy`, `sortDir`                 |


## Audit API
| Method  | Endpoint                                 | Description                 | Params                                                   |
| ------- | ---------------------------------------- | --------------------------- | -------------------------------------------------------- |
| **GET** | `/api/auditQuantity`                     | Get all stock transactions  | Query Params: `page`, `size`, `sortBy`, `sortDir`        |
| **GET** | `/api/auditQuantity/product/{productId}` | Get transactions by product | Path Variable: `productId`, Query Params: `page`, `size` |


## User API
| Method   | Endpoint     | Description       | Body / Params               |
| -------- | ------------ | ----------------- | --------------------------- |
| **POST** | `/api/users` | Create a new user | JSON: `name`, `email`, etc. |
| **GET**  | `/api/users` | Get all users     | None                        |

## Order API 
| Method    | Endpoint                    | Description                       | Body / Params                                   |
| --------- | --------------------------- | --------------------------------- | ----------------------------------------------- |
| **POST**  | `/api/orders/place`         | Place a new order                 | Query Params: `userId`, `productId`, `quantity` |
| **PATCH** | `/api/orders/{id}/cancel`   | Cancel an existing order          | Path Variable: `id`                             |
| **GET**   | `/api/orders/user/{userId}` | Get all orders of a specific user | Path Variable: `userId`                         |
| **GET**   | `/api/orders/admin/all`     | Get all orders (admin access)     | None                                            |



## Notes
- No automated test cases have been written as of now.
- Ensure MySQL is running before starting the application.
- Use Postman or Swagger UI to test API endpoints quickly.
- You can get the required `curl` commands from Swagger documentation to test the endpoints after setting it up locally.
