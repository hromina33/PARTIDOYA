# ACME Shop Platform Starter — User Stories

Minimal API for teaching DDD. Only 4 endpoints.

Conventions:
- Base path: `/api/v1`
- Body: `application/json`
- Errors: `{ "code": "...", "message": "..." }` — codes are `NOT_FOUND`, `CONFLICT`, `VALIDATION_ERROR`, `UNEXPECTED_ERROR`.

---

## TS-CAT001 — Create a Category

As a frontend developer, I want to create a category so that products can be grouped under it.

Acceptance criteria:
- Scenario: Successful create
  - Given a POST request to `/api/v1/categories` with body `{ "name": "Books", "description": "Printed and digital books" }`
  - When the API validates and creates the category
  - Then the API responds `201 Created` with body `{ "id": Long, "name": String, "description": String }`.
- Scenario: Validation error
  - Given a POST request with a missing or blank `name`/`description`, or with `name` longer than 64 characters / `description` longer than 1000
  - When the API validates the request
  - Then the API responds `400 Bad Request` with code `VALIDATION_ERROR`.
- Scenario: Duplicate name
  - Given a POST request whose `name` matches an existing category
  - When the API detects the conflict
  - Then the API responds `409 Conflict` with code `CONFLICT`.

---

## TS-CAT002 — Get Category by id

As a frontend developer, I want to fetch a category by id.

Acceptance criteria:
- Scenario: Found
  - Given a GET request to `/api/v1/categories/{categoryId}`
  - When the category exists
  - Then the API responds `200 OK` with `{ "id": Long, "name": String, "description": String }`.
- Scenario: Not found
  - Given a GET request to `/api/v1/categories/{categoryId}` for a non-existent id
  - When the category does not exist
  - Then the API responds `404 Not Found`.

---

## TS-P001 — Create a Product

As a frontend developer, I want to create a product so that I can add items to the catalog.

Acceptance criteria:
- Scenario: Successful create
  - Given a POST request to `/api/v1/products` with body `{ "sku": "BOOK-001", "name": "DDD Quickly", "description": "Intro", "price": 29.90, "currency": "USD", "stock": 100, "categoryId": 1 }`
  - When the API validates the attributes and confirms the referenced category exists
  - Then the API responds `201 Created` with body `{ "id": Long, "sku": String, "name": String, "description": String, "price": Number, "currency": String, "stock": Integer, "categoryId": Long }`.
- Scenario: Validation error
  - Given a POST request with invalid attributes (blank SKU, negative price, stock < 0, currency not 3 letters, etc.)
  - When the API validates the request
  - Then the API responds `400 Bad Request` with code `VALIDATION_ERROR`.
- Scenario: Duplicate SKU
  - Given a POST request with a SKU that already exists
  - Then the API responds `409 Conflict` with code `CONFLICT`.
- Scenario: Referenced category does not exist
  - Given a POST request with a `categoryId` that does not match any category
  - Then the API responds `404 Not Found` with code `NOT_FOUND`.

---

## TS-P002 — Get Product by id

As a frontend developer, I want to fetch a product by id.

Acceptance criteria:
- Scenario: Found
  - Given a GET request to `/api/v1/products/{productId}`
  - When the product exists
  - Then the API responds `200 OK` with the product attributes.
- Scenario: Not found
  - Given a GET request to `/api/v1/products/{productId}` for a non-existent id
  - When the product does not exist
  - Then the API responds `404 Not Found`.
