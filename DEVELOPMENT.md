# DEVELOPMENT.md - Mini CRM Server Development Notes

## Current Work

### In Progress
- [ ] 

### Recently Completed
- [x] Created AGENTS.md with architectural guidelines
- [x] Initial project setup with Spring Boot 4.1.0 & PostgreSQL

---

## Active Development Tasks

### Entity Development
- [ ] Task: 
  - Notes:

### Controller Development
- [ ] Task:
  - Notes:

### Database Migrations
- [ ] Task:
  - Notes:

---

## Known Issues & Workarounds

### Issue Title
**Description:** 
**Affected Component:** 
**Workaround:** 
**Priority:** (High/Medium/Low)
**Status:** (Open/In Progress/Resolved)

### Example: Soft Delete Filtering
**Description:** Customer and Contact queries must filter `deleted=false` manually
**Affected Component:** All repository queries for Customer/Contact
**Workaround:** Remember to add `.where(customer.deleted.isFalse())` or JPQL WHERE clause
**Priority:** High
**Status:** Design Pattern Defined (ongoing vigilance)

---

## Architecture & Design Decisions

### Decision: Soft Delete Pattern for Customer & Contact
**Why:** Preserve audit trail and historical data instead of permanent deletion
**Alternative Considered:** Hard delete with cascading
**Trade-offs:** 
- ✅ Keeps audit history
- ❌ Requires filtering at query level
- ❌ No foreign key cascade available

**Date:** 2026-08-08
**Status:** Implemented

### Decision: LAZY Loading with FetchType
**Why:** Prevent N+1 query problems and improve performance
**Alternative Considered:** Using EAGER loading by default
**Trade-offs:**
- ✅ Reduces unnecessary data retrieval
- ❌ Requires explicit joins when needed
- ❌ Need to verify SQL with `show-sql=true`

**Date:** 2026-08-08
**Status:** Implemented

### Decision: Manual Timestamp Management (No @CreationTimestamp)
**Why:** Explicit control over audit trail; matches database timestamp precision
**Alternative Considered:** Using @CreationTimestamp/@UpdateTimestamp
**Trade-offs:**
- ✅ Explicit & controllable
- ❌ Manual work in every entity save
- ✅ Easier to test and reason about

**Date:** 2026-08-08
**Status:** Implemented

---

## Technical Debt & Improvement Opportunities

### High Priority
- [ ] **Enable Spring Security** - Currently commented out in pom.xml
  - Impact: Authentication & authorization needed for production
  - Effort: Medium
  - Files: pom.xml, existing controllers

- [ ] **Create Repository Layer** - Currently entities only, no repositories
  - Impact: Better data access abstraction, easier testing
  - Effort: Medium
  - Files: Create `src/main/java/.../repository/`

- [ ] **Create Service Layer** - Business logic currently in controllers
  - Impact: Reusability, testability
  - Effort: Medium
  - Files: Create `src/main/java/.../service/`

### Medium Priority
- [ ] **Add DTO Pattern** - Return DTOs instead of entities from API
  - Impact: Decouples API from domain model
  - Effort: High
  - Files: Create `src/main/java/.../dto/`

- [ ] **Add Exception Handling** - Global error handling strategy
  - Impact: Consistent error responses
  - Effort: Low-Medium
  - Files: Create `@ControllerAdvice`

- [ ] **Expand Test Suite** - Currently only context loading test
  - Impact: Confidence in changes
  - Effort: Medium-High
  - Files: `src/test/java/`

### Low Priority
- [ ] **Add Logging Framework** - Debug production issues
  - Impact: Better observability
  - Effort: Low
  - Files: Add SLF4J configuration

- [ ] **API Documentation** - Swagger/Springdoc-OpenAPI
  - Impact: Client documentation
  - Effort: Low
  - Files: Add springdoc-openapi dependency

---

## Debugging & Investigation Notes

### N+1 Query Detection
**How to check:** Enable SQL logging in `application.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```
Then review console output for:
- Multiple SELECT queries on same table in a loop
- Missing JOINs in expected queries

### Testing Soft Delete Logic
**File:** `src/test/java/.../repository/`
**Test Pattern:**
```java
// Find active customers
List<Customer> active = findByDeletedFalse();

// Soft delete
customer.setDeleted(true);
restore.save(customer);

// Verify not in list anymore
active = findByDeletedFalse();
assertFalse(active.contains(customer));
```

### Timestamp Issues
**Watch for:**
- Forgetting to set `createdAt`/`updatedAt` before save
- Using old `java.util.Date` instead of `LocalDateTime`
- Timezone-related bugs with TIMESTAMP(6)

**Quick fix:** Always init timestamps in entity constructor or service layer before persist

---

## Database & Migration Notes

### Current Schema Version
**Latest Migration:** V1__create_tables.sql
**Status:** All 4 tables (users, customers, contacts, notes) created

### Migration Checklist for Future Versions
When adding V2, V3, etc:
- [ ] Use pattern: `V{number}__{description}.sql`
- [ ] Add CHECK constraints for enums
- [ ] Use BIGINT IDENTITY for primary keys
- [ ] Set TIMESTAMP(6) for audit fields
- [ ] Foreign keys: no CASCADE (handle in code)
- [ ] Test migration with clean DB: `dropdb miniCRM && createdb miniCRM`

### PostgreSQL Connection Details
- **Host:** localhost:5432
- **Database:** miniCRM
- **User:** postgres
- **Password:** postgres123
- **Driver:** postgresql-{version}

---

## Performance & Optimization Notes

### Query Optimization Opportunities
- [ ] Index on `customers.deleted` for faster soft-delete filtering
- [ ] Index on `contacts.customer_id` for customer lookups
- [ ] Index on `notes.customer_id` for note queries

### Memory Considerations
- Large customer lists: Implement pagination in controllers
- LAZY loading working as intended - verify with SQL logs
- Lombok @Data includes toString() - be careful with large result sets in logs

---

## Integration Points & External Dependencies

### Database Integration
- PostgreSQL 12+ required
- Flyway handles schema versioning
- Spring Data JPA with Hibernate ORM

### Build & Deployment
- Maven 3.8.1+ (comes with project wrapper mvnw.cmd)
- Java 21 required (specified in pom.xml)
- Spring Boot 4.1.0 stable

### Missing/Commented Dependencies
- Spring Security (pom.xml lines 37-40) - **ACTIVATE WHEN NEEDED**
- No external API dependencies currently

---

## Testing Strategy

### Current Test Coverage
- ✅ Application context loads successfully (`MiniCrmServerApplicationTests.java`)
- ❌ No repository tests
- ❌ No service tests
- ❌ No controller tests

### Testing Plan
1. Add repository tests for CRUD operations on each entity
2. Add service tests for business logic (once service layer created)
3. Add controller integration tests for API endpoints
4. Add soft-delete filtering tests

### Running Tests
```bash
mvnw.cmd test
```

---

## Code Quality & Standards

### Lombok Usage
All entities use:
```java
@Data           // Auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Required for JPA
```
Keep this pattern for all new entities.

### Enum Handling
Always use:
```java
@Enumerated(EnumType.STRING)  // Stores as VARCHAR in DB
@Column(...)
private EnumName field;
```
This matches database CHECK constraints.

### Package Naming
✅ Correct: `com.oji.mini_crm_server` (underscores)
❌ Wrong: `com.oji.mini-crm-server` (hyphens)

---

## Team Communication Notes

### For Future Developers
- Read AGENTS.md first for architectural patterns
- N+1 queries are easy to create - always check SQL logs
- Soft delete filtering is not automatic - it's a manual responsibility
- Timestamps must be set explicitly in code

### Meeting Notes
- Date: YYYY-MM-DD
  - Topic:
  - Decisions:
  - Action Items:

---

## Release Notes & Version History

### v0.0.1-SNAPSHOT (Current)
- Initial project setup
- 4 core entities: User, Customer, Contact, Note
- Basic HelloController with /greet endpoint
- Flyway migrations for schema management
- Development documentation (AGENTS.md, DEVELOPMENT.md)

### Future Versions
- v0.1.0: Add API endpoints for CRUD operations
- v0.2.0: Add Spring Security with authentication
- v0.3.0: Add comprehensive error handling & logging
- v1.0.0: Production-ready with full test coverage

---

## Quick Reference Links & Resources

- [Spring Boot 4.1.0 Docs](https://docs.spring.io/spring-boot/4.1.0/reference/)
- [Spring Data JPA Guide](https://docs.spring.io/spring-boot/4.1.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [PostgreSQL JSON Type Guide](https://www.postgresql.org/docs/current/datatype-json.html) (for future enhancements)
- [Lombok Features](https://projectlombok.org/features/all)

---

**Last Updated:** 2026-08-08
**Created by:** AI Assistant
**Status:** Active Development

