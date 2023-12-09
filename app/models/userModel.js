const connection = require('../../config/database');
const bcrypt = require('bcrypt');

const User = {
    createUser: (fullname, email, password, city, callback) => {
        const checkQuery = "SELECT * FROM users WHERE email = ?";
        connection.query(checkQuery, [email], (checkErr, checkResult) => {
            if (checkErr) {
                return callback(checkErr, null);
            }
            
            if (checkResult.length > 0) {
                return callback(new Error('Email is already taken'), null);
            }
            
            // Hash the password and proceed with user registration
            bcrypt.hash(password, 10, (hashErr, hash) => {
                if (hashErr) {
                    return callback(hashErr, null);
                }
                const insertQuery = "INSERT INTO users (fullname, email, password, city) VALUES (?, ?, ?, ?)";
                connection.query(insertQuery, [fullname, email, hash, city], (insertErr, result) => {
                    if (insertErr) {
                        return callback(insertErr, null);
                    } else {
                        callback(null, result);
                    }
                });
            });
        });
    },

    getUserByEmailAndPassword: (email, password, callback) => {
        const query = "SELECT * FROM users WHERE email = ?";
        connection.query(query, [email], (err, rows) => {
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