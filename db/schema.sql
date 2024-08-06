CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

CREATE TABLE stories (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    genre VARCHAR(50),
    setting VARCHAR(50),
    title VARCHAR(100),
    specialMessage VARCHAR(255),
    content TEXT,
    image Text,
    age_range VARCHAR(10),
    word_range VARCHAR(10),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE story_characters (
    id SERIAL PRIMARY KEY,
    story_id INT REFERENCES stories(id) ON DELETE CASCADE,
    character VARCHAR(50)
);
