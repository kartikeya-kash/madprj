-- Step 1: Create the database
CREATE DATABASE madprjdb;

-- Step 2: Use the database
USE madprjdb;

-- Step 3: Create table `users`
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (name, email, password)
VALUES ('Kartik', 'kartik@gmail.com', '123456');