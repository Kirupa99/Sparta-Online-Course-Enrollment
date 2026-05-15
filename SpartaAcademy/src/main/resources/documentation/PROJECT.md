# Sparta Global Academy – API & Web Development Mini Project

## Brief

In your last project, Sprint 1, you create a Spring Boot API for the Academy. In this spring you will extend your completed REST API project by adding a web layer using Spring MVC and Thymeleaf.

Your updated application must now include web controllers and at least 3 user-facing view rendered with Thymeleaf. Expand your data access layer with additional custom repository methods that your new controllers can utilise.

Continue working collaboratively in your group, using Git/GitHub, a project board, and Scrum practices. Maintain DTO usage, update Swagger/OpenAPI documentation as needed, and ensure your service layer remains well-tested with JUnit + Mockito.

The extension duration is one sprint.

---

## Deliverables

- A complete Spring Boot REST API with an added web layer
- Minimum 3 resources with full CRUD: Trainers, Trainees, Courses
- At least two additional custom repository methods beyond standard JpaRepository methods (e.g., `filterByName`, `findByCourseDuration`, `findTraineesByTrainer`)
- Web controllers for your resources with endpoints returning Thymeleaf views
- At least one Thymeleaf template displaying data from the API (e.g., list of courses, trainers, or trainees)
- The application can use either an in-memory database or MySQL for data persistence
- Service layer which utilizes DTOs
- Mockito and JUnit tests for the Service Layers and new web controllers
- Spring Secuity implmented
- Work collaboratively on GitHub
- GitHub project board with user stories (examples below) with the following columns
  - **Project Backlog, Sprint Backlog, In Progress, In Review, Completed, Notes**
  - Include a Definition of Done for your User Stories and Project as a ticket in the **Notes** column
  - The taskboard can also contain tasks such as *"Create skeleton structure"*, *"Prep for Review"* etc
- GitHub Action for CI (optional)
- README detailing how to set up and run the application, including web layer instructions

### User Stories (examples)

| User Story | Scenario | Given | When | Then |
|------------|----------|-------|------|------|
| 1. As a Trainer, I want to view all courses in a web page so that I can manage them easily. | 1.1 View courses list | I am logged in as a Trainer and there are courses in the system | I navigate to the "Courses" page | I should see a list of all courses displayed on the page |
| | 1.2 No courses available (sad path) | I am logged in as a Trainer and there are no courses in the system | I navigate to the "Courses" page | I should see a message indicating that no courses are available |
| 2. As a Trainer, I want to view trainees assigned to me using a web interface. | 2.1 View assigned trainees | I am logged in as a Trainer and I have trainees assigned to me | I navigate to the "My Trainees" page | I should see a list of my assigned trainees |
| | 2.2 No assigned trainees (sad path) | I am logged in as a Trainer and I have no trainees assigned | I navigate to the "My Trainees" page | I should see a message indicating that no trainees are assigned to me |
| 3. As a Trainer, I want to search for courses by duration so that I can easily find relevant information. | 3.1 Search courses by duration | I am logged in as a Trainer and there are courses with various durations | I enter a duration filter and submit the search on the "Courses" page | I should see a filtered list of courses matching the duration |
| | 3.2 No courses match duration (sad path) | I am logged in as a Trainer and no courses match the entered duration | I enter a duration filter and submit the search on the "Courses" page | I should see a message indicating that no courses match the selected duration |

---

- 5-10 min demo: Show the new Thymeleaf view(s), demonstrate web controller endpoints, highlight new repository methods, tests, design decisions, and updated project board
