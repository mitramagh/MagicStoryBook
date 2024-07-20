CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE stories (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    genre VARCHAR(50),
    setting VARCHAR(50),
    character VARCHAR(50),
    title VARCHAR(100),
    content TEXT,
    age_range VARCHAR(10),
    word_range VARCHAR(10),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
