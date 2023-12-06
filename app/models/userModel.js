// app/models/userModel.js
const connection = require('../../config/database');
const bcrypt = require('bcrypt');

const User = {
    createUser: (fullname, username, password, email, callback) => {
        const checkQuery = "SELECT * FROM users WHERE username = ?";
        connection.query(checkQuery, [username], (checkErr, checkResult) => {
            if (checkErr) {
                return callback({ status: 500, message: 'Internal Server Error' }, null);
            }
            
            if (checkResult.length > 0) {
                return callback({ status: 400, message: 'Username is already taken' }, null);
            }
            
            // Hash the password and proceed with user registration
            bcrypt.hash(password, 10, (hashErr, hash) => {
                if (hashErr) {
                    return callback({ status: 500, message: 'Internal Server Error' }, null);
                }
                const insertQuery = "INSERT INTO users (fullname, username, password, email) VALUES (?, ?, ?, ?)";
                connection.query(insertQuery, [fullname, username, hash, email], (insertErr, result) => {
                    if (insertErr) {
                        return callback({ status: 500, message: 'Internal Server Error' }, null);
                    } else {
                        callback(null, { status: 201, message: 'User registered successfully.', userId: result.insertId });
                    }
                });
            });
        });
    },

    getUserByUsernameAndPassword: (username, password, callback) => {
        const query = "SELECT * FROM users WHERE username = ?";
        connection.query(query, [username], (err, rows) => {
            if (err) {
                return callback(err, null);
            }

            if (rows.length === 0) {
                return callback(null, null);
            }

            const user = rows[0];

            // Compare hashed password
            bcrypt.compare(password, user.password, (err, isMatch) => {
                if (err) {
                    return callback(err, null);
                }

                if (!isMatch) {
                    return callback(null, null);
                }

                // Remove the password field from the user object before passing it to the callback
                delete user.password;
                callback(null, user);
            });
        });
    },

    getAllUsers: (callback) => {
        const query = "SELECT * FROM users";
        connection.query(query, (err, rows) => {
            callback(err, rows);
        });
    }
};

module.exports = User;
