## - BoardRoom -

## Team Introduction
We are Group 17, a group of 7 members working together on a Board Game Rental and Event Host Management System. Our team is comprised of:
- Janelle Tam - Software Engineering, U1
- Malak Oualid - Software Engineering, U1
- Natasha Lawford - Software Engineering, U1
- Alison Quach - Computer Engineering, U2
- Nathan Reid - Computer Engineering, U2
- Gracie Diabo - Software Engineering, U2
- Jason Rustom - Software Engineering, U2


## Project Scope
The **BoardRoom** provides the following features:
- **User Authentication**: Users must log in to access any features.
- **Account Types**:
  - **Players**: Can browse available games, review them, and register for events.
  - **Game Owners**: Can manage their game collection, receive borrowing requests, and track their lending history.
- **Board Game Management**:
  - **Players**: A list of all available board games, allowing users to see who owns each game.
  - **Game Owners**: Ability to add,update and delete board games, as well as making them available for rent.
- **Event Creation & Participation**:
  - Users can create board game events with a **date, time, location, description, max participants, and the game to be played**.
  - Other users can register for these events if slots are available.

## Team Members & Contributions
### Deliverable 0.5
(All members contributed equally)

| Team Member     | Role                     | Deliverable| Description of Work            | Time Spent (hours)  |
|-----------------|--------------------------|------------|--------------------------------|---------------------|
| Gracie Diabo    | Developer                | 0.5        | Functional Requirements        | 3                   |
| Janelle Tam     | Project Manager          | 0.5        | Use Case Diagram               | 4                   |
| Natasha Lawford | Developer                | 0.5        | Functional Requirements        | 3                   |
| Alison Quach    | Developer                | 0.5        | Use Case Diagram               | 3                   |
| Malak Oualid    | Senior Testing Developer | 0.5        | Functional Requirements        | 3                   |
| Jason Rustom    | Senior Developer         | 0.5        | Non-Functional Requirements    | 3                   |
| Nathan Reid     | Developer                | 0.5        | Functional Requirements        | 4                   |

### Deliverable 1
(All members contributed equally)

| Team Member     | Role                     | Deliverable| Description of Work            | Time Spent (hours)  |
|-----------------|--------------------------|------------|--------------------------------|---------------------|
| Gracie Diabo    | Developer                | 1          | Domain Model                   | 11                  |
| Janelle Tam     | Project Manager          | 1          | Persistence Layer Testing      | 12                  |
| Natasha Lawford | Developer                | 1          | Domain Model                   | 11                  |
| Alison Quach    | Developer                | 1          | Domain Model                   | 11                  |
| Malak Oualid    | Senior Testing Developer | 1          | Persistence Layer Testing      | 12                  |
| Jason Rustom    | Senior Developer         | 1          | Persistence Layer, Domain Model| 12                  |
| Nathan Reid     | Developer                | 1          | Persistence Layer              | 11                  |

### Deliverable 2
(All members contributed equally)

| Team Member     | Role                     | Deliverable| Description of Work                                              | Time Spent (hours)  |
|-----------------|--------------------------|------------|------------------------------------------------------------------|---------------------|
| Gracie Diabo    | Developer                | 2          | Development & Testing: Log In & Person Class + Report            | 20                  |
| Janelle Tam     | Project Manager          | 2          | Development & Testing: Board Game & Review Classes + Code Review | 22                  |
| Natasha Lawford | Developer                | 2          | Development & Testing: Registration Class + Report               | 21                  |
| Alison Quach    | Developer                | 2          | Development & Testing: Event & Review Classes + Report           | 21                  |
| Malak Oualid    | Senior Developer         | 2          | Development & Testing: Lending History & Profile + Code Review   | 20                  |
| Jason Rustom    | Senior Developer         | 2          | Development & Testing: Board Game & Toggle Account + Code Review | 22                  |
| Nathan Reid     | Developer                | 2          | Development & Testing: Borrow Request Class + Code Review        | 21                  |

### Deliverable 3
(All members contributed equally)

| Team Member     | Role                     | Deliverable| Description of Work                                              | Time Spent (hours)  |
|-----------------|--------------------------|------------|------------------------------------------------------------------|---------------------|
| Gracie Diabo    | Developer                | 2          | Development & Testing: Log In & Homepage, Update Header and Homepage, UI Design + Report            | 25                  |
| Janelle Tam     | Project Manager          | 2          | Development & Testing: New Review, Create Game Popup, Add Game Forms, View Games  | 26                  |
| Natasha Lawford | Developer                | 2          | Development & Testing: Event Creation, Event Registration, Unregister Event, UI Design + Report               | 25                  |
| Alison Quach    | Developer                | 2          | Development & Testing: Header, Event Creation, Event Creation form, Display Events + Report           | 25                  |
| Malak Oualid    | Senior Developer         | 2          | Development & Testing: View Games, Create Game Popup, Borrow Request Catalogue, Borrow Form    | 26                  |
| Jason Rustom    | Senior Developer         | 2          | Development & Testing: Local Storage, Account Details Update Login, Update Owned Games, Toggle Account, + Code Review | 25                  |
| Nathan Reid     | Developer                | 2          | Development & Testing: Account Details: Borrow Requests, Lending History, Event History, Owned Games + Report        | 25                  |

## Testing Summary
We created a persistence test for each model class in the application, such that the test would perform read and write access for the objects, attributes, and references.

We implemented service and integration tests to validate the core logic and overall system behavior:

Service Tests: These tests focus on validating the business logic of the services. We tested various scenarios, including valid and invalid inputs, expected outputs, and exception handling.

Integration Tests: These tests verify the interaction between different components, including the persistence layer and external dependencies. They ensure that database operations, API interactions, and service-layer logic work together as expected in a real-world scenario.

UI Testing: We assigned three UI testers to cover the UI functionality and confirm that interactions with the backend are consistent through several test procedures including logging in, creating events, sending borrow requests, ... 

## Testing Configuration
Please follow these instructions to ensure the project tests runs on your local host.
1. Update your Java version to version 23 (will need to update [path variables](https://www.happycoders.eu/java/how-to-switch-multiple-java-versions-windows/]))
2. Change your local psql postgres user password to "group17" to make it consistent. You can use these commands
```
psql -U postgres
\password postgres
[enter 'group17' as the password]
```
3. Create a new database  called `boardroom`
```
psql -U postgres
CREATE DATABASE boardroom;
```
4. Run `./gradlew build`

## Documentation for RESTful service endpoints
Please follow these instructions to ensure the project RESTful endpoints documentation runs on your local host.
1. Run `./gradlew bootRun`
2. Open `http://localhost:8080/swagger-ui.html` on your browser

## Documentation for Backend Local Host Setup
From the root directory of the project in the terminal run: 
1. cd Boardroom-backend
2. Run './gradlew bootrun'
The backend is hosted locally at 'http://localhost:8080'

## Documentation for Frontend Local Host Setup
From the root directory of the project in the terminal run: 
1. cd Boardroom-frontend
2. npm install 
3. npm run dev
The frontend is locally hosted at 'http://localhost:5173'

## Link to the Deliverable 1 Wiki:
https://github.com/McGill-ECSE321-Winter2025/project-group-17/wiki/Project-Report-%E2%80%90-Deliverable-1 

## Link to the Deliverable 2 Wiki:
https://github.com/McGill-ECSE321-Winter2025/project-group-17/wiki/Project-Report-%E2%80%90-Deliverable-2

## Link to the Deliverable 3 Wiki:
https://github.com/McGill-ECSE321-Winter2025/project-group-17/wiki/Project-Report-%E2%80%90-Deliverable-3
