CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS image_scan (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    users_id INT,
    date DATETIME,
    waste_type VARCHAR(255),
    filename VARCHAR(255),
    attachment VARCHAR(255),
    FOREIGN KEY (users_id) REFERENCES users(id)
);