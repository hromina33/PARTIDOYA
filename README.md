# ACME Shop Platform Starter

Minimal DDD example for teaching Domain-Driven Design from scratch. Contains a single bounded context (`catalog`) with two aggregates (`Category` and `Product`), each exposing only:

- `POST` to create
- `GET /{id}` to read by id

Everything else (delete, update, list, search, events, ACL, i18n, `Result<T, E>` monads) has been intentionally **removed** so students see the smallest possible end-to-end DDD slice.

## Tech stack
- Java 21, Spring Boot 4.0.6
- H2 in-memory database (no setup required)
- Lombok, SpringDoc OpenAPI

## Run it
```bash
mvn spring-boot:run
```

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- H2 console: <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:acmeshop`, user `sa`, no password)

## API surface (4 endpoints)

### Categories
- `POST /api/v1/categories` — body: `{ "name": "Books", "description": "Printed and digital books" }`
- `GET /api/v1/categories/{categoryId}`

### Products
- `POST /api/v1/products` — body: `{ "sku": "BOOK-001", "name": "DDD Quickly", "description": "Intro to DDD", "price": 29.90, "currency": "USD", "stock": 100, "categoryId": 1 }`
- `GET /api/v1/products/{productId}`

## DDD patterns demonstrated

| Pattern | Where to look |
|---|---|
| **Bounded context** | `catalog/` directory |
| **Aggregate root** | `catalog/domain/model/aggregates/Category.java`, `Product.java` |
| **Value objects** | `catalog/domain/model/valueobjects/` (CategoryId, CategoryName, CategoryDescription, ProductId, ProductName, ProductDescription, Sku, Money, Stock) |
| **References between aggregates by id only** | `Product.categoryId: CategoryId` — never holds a `Category` instance |
| **Identity immutability** | No `@Setter` on the `id` field; aggregates expose a rehydration constructor used only by the assembler |
| **Repository pattern (port + adapter)** | `catalog/domain/repositories/` (port) + `catalog/infrastructure/persistence/jpa/adapters/` (adapter) |
| **Domain / persistence separation** | Aggregates are pure POJOs; JPA mapping lives in `*PersistenceEntity` classes; `*PersistenceAssembler` translates between them |
| **CQRS — Command/Query separation** | `application/commandservices/` (writes) vs `application/queryservices/` (reads), each as `interface` + `internal/...Impl` |
| **Commands and queries as records** | `domain/model/commands/`, `domain/model/queries/` |
| **REST adapter** | `interfaces/rest/` — controller + `*Resource` records + transformers |
| **Transformer / assembler pattern** | `interfaces/rest/transform/` and `infrastructure/persistence/jpa/assemblers/` |
| **Centralized error handling** | `shared/interfaces/rest/GlobalExceptionHandler` maps domain exceptions → HTTP |
| **Shared kernel for cross-cutting infra** | `shared/` package: auditable JPA base, generic exceptions, error resource |

## Package layout
```
com.acme.shop.platform
├── catalog/                      # Bounded context
│   ├── application/
│   │   ├── commandservices/      # Write-side service interfaces
│   │   ├── queryservices/        # Read-side service interfaces
│   │   └── internal/             # Spring @Service implementations
│   ├── domain/
│   │   ├── model/
│   │   │   ├── aggregates/       # Category, Product (pure POJOs)
│   │   │   ├── commands/         # CreateCategoryCommand, CreateProductCommand
│   │   │   ├── queries/          # GetCategoryByIdQuery, GetProductByIdQuery
│   │   │   └── valueobjects/     # 9 VOs
│   │   └── repositories/         # Port interfaces
│   ├── infrastructure/
│   │   └── persistence/jpa/
│   │       ├── adapters/         # Implement the domain ports
│   │       ├── assemblers/       # Domain ↔ persistence mapping
│   │       ├── converters/       # JPA AttributeConverter (Sku)
│   │       ├── embeddables/      # JPA @Embeddable (Money)
│   │       ├── entities/         # *PersistenceEntity classes (JPA-mapped)
│   │       └── repositories/     # Spring Data JPA repositories
│   └── interfaces/
│       └── rest/                 # Controllers, REST resources, transformers
└── shared/                        # Cross-cutting infrastructure
    ├── domain/exceptions/         # ResourceNotFoundException, ResourceConflictException
    ├── infrastructure/persistence/jpa/entities/   # AuditableAbstractPersistenceEntity
    └── interfaces/rest/           # GlobalExceptionHandler, ErrorResource
```

## What's intentionally NOT here

For a richer reference with the same patterns plus events, ACL between contexts, `Result<T, ApplicationError>`, i18n, native JOIN queries, and Sales context — see the sibling project `acme-shop-platform`.

## License

MIT — see `LICENSE.md`.
