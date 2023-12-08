CREATE TABLE records (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(25) NOT NULL,
    amount DOUBLE NOT NULL,
    date DATETIME NOT NULL,
    notes TEXT,
    attachment VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS waste_type (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS image_scan (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    users_id INT,
    date DATETIME,
    filename VARCHAR(255),
    attachment VARCHAR(255),
    FOREIGN KEY (users_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS result (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    image_scan_id INT,
    waste_type_id INT,
    amount INT,
    status VARCHAR(255),
    FOREIGN KEY (image_scan_id) REFERENCES image_scan(id),
    FOREIGN KEY (waste_type_id) REFERENCES waste_type(id)
);