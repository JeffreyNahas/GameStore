# project-group-11

Team Introduction

Welcome to our ECSE 321 Project repository! This project focuses on developing a system in compliance with software engineering principles, covering requirements, domain modeling, database design, and more. Our team is dedicated to implementing a solution following best practices in agile project management and ensuring the quality of the system through testing, documentation, and collaboration.

# Team Members:

| Name            | GitHub Name                                       | Major                  | Year |
|-----------------|---------------------------------------------------|------------------------|------|
| Emile Labrunie |     E10o0 (https://github.com/E10o0)| Computer Engineering   | U2   |
| Paolo Lahoud |    Gommo203 (https://github.com/Gommo203)        | Software Enineering             | U2   |
| Rami El Nawam    |  Rami EL-Nawam (https://github.com/RamiNawam)   | Software Enineering | U2   |
| Jeffrey Nahas  | JeffreyNahas (https://github.com/JeffreyNahas) | Software Engineering   | U2   |
| Yanzhe Zhang| yanzhehw (https://github.com/yanzhehw) | Software Engineering   | U2   |


# Main Scope of the Project

The owner of an independent game shop wants to expand their business with an online store. Our task is to develop a web application that allows the owner and employees to manage the product catalog, inventory, and customer orders. The system should also provide a user-friendly interface for customers to browse and purchase games online. 

Key functionalities include:
- Store staff can manage the inventory, add new games, and remove games.
- The owner can approve new games before they are published.
- Customers can create accounts, purchase games, track order history, save items to a wish list, and post reviews.
- The owner requires an administrative dashboard for managing employees, game categories, reviewing customer orders, and replying to reviews.

The application must support these functionalities via a web frontend, ensuring accessibility for all stakeholders. No external system integrations are required. 

## DELIVERABLES:

# Deliverable 1: Requirements, Domain Model, and Database Design

You can see the Deliverable Report here: [Deliverable1](https://github.com/McGill-ECSE321-Fall2024/project-group-11/wiki/Deliverable1)


This deliverable includes:
- requirements selection (functional and non-functional system requirements).
- Use case diagrams and detailed specifications for six use cases.
- A domain model represented through UML class diagrams.
- A PostgreSQL-based persistence layer implemented.
- A test suite validating read/write operations for each class of the domain model.
- A build system automating the compilation and testing processes.
- Documentation of team meetings and design decisions in the project wiki.

## Team Members and Roles

| Name            | Role                 | Hours                            | Contribution|
|-----------------|----------------------|----------------------------------|----------------------------------|
| Emile Labrunie   | Project Manager, uses Cases Designer  | 16hours  | Manage Wiki pages,manage table of contents,did the use cases diagrams, create issues for deliverable 1,one use case specification|
|  Paolo Lahoud| Requirements and UML diagram | 20 hours | Create & manage Domain Model ,Schedule meetings,technical writing for requirements,    writing test cases ,one use case specification     |
| Rami El Nawam | Database Designer    | 17 hours |create CRUD & update CRUD repository,comment CRUD and persistence. add JPA annotations ,one use case specification |
| Jeffrey Nahas  |  Build System Lead     | 18 hours | create persistence layer,create JUnit tests for persistence layer,writing test cases,one use case specification  |
|  Yanzhe Zhang|Test Developer, persistence layer developer    | 17 hours | update, and type out meeting minutes,writing test cases, one use case specification|

Each team member contributed to the project through their assigned roles, ensuring that all tasks for Deliverable 1 were completed efficiently and on time.



# Deliverable 2: Backend Services, Behaviour Modelling, and Testin

You can see the Deliverable Report here: [Deliverable2](https://github.com/McGill-ECSE321-Fall2024/project-group-11/wiki/Deliverable2)


For Deliverable 2, we will implement backend services using Java Spring to provide RESTful HTTP endpoints for all use cases, supported by a persistence layer from Deliverable 1. This includes creating a software quality assurance plan, achieving high test coverage through unit and integration testing with JUnit, and documenting our testing and quality assurance practices. A build system will be set up to automate compilation, testing, and coverage reporting. We’ll use GitHub’s issue tracking for project management and document our progress, team roles, and key design decisions in a project report linked in the README file.

## Team Members and Roles

| Name            | Role                 | Hours                            | Contribution|
|-----------------|----------------------|----------------------------------|----------------------------------|
| Emile Labrunie   | Management and Development | 32hours  | Manage Wiki pages,manage table of contents,Re did the UML diagram,implemented repository , DTo and Controller Files|
|  Paolo Lahoud| Development and Testing  | 28 hours |Schedule meetings, Unit testing ,Integration Testing, Dto files correction   |
| Rami El Nawam | Development and Testing    | 29 hours | Meeting Minutes, Re did Umple code, Services files correction, Unit Testing, Integration Testing|
| Jeffrey Nahas  | Development and Testing     | 33 hours | created data base again, and ran Tests, implemented servoces , DTo and Controller Files,testing |
|  Yanzhe Zhang|Development and Testing    | 35 hours | Persistence Layer with model files and repository files, did and debugged services files, Testing |

Each team member contributed to the project through their assigned roles, ensuring that all tasks for Deliverable 2 were completed efficiently and on time.

# Deliverable 2: Backend Services, Behaviour Modelling, and Testin

You can see the Deliverable Report here: [Deliverable3](https://github.com/McGill-ECSE321-Fall2024/project-group-11/wiki/Deliverable3)

For Deliverable 3, we will develop a web frontend using HTML, CSS, and JavaScript, ensuring usability and functional correctness. This frontend will integrate seamlessly with the backend services implemented in Deliverable 2 via asynchronous RESTful API calls. We will establish a build system to automate compilation, testing, and packaging processes, ensuring a streamlined development workflow. Project management will involve using GitHub’s issue tracking to manage the backlog, assign responsibilities, and document progress. A README file will outline team roles, contributions, and the project’s scope, while a linked project report in the GitHub wiki will capture meeting minutes and key design decisions.

| Name            | Contributions frontend                 | Hours                            | Contribution|
|-----------------|----------------------|----------------------------------|----------------------------------|
| Emile Labrunie   | navigation bars for employee owner, create game page, add promotion, create employee , myaccount page | 22hours  | Manage Wiki pages,manage table of contents,|
|  Paolo Lahoud| initialized frontend, customer home page, log in page, wishlist | 28 hours |Schedule meetings,   |
| Rami El Nawam | my games page    | 22 hours | record Meeting Minutes |
| Jeffrey Nahas  | implemented functionnalities for customers to link it to backend, log out  method | 26 hours |fix backend for frontend |
|  Yanzhe Zhang|Delete game, Delete employee   | 24 hours | created services methods missing|

Each team member contributed to the project through their assigned roles, ensuring that all tasks for Deliverable 3 were completed efficiently and on time.
