# Mastercard Integration Service

## Overview

The Mastercard Integration Service is a Service SDK microservice that acts as an integration layer between Backbase's Digital Banking Platform and Mastercard's OpenBanking Connect APIs. It implements Backbase's integration contracts for account information services (AIS) and payment initiation services (PIS), translating them to Mastercard OpenBanking Connect API calls.

## Architecture

This service functions as an adapter that:
- Exposes Backbase-compliant REST APIs for arrangements, payments, and cards
- Transforms Backbase data models to Mastercard OpenBanking Connect formats
- Handles authentication context and request metadata propagation
- Provides asynchronous balance fetching for multiple accounts

### Key Components

```
mastercard-integration-service
├── config/              # Configuration and API client setup
├── rest/                # REST Controllers implementing Backbase APIs
├── service/             # Business logic layer
├── mapper/              # Data transformation between Backbase and Mastercard models
├── model/               # Internal data models
└── util/                # Utility classes for request handling
```

## Features

### 1. Account Information Services (AIS)

**Balance Controller** (`BalanceController`)
- Implements Backbase's `BalanceApi` contract
- Fetches account balances from Mastercard OpenBanking Connect
- Supports batch balance retrieval with asynchronous processing
- Maps both current and available balances

**Key Operations:**
- `GET /balances` - Retrieve balances for multiple arrangements

### 2. Payment Initiation Services (PIS)

**Payment Order Controller** (`PaymentOrderController`)
- Implements Backbase's `PaymentOrderIntegrationOutboundApi` contract
- Manages payment order lifecycle

**Key Operations:**
- `POST /payment-orders` - Create new payment orders
- `PUT /payment-orders/{bankReferenceId}` - Update existing payment orders
- `POST /payment-orders/{bankReferenceId}/cancel` - Cancel payment orders

**Payment Validation Controller** (`PaymentValidationController`)
- Implements Backbase's `PaymentOrderIntegrationValidatorApi` contract
- Validates payment order requests before execution

**Key Operations:**
- `POST /webhook/payment-order/validate` - Validate payment order details

### 3. Card Management Services

**Cards Controller** (`CardsController`)
- Implements Backbase's `CardsApi` contract
- Provides card management operations with mock implementations

**Key Operations:**
- `GET /cards` - List cards with optional filters
- `GET /cards/{id}` - Get card details
- `POST /cards` - Request new card
- `PUT /cards/{id}/activate` - Activate a card
- `PUT /cards/{id}/lock-status` - Update card lock status
- `POST /cards/{id}/pin/request` - Request PIN
- `POST /cards/{id}/pin/reset` - Reset PIN
- `POST /cards/{id}/replacement` - Request card replacement
- `PUT /cards/{id}/limits` - Change card limits
- `POST /cards/{id}/authorized-users` - Add authorized user
- `GET /cards/{id}/replacement/fee` - Get replacement fee

## Technical Stack

- **Framework:** Spring Boot 3.x with Service SDK 16.1.6
- **Java Version:** 17
- **Build Tool:** Maven
- **API Generation:** BOAT Maven Plugin (Backbase OpenAPI Tools)
- **Security:** Backbase Auth Security
- **Service Discovery:** Eureka Client
- **Mapping:** MapStruct
- **API Documentation:** OpenAPI/Swagger

## Dependencies

### Backbase Components
- `service-sdk-starter-core` - Core service SDK functionality
- `service-sdk-starter-mapping` - Data mapping utilities
- `auth-security` - Security and authentication
- `backbase-bom` - Bill of materials for version management

### Generated API Clients
The service generates multiple API clients during build:
- **Arrangement Manager API** - Balance integration contracts
- **Payment Order Service API** - Payment integration contracts
- **Card Manager API** - Card management contracts
- **Mastercard AIS API** - Account Information Service client
- **Mastercard APS API** - Account Payment Service client

## Configuration

### Application Properties

**Mastercard OpenBanking Connect API Configuration:**
```yaml
mastercard:
  mcob:
    api:
      baseUri: https://api.mastercard.com/openbanking  # Optional custom base URI
      proxy:
        enabled: false                                  # Enable proxy
        host: proxy.example.com                         # Proxy host
        port: 8080                                      # Proxy port
```

**Service Registration:**
```yaml
spring:
  application:
    name: mastercard-integration-service

eureka:
  instance:
    metadata-map:
      public: false  # Internal service, not exposed publicly
```

## API Client Configuration

The `OpenBankingApiConfiguration` class:
- Configures the Mastercard API client with custom base URI
- Supports HTTP proxy configuration for corporate environments
- Provides beans for Account Balances API

## Request Context Handling

The service propagates authentication and request context through:
- **RequestUtils** - Extracts and builds request information including:
  - PSU IP Address
  - PSU User Agent
  - X-Request-ID for tracing
  - ASPSP ID from token claims
  - Consent ID from token claims

## Data Mapping

### Balance Mapping
- Transforms Mastercard balance types to Backbase balance models
- Supports "Current" and "Available" balance indicators
- Handles credit/debit indicators
- Calculates final balance amounts considering multiple balance entries

### Request Information Mapping
- Maps internal request context to Mastercard's RequestInfo format
- Preserves correlation IDs for distributed tracing

## Build and Run

### Build the Service
```bash
mvn clean install
```

### Run Locally
```bash
source .env
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Build Docker Image
The service includes Jib configuration for containerization:
```bash
mvn compile jib:dockerBuild
```

## Code Generation

The BOAT Maven Plugin generates code during the `generate-sources` phase:

1. **Backbase API Implementations** - Server-side implementations for:
   - Arrangement Balance API
   - Payment Order API
   - Payment Validation API
   - Card Manager API

2. **Mastercard API Clients** - Client-side implementations for:
   - Account Information Service (AIS)
   - Account Payment Service (APS)

## Testing

The service includes:
- Integration tests for REST controllers
- Unit tests for mappers
- Spring Cloud Contract stub runner support for contract testing

Run tests:
```bash
mvn test
```

## Service Behavior

### Balance Service
- Accepts multiple arrangement IDs
- Fetches balances asynchronously using `AsyncTaskExecutor`
- Returns consolidated balance information
- Throws `NotFoundException` if account balances are not found
- Throws `BadRequestException` for API errors

### Payment Order Service
- Currently provides mock implementations
- Generates UUID-based bank reference IDs
- Returns "PROCESSED" status for all operations
- Logs all payment operations for debugging

### Cards Service
- Provides mock card data
- Extracts cardholder name from security context (`sub` claim)
- Returns predefined card attributes (GBP currency, Visa brand, etc.)

## Security

- Integrates with Backbase's security framework
- Uses `SecurityContextUtil` to extract user claims
- Supports OAuth2/OIDC authentication flow
- Propagates consent and ASPSP identifiers from access tokens

## Monitoring and Operations

The service includes:
- Actuator endpoints for health checks and metrics
- HTTP trace repository for request/response logging
- SLF4J logging with configurable levels
- Service discovery registration with Eureka

## Integration Points

### Upstream (Backbase Platform)
- Arrangement Manager - For balance queries
- Payment Order Service - For payment execution
- Card Manager - For card operations

### Downstream (Mastercard OpenBanking Connect)
- Account Information Service API
- Payment Service API

## Development Notes

### Adding New Endpoints
1. Update relevant controller in `rest/` package
2. Add business logic in `service/` package
3. Create mappers in `mapper/` package if needed
4. Add integration tests

### Customizing API Clients
Modify the BOAT plugin executions in `pom.xml` to:
- Change API specifications
- Adjust package names
- Configure additional generation options

## Known Limitations

- Payment operations are currently mocked
- Card operations return simulated data
- No actual Mastercard API integration for payments and cards (AIS balances are integrated)

## Future Enhancements

- Complete Mastercard Payment Service integration
- Implement real card management operations
- Add transaction history retrieval
- Support additional account types
- Implement webhook handling for payment status updates

## Related Components

This service works in conjunction with:
- **Authorization Server** - Provides OAuth2/OIDC authentication
- **Backbase Environment** - Complete Digital Banking Platform setup

## Support and Documentation

- [Backbase Service SDK Documentation](https://community.backbase.com/)
- [Mastercard OpenBanking Connect Documentation](https://developer.mastercard.com/open-banking-connect/documentation/)
- [BOAT Maven Plugin](https://github.com/Backbase/boat-maven-plugin)

## License

This is a proof-of-concept implementation for integration purposes. Do not use it in production.

