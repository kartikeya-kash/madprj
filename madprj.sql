-- Step 1: Create the database
CREATE DATABASE madprjdb;

-- Step 2: Use the database
USE madprjdb;

-- Step 3: Create table `users`
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT,
    weight FLOAT,
    gender VARCHAR(10),
    height FLOAT,
    HG_DailyCal INT,
    HG_Sleep FLOAT,
    HG_Water FLOAT,
    HG_Steps INT,
    MI_BloodType VARCHAR(10),
    MI_Allergies VARCHAR(255),
    MI_MedicalCondition VARCHAR(255)
);


INSERT INTO users (
    name, email, password, age, weight, gender, height,
    HG_DailyCal, HG_Sleep, HG_Water, HG_Steps,
    MI_BloodType, MI_Allergies, MI_MedicalCondition
)
VALUES (
    'Aarav Mehta', 
    'aarav@example.com', 
    'password123', 
    24, 
    68.5, 
    'Male', 
    175.2, 
    2200, 
    7.5, 
    2.8, 
    8500, 
    'B+', 
    'Dust', 
    'Asthma'
);


INSERT INTO users (
    name, email, password, age, weight, gender, height,
    HG_DailyCal, HG_Sleep, HG_Water, HG_Steps,
    MI_BloodType, MI_Allergies, MI_MedicalCondition
)
VALUES (
    'Kartikeya Sharma', 
    'kartikeya.anjul@gmail.com', 
    'pass', 
    20, 
    85, 
    'Male', 
    170.2, 
    2200, 
    7.5, 
    2.8, 
    8500, 
    'A+', 
    'Dust', 
    'NONE'
);

select * from users;


CREATE TABLE user_calorie_intake (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    calorie_intake INT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE
);