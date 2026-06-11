# FirstClub Membership Program

Backend + dashboard for managing subscription memberships, tier eligibility, benefits, and orders for FirstClub.

## Overview

This project is a Spring Boot 3.2.5 application built with Java 17 and Maven. It demonstrates:

- Membership plans with effective pricing and duration
- Tier-based benefits with configurable eligibility rules
- Subscription lifecycle actions like subscribe, upgrade, downgrade, and cancel
- Tier evaluation based on order count, order value, or cohort
- A modern static dashboard UI for interacting with the API
- H2 in-memory database with seeded demo data

## Tech Stack

- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Java 17
- Maven

## Project Structure

- `src/main/java/com/firstclub/membership/` - backend source code
- `src/main/resources/application.properties` - app and database configuration
- `src/main/resources/static/` - dashboard UI files
- `pom.xml` - Maven build file

## Demo Data

The app seeds the database on startup with:

- 3 plans: Monthly, Quarterly, Yearly
- 3 tiers: Silver, Gold, Platinum
- 3 demo users: Alice, Bob, and Carol
- Sample orders for Alice and Bob

## How To Run

### Prerequisites

- Java 17 or higher
- Maven 3.8+ or a Maven wrapper

### Start the App

From the `membership-program` folder:

```bash
mvn clean spring-boot:run
```

If you prefer compile first:

```bash
mvn clean compile
mvn spring-boot:run
```

## How To See The UI

After the application starts, open:

- UI: http://localhost:8080/

The dashboard lets you:

- Select a demo user
- View eligible tier and active membership details
- Subscribe to a plan and tier
- Upgrade, downgrade, or cancel membership
- Place orders and re-check eligibility
- See API activity in the log panel

## H2 Console

You can inspect the in-memory database here:

- H2 Console: http://localhost:8080/h2-console

Use these connection values:

- JDBC URL: `jdbc:h2:mem:membershipdb`
- Username: `sa`
- Password: empty

## API Endpoints

- `GET /api/users`
- `GET /api/plans`
- `GET /api/tiers`
- `POST /api/membership/subscribe`
- `GET /api/membership/{userId}`
- `PUT /api/membership/{userId}/upgrade`
- `PUT /api/membership/{userId}/downgrade`
- `DELETE /api/membership/{userId}`
- `GET /api/membership/{userId}/eligible-tier`
- `POST /api/orders`

## Notes

- Only one active membership is allowed per user.
- Tier eligibility uses OR logic across configured strategies.
- Platinum is checked first, then Gold, then Silver.
- The app uses optimistic locking on membership updates.

