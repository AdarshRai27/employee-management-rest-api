# Employee Management REST API

A simple, runnable Spring Boot REST API implementing employee CRUD operations for the given scenario.

## Requirements

- JDK 17 or later
- Apache Maven 3.8+
- Postman, curl, or another REST client (optional, for testing endpoints)

## Run the application

1. Extract the ZIP file.
2. Open a terminal in the extracted `employee-management-rest-api` folder.
3. Run:

```bash
mvn clean spring-boot:run
```

The API starts at `http://localhost:8080`.

To build a runnable JAR:

```bash
mvn clean package
java -jar target/employee-management-rest-api-1.0.0.jar
```

## API endpoints

Base URL: `http://localhost:8080/api/employees`

| Method | Endpoint | Purpose | Success response |
|---|---|---|---|
| POST | `/api/employees` | Add an employee | `201 Created` |
| GET | `/api/employees` | Get all employees | `200 OK` |
| GET | `/api/employees/{id}` | Get employee by ID | `200 OK` |
| PUT | `/api/employees/{id}` | Update employee details | `200 OK` |
| DELETE | `/api/employees/{id}` | Delete employee | `200 OK` |

For an unknown employee ID, GET/PUT/DELETE return `404 Not Found`. Invalid request data returns `400 Bad Request`. Creating a duplicate ID returns `409 Conflict`.

## Test with curl

### 1. Add an employee

```bash
curl -i -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Aarav Sharma","department":"IT","salary":55000}'
```

Expected status: `201 Created`.

Add another employee:

```bash
curl -i -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":2,"name":"Priya Verma","department":"HR","salary":48000}'
```

### 2. Retrieve all employees

```bash
curl -i http://localhost:8080/api/employees
```

### 3. Retrieve an employee by ID

```bash
curl -i http://localhost:8080/api/employees/1
```

Try a missing ID, such as `/api/employees/99`, to receive `404 Not Found`.

### 4. Update an employee

```bash
curl -i -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Aarav Sharma","department":"Cyber Security","salary":62000}'
```

The ID in the URL identifies the employee. The request body supplies the new name, department, and salary.

### 5. Delete an employee

```bash
curl -i -X DELETE http://localhost:8080/api/employees/1
```

## Implementation notes

- `Employee` is a Java model/POJO with `id`, `name`, `department`, and `salary`, a no-argument constructor, a parameterized constructor, getters, and setters.
- `EmployeeController` uses `@RestController`, `@RequestMapping`, `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`, `@RequestBody`, and `@PathVariable`.
- `ResponseEntity` is used to return suitable HTTP status codes and response bodies.
- This assignment version stores employees in a thread-safe in-memory `ConcurrentHashMap`; records are cleared when the application stops. A database is not required by the given scenario. For persistent storage, the controller can later be connected to Spring Data JPA and MySQL.
- The API does not accept an ID in the PUT URL body as an override; the URL ID remains authoritative.
