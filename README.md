## Restaurant Picker Application

Restaurant Picker is a Spring Boot backend application that allows a group of users to:

Create a lunch session

Join the session

Submit restaurant options

Randomly pick one restaurant

End the session after selection

## Features

Create & manage lunch sessions

Join sessions

Restrict restaurant submissions to joined users only

Random restaurant selection

Session lifecycle management (ACTIVE → ENDED)

Batch user bootstrap via CSV

Centralized exception handling

Integration & batch testing

## Tech Stack

Java 17

Spring Boot 3

Spring Data JPA

Spring Batch

H2 In-Memory Database

Maven

JUnit 5

## REST API Endpoints
## User APIs
Method	Endpoint	Description
POST	/users	Create a user
GET	/users	Get all users

Request

{
  "name": "Alice"
}

## Session APIs
Create Session
Method	Endpoint
POST	/sessions

Request Body

{
  "userId": 1
}

## Join Session
Method	Endpoint
POST	/sessions/{sessionId}/join?userId={userId}

## End Session
Method	Endpoint
POST	/sessions/{sessionId}/end?userId={creatorId}

Only the session creator can end the session.

## Restaurant APIs
## Submit Restaurant
Method	Endpoint
POST	/sessions/{sessionId}/restaurants

Request Body

{
  "userId": 2,
  "restaurantName": "Dominos"
}


Only users who have joined the session can submit.

## Pick Random Restaurant
Method	Endpoint
GET	/sessions/{sessionId}/restaurants/pick

Returns randomly selected restaurant.

##  Session Lifecycle

Session created → ACTIVE

Users join session

Restaurants submitted

Random restaurant picked

Session ended → ENDED

No further submissions allowed

##  Testing

Integration tests for session flows

Service-level validation tests

Batch job testing using SpringBatchTest

H2 used for isolation

Run tests:

mvn clean test

## Spring Batch – User Bootstrap

Users can be preloaded from a CSV file using Spring Batch.

CSV Location
src/main/resources/users.csv

Run Batch Job
SPRING_PROFILES_ACTIVE=batch mvn spring-boot:run


Verify:

SELECT * FROM USERS;

## Run Application
mvn spring-boot:run


API: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

## Assumptions

Authentication is out of scope

Users exist before session creation

One restaurant is picked per session

### Run tests
mvn clean test

## Run the Application

mvn spring-boot:run
Application runs on: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

Batch run to add users : $env:SPRING_PROFILES_ACTIVE="batch"
                         mvn spring-boot:run


## API Testing (Postman)

This project includes a Postman collection for testing all REST APIs.

**Location:** `/postman/RestaurantPicker.postman_collection.json`

### How to use
1. Import the collection into Postman
2. Set base URL (e.g. `http://localhost:8080`)
3. Run APIs in the required order

Author:

Shobana Natarajan