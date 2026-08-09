# AGENTS.md - Mini CRM Server AI Coding Guide

## Project Overview
Mini CRM Server is a Spring Boot 4.1.0 REST API written in Java 21 for managing customers, contacts, and notes. It uses PostgreSQL with Flyway migrations and Spring Data JPA for data access.

**Key Stack:** Spring Boot 4.1.0 | Java 21 | PostgreSQL | Flyway | Lombok | Maven

## Essential Architecture

### Data Model & Entity Relationships
The application follows a star schema pattern centered on **Customer**:
- **User**: System users with ADMIN/USER roles. No soft delete. Tracked as audit references (createdBy/updatedBy) in Customer and Note.
- **Customer**: Core entity with soft delete (deleted=false default), audit fields (createdBy/updatedBy as User references), and CustomerStatus enum (ACTIVE/INACTIVE/LEAD)
- **Contact**: Customers' contacts; belongs to Customer; soft deletable. **No audit fields** (no createdBy/updatedBy). Multiple contacts per customer.
- **Note**: Audit logs for customers; tracks createdBy/updatedBy users (User references); multiple notes per customer.

**Pattern Reference:** See `src/main/java/com/oji/mini_crm_server/model/` for the complete schema.

### Database Schema & Migrations
- Flyway manages all schema changes (V1__create_tables.sql is the baseline)
- Use `CREATE TABLE` statements in new migration files following the V{number}__{description}.sql pattern
- Schema validation is enabled: `spring.jpa.hibernate.ddl-auto=validate` (production safety)
- PostgreSQL BIGINT IDENTITY columns for all primary keys
- Foreign key constraints enforced with CASCADE not configured (deletion must be handled in code)

### Entity Modeling Patterns
All entities follow this structure:
```java
@Data  // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Required by JPA
@Entity
@Table(name = "table_name")
public class EntityName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    private Long id;
    // ... fields ...
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

**Lazy Loading Rule:** All relationships use `fetch = FetchType.LAZY` to prevent N+1 queries.
**Soft Delete Pattern:** Customer and Contact entities have `deleted` boolean field (default false). Queries must filter `deleted=false`.
**Audit Fields:** createdBy/updatedBy reference User entity with LAZY loading; createdAt/updatedAt use LocalDateTime.

### JSON Serialization & Circular Reference Prevention
The application uses Jackson annotations to prevent infinite loops during JSON serialization:
- **@JsonManagedReference:** Used on the parent side of bidirectional relationships (forwards the reference)
  - Example: `Customer.@JsonManagedReference("customer-contacts")` on the @OneToMany side
- **@JsonBackReference:** Used on the child side to suppress serialization (prevents circular reference)
  - Example: `Contact.@JsonBackReference("customer-contacts")` on the @ManyToOne side
- **Special case:** User → Customer relationships use both `@JsonManagedReference` and `@JsonBackReference` for audit tracking (createdBy/updatedBy)

**Important:** When serializing parent entities, child collections are included (via JsonManagedReference), but child entities won't include their parent to prevent cycles.

### Relationship Mappings

**Complete Entity Relationship Diagram:**
```
User ←→ Customer (bidirectional audit)
  ├─ User.@OneToMany(mappedBy="createdBy") → List<Customer> createdCustomers
  ├─ User.@OneToMany(mappedBy="updatedBy") → List<Customer> updatedCustomers
  ├─ Customer.@ManyToOne(fetch=LAZY) → User createdBy [@JoinColumn(name="created_by")]
  └─ Customer.@ManyToOne(fetch=LAZY) → User updatedBy [@JoinColumn(name="updated_by")]

Customer ←→ Contact (one-to-many)
  ├─ Customer.@OneToMany(mappedBy="customer") → List<Contact> contacts
  └─ Contact.@ManyToOne(fetch=LAZY) → Customer customer [@JoinColumn(name="customer_id", nullable=false)]

Customer ←→ Note (one-to-many with audit)
  ├─ Customer.@OneToMany(mappedBy="customer") → List<Note> notes
  ├─ Note.@ManyToOne(fetch=LAZY) → Customer customer [@JoinColumn(name="customer_id", nullable=false)]
  ├─ Note.@ManyToOne(fetch=LAZY) → User createdBy [@JoinColumn(name="created_by")]
  └─ Note.@ManyToOne(fetch=LAZY) → User updatedBy [@JoinColumn(name="updated_by")]
```

**Key Details:**
- All `@ManyToOne` relationships use `fetch = FetchType.LAZY` (prevent N+1 queries)
- Audit relationships (`created_by`, `updated_by`) use **nullable** `@JoinColumn` (can be NULL if user deleted)
- **Contact has NO audit fields** (no createdBy/updatedBy); only Customer and Note track audit info
- JSON serialization uses `@JsonManagedReference` (parent) + `@JsonBackReference` (child) to prevent circular references

## Development Workflows

### Build & Run
```bash
# Build with Maven (Windows)
mvnw.cmd clean package

# Run application (requires PostgreSQL on localhost:5432/miniCRM)
mvnw.cmd spring-boot:run

# Run tests
mvnw.cmd test
```

### Database Setup
```sql
-- PostgreSQL setup (see application.properties for defaults)
createdb miniCRM
-- User: postgres / Password: postgres123
-- Flyway migrations auto-run on startup
```

### Configuration
- `application.properties`: Database connection, Flyway, SQL logging
- `spring.jpa.show-sql=true`: Logs all generated SQL (check console in development)
- `spring.jpa.properties.hibernate.format_sql=true`: Pretty-prints SQL
- Spring Boot DevTools included for hot reload (optional)

## Critical Patterns & Conventions

### Package Structure
The project uses **underscores in package names** (not hyphens):
- ✅ `com.oji.mini_crm_server` (correct)
- ❌ `com.oji.mini-crm-server` (invalid)

For new packages: follow `com.oji.mini_crm_server.{layer}.{domain}` pattern.

### Controllers
- Use `@RestController` annotation (returns JSON, not views)
- Map endpoints with `@GetMapping`, `@PostMapping`, etc.
- Controller example: `HelloController.java` → GET `/greet` returns plain text string

### Enums & Validation
- **Role** (ADMIN, USER): Stored as VARCHAR in users.role with CHECK constraint
- **CustomerStatus** (ACTIVE, INACTIVE, LEAD): Stored as VARCHAR in customers.status with CHECK constraint
- Use `@Enumerated(EnumType.STRING)` for @Column mapping (preserves readability in database)
- Column names in database use snake_case; Java properties use camelCase

### Timestamps
- All entities use `LocalDateTime` (not Instant or Date)
- Database columns: `TIMESTAMP(6)` for microsecond precision
- Must manually set createdAt/updatedAt in application code (no @CreationTimestamp or @UpdateTimestamp)
- Pattern: Initialize in constructors or set before persistence operations

### Naming Conventions
| Entity | Table | ID Column | Example |
|--------|-------|-----------|---------|
| User | users | user_id | private Long id + @Column(name = "user_id") |
| Customer | customers | customer_id | private Long id + @Column(name = "customer_id") |
| Contact | contacts | contact_id | private Long id + @Column(name = "contact_id") |
| Note | notes | note_id | private Long id + @Column(name = "note_id") |

Database columns: lowercase_with_underscores; Java fields: camelCase.

## Common Tasks for AI Agents

### Adding a New Entity
1. Create Java class in `src/main/java/com/oji/mini_crm_server/model/EntityName.java` with @Data, @NoArgsConstructor, @Entity, @Table
2. Include audit fields (createdAt, updatedAt as LocalDateTime)
3. For auditable entities: add createdBy, updatedBy (User) references
4. Use `@GeneratedValue(strategy = GenerationType.IDENTITY)` for @Id
5. Create Flyway migration `src/main/resources/db/migration/V{next_number}__create_entity.sql`
6. Add column checks for enums if applicable (e.g., `CHECK (status IN ('ACTIVE', 'INACTIVE', 'LEAD'))`)

### Adding an API Endpoint
1. Create or update controller in `src/main/java/com/oji/mini_crm_server/controller/`
2. Use `@RestController` and `@GetMapping`, `@PostMapping` etc.
3. For future: Create service layer (currently minimal) if business logic needed
4. For future: Create repository extending JpaRepository for database queries

### Working with Relationships

**Customer-Contact Relationships:**
- Use `@OneToMany(mappedBy = "customer")` on Customer.contacts
- Use `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "customer_id", nullable = false)` on Contact.customer
- Contact has NO audit fields; if tracking is needed, create a Note instead
- JSON: Parent (Customer) serializes as `@JsonManagedReference`, child (Contact) as `@JsonBackReference`

**Customer-Note Relationships:**
- Use `@OneToMany(mappedBy = "customer")` on Customer.notes
- Use `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "customer_id", nullable = false)` on Note.customer
- Note must track audit info: `Note.createdBy` and `Note.updatedBy` as User references
- JSON: Parent (Customer) serializes as `@JsonManagedReference`, child (Note) as `@JsonBackReference`

**User-Customer Audit Relationships:**
- Customer tracks who created/updated it via `createdBy` and `updatedBy` User foreign keys
- User maintains reverse collections: `@OneToMany(mappedBy = "createdBy")` and `@OneToMany(mappedBy = "updatedBy")`
- Both sides use nullable `@JoinColumn` to handle deleted users gracefully
- When querying customers, audit user info is LAZY-loaded; explicitly join if needed in service layer

**General Principles:**
- Always use `fetch = FetchType.LAZY` to avoid N+1 query problems
- When querying: explicitly use `.join()` in JPQL or Criteria API if parent needs child data
- Delete operations: Since foreign keys have no CASCADE, deletion must be handled in service code
- Soft deletes: Filter `deleted = false` at repository query level for Customer and Contact

### Running Migrations
- Place `.sql` file in `src/main/resources/db/migration/` with naming V{N}__{description}.sql
- Flyway auto-applies on next startup
- Schema must match pom.xml dependency: `flyway-database-postgresql` for PostgreSQL-specific features

## Testing
- Test files: `src/test/java/com/oji/mini_crm_server/`
- Use `@SpringBootTest` for integration tests
- Currently minimal: only context loading test present
- Future: Add repository, service, and controller tests

## Key Files Reference
- **Main App:** `src/main/java/com/oji/mini_crm_server/MiniCrmServerApplication.java` (@SpringBootApplication entry point)
- **Models:** `src/main/java/com/oji/mini_crm_server/model/` (User, Customer, Contact, Note, Role, CustomerStatus)
- **Controllers:** `src/main/java/com/oji/mini_crm_server/controller/` (HelloController example)
- **Database:** `src/main/resources/application.properties` (PostgreSQL config), `src/main/resources/db/migration/V1__create_tables.sql` (schema)
- **Build:** `pom.xml` (dependencies: Spring Data JPA, PostgreSQL driver, Lombok, Flyway)

## Warnings & Edge Cases
- **No Security:** Spring Security dependency is commented out in pom.xml; no auth implemented yet
- **Soft Deletes:** Customer and Contact use soft delete logic (deleted flag); queries must filter these at repository level
- **Audit User IDs:** createdBy/updatedBy can be NULL if user account is deleted; handle gracefully in service layer
- **N+1 Problem:** LAZY fetching requires explicit join or separate queries; always check generated SQL with show-sql=true
- **Timestamp Initialization:** Must manually set createdAt/updatedAt; no automatic timestamp generation
- **JSON Circular References:** Always use @JsonManagedReference + @JsonBackReference on bidirectional relationships; missing annotations cause infinite loops
- **Contact Audit Limitation:** Contact entity has no createdBy/updatedBy fields; if full audit trail needed, create Note records instead
- **No Cascade Deletes:** Foreign keys don't cascade; deleting User or Customer requires manual deletion of related records in service code

