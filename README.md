# MagicStoryBook

This is the backend part of the magicstorybook application that supports the creation and management of user-generated stories, integrated with OpenAI for story content and image generation. The application includes secure user authentication with OAuth2, extensive logging, unit testing, and Docker support for deployment.

### Prerequisites

Java 17
Maven 3.8.1+
Docker (for containerization)
OpenAI API Key

### Setup and Run
1. Clone the Repository

2. Set Up Environment Variables

3. Build the Project
   Use Maven to clean and build the project:

`mvn clean package`
4. Run the Application Locally

`java -jar target/magicstorybook-0.0.1-SNAPSHOT.jar`
The application will start on http://localhost:8081.

5. Running Tests

`mvn test`
6. Docker Setup

`docker build -t magicstorybook`
`docker run -p 8081:8081 magicstorybook`


### API Endpoints

#### User Management:

GET /api/user/profile: Fetch the current user's profile.
GET /api/user/{id}: Fetch a user by ID.
GET /api/user/email: Fetch a user by email.
DELETE /api/user/{id}: Delete a user.

#### Story Management:

POST /api/stories/create: Create a new story.
GET /api/stories: Get all stories.
GET /api/stories/{id}: Get a story by ID.
DELETE /api/stories/{id}: Delete a story.
