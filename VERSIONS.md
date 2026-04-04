# Project Version Standards

This document defines the standardized versions used across the StreetLeague project.

## Backend (Java/Spring Boot)

- **Java**: 21
- **Spring Boot**: 3.5.11
- **Maven**: Wrapper included (mvnw)
- **Lombok**: Managed by Spring Boot parent

### Key Dependencies
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot Starter WebSocket
- H2 Database (runtime)

## Frontend (Angular)

- **Node.js**: 18.20.0 (LTS)
- **npm**: >=9.0.0
- **Angular**: 16.2.16
- **TypeScript**: 5.1.3
- **RxJS**: 7.8.0
- **Zone.js**: 0.13.0

### Key Dependencies
- Angular CLI: 16.2.16
- Angular DevKit Build Angular: 16.2.16
- Jasmine: 4.6.0
- Karma: 6.4.0

## Version Management Files

- `.nvmrc` - Node version for the entire project
- `frontend/.nvmrc` - Node version for frontend
- `.java-version` - Java version for the entire project
- `backend/.java-version` - Java version for backend
- `frontend/package.json` - Frontend dependencies with engines specification
- `backend/pom.xml` - Backend dependencies

## Version Range Strategy

All versions use the tilde (`~`) range to allow patch-level updates only:
- `~16.2.16` allows updates to 16.2.x (e.g., 16.2.17, 16.2.18)
- This ensures consistency while allowing bug fixes

## Setup Instructions

### Backend
```bash
cd backend
./mvnw clean install
```

### Frontend
```bash
cd frontend
nvm use  # Uses version from .nvmrc
npm install
```

## Updating Versions

When updating versions, ensure:
1. All Angular packages use the same version
2. Update both `package.json` and this documentation
3. Test thoroughly after version updates
4. Update `.nvmrc` if changing Node.js version
5. Update `.java-version` if changing Java version
