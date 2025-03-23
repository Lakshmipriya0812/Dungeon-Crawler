
---

# Dungeon Crawler Game

## Description

Dungeon Crawler is a Java-based game with a graphical user interface (GUI) where the player navigates a dungeon, collects coins, and avoids enemies. The game features a panel where the player controls a tile, collects coins, and interacts with power-ups and enemies. The player can move two steps at a time when they collect a power-up.

### Key Features:
- **Player Movement**: The player tile moves around the game board, collecting coins and avoiding enemies.
- **Enemies**: Enemies move horizontally and vertically on the board, creating obstacles for the player.
- **Power-Ups**: Collect power-ups to gain the ability to move two steps at a time, enhancing gameplay.
- **Coins**: The player collects coins scattered around the game board to increase their score.

## Development Methodologies

The development of this game follows **Test-Driven Development (TDD)** and **Extreme Programming (XP)** practices to ensure code quality and functionality throughout the development process.

### Practices:
- **Test-Driven Development (TDD)**: Unit tests were written before implementing the game logic to ensure each feature works as expected.
- **Extreme Programming (XP)**: Used practices such as **pair programming**, **refactoring**, and **continuous integration** to ensure code quality.

## Technologies Used

- **Java**: The game is built using Java for the core logic and user interface.
- **Gradle**: Used for project automation, build management, and dependency management.
- **JUnit**: Unit testing was performed using JUnit to validate game mechanics and functionality.
- **Mockito**: Mocking was used with Mockito to simulate dependencies in unit tests for isolated testing.
- **Cucumber**: Behavior-driven testing (BDD) was applied to ensure that the game behaves as expected from the player's perspective.
- **Mutation Testing**: Mutation testing was used to evaluate the effectiveness of the tests by introducing small code changes and checking if tests would catch them.
- **Static Code Analysis**:  Continuous integration and continuous deployment were set up using GitLab CI to automate testing, building, and deployment processes. GitLab CI pipelines were configured for code quality checks and testing.

## Continuous Integration and Deployment

CI/CD pipelines were set up to automate testing, building, and deployment, allowing for a streamlined development and deployment process.

- **Continuous Integration (CI)**: Ensures that every code change is integrated and tested automatically to catch issues early.
- **Continuous Deployment (CD)**: Automates the deployment process, ensuring that the latest build is always available for testing or production.


### Pipeline Breakdown:

- **Build Stage**: The project is built using **Gradle** with caching to speed up future builds.
- **Code Quality**: Static code analysis is performed by GitLab's **Code-Quality** template to ensure that the code adheres to quality standards. The code quality results are saved as a report (`gl-code-quality-report.json`).
- **Testing**: 
  - Unit tests are run using **JUnit**, with test reports generated and saved as artifacts.
  - **Jacoco** is used for code coverage reporting.
- **Mutation Testing**: **Pitest** is used for mutation testing, which ensures that tests are meaningful and robust. The mutation testing results are saved as a `.tar.gz` file.
- **HTML Report**: Code quality results are also saved as an HTML file for easier inspection (`gl-code-quality-report.html`).

### Running the CI/CD Pipeline:

The pipeline is automatically triggered on every **commit** or **merge request** to ensure:
- Code is always tested and built before deployment.
- Static code analysis and mutation testing are integrated into the CI process.
- Quality reports are generated for code inspection.

