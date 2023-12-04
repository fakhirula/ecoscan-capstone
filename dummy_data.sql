-- Insert data into 'users' table
INSERT INTO users (fullname, username, password, email)
VALUES 
    ('John Doe', 'john_doe', 'password123', 'john.doe@example.com'),
    ('Jane Smith', 'jane_smith', 'securepwd456', 'jane.smith@example.com');

-- Insert data into 'waste_type' table
INSERT INTO waste_type (name)
VALUES 
    ('Plastic'),
    ('Glass'),
    ('Paper');

-- Insert data into 'image_scan' table
INSERT INTO image_scan (users_id, date, attachment)
VALUES 
    (1, '2023-01-01 12:00:00', 'image1.jpg'),
    (2, '2023-02-15 14:30:00', 'image2.jpg');

-- Insert data into 'result' table
INSERT INTO result (image_scan_id, waste_type_id, amount, status)
VALUES 
    (1, 1, 5, 'Recyclable'),
    (1, 2, 3, 'Non-Recyclable'),
    (2, 3, 8, 'Recyclable');
