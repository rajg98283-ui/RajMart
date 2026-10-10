# Contributing to RajMart

Thank you for your interest in contributing to **RajMart**! We follow clean architecture principles, strict layering, and production-grade security standards.

## Code of Conduct
- Write clean, self-documenting code.
- Always include automated unit and integration tests for new features.
- Never commit secrets, credentials, API keys, or `.env` files.

## Development Workflow
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/RajMart.git
   cd RajMart
   ```

2. **Verify Java 17 and Maven**:
   ```bash
   java -version  # Must be Java 17+
   mvn -version
   ```

3. **Configure Environment Variables**:
   Copy `.env.example` to `.env` if local overrides are needed:
   ```bash
   cp .env.example .env
   ```

4. **Run the Test Suite**:
   ```bash
   mvn clean test
   ```

5. **Start Application Locally**:
   ```bash
   mvn spring-boot:run
   ```
   Open `http://localhost:8080` in your browser.

## Architectural Guidelines
- **Strict Layering**: Controller → Service → DAO → Database.
  - No SQL inside Controllers.
  - No business logic inside DAOs.
  - Do NOT directly access DAOs from Controllers.
  - Use DTOs instead of exposing database entities directly.
- **Database Access**: Always use `PreparedStatement` with `try-with-resources` against the managed connection pool. Never concatenate SQL strings.
- **Security**: Passwords must always be hashed with BCrypt. Enforce role authorizations server-side.
- **Envelopes**: Standardize JSON responses using `ApiResponse<T>`.
- **Commit Messages**: Follow conventional commits (e.g. `feat(auth): ...`, `fix(cart): ...`).
