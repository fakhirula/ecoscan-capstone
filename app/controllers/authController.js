const jwt = require('jsonwebtoken');
const User = require('../models/userModel');
const dotenv = require('dotenv');

dotenv.config();

const register = (req, res) => {
    const { fullname, username, password, email } = req.body;

    User.createUser(fullname, username, password, email, (err, result) => {
        if (err) {
            if (err.message === 'Username is already taken') {
                return res.status(400).json({ success: false, message: 'Registration failed. Username is already taken. Please choose another username.' });
            }

            return res.status(500).json({ success: false, message: 'Internal Server Error' });
        }

        res.status(201).json({ success: true, message: 'User registered successfully.' });
    });
};

const login = (req, res, next) => {
    const { username, password } = req.body;

    User.getUserByUsernameAndPassword(username, password, (err, user) => {
        if (err) {
            return res.status(500).json({ success: false, message: 'Internal Server Error' });
        }

        if (!user) {
            return res.status(401).json({ success: false, message: 'Authentication failed. Invalid username or password.' });
        }

        const token = jwt.sign({ id: user.id, username: user.username }, process.env.JWT_SECRET, { expiresIn: '1h' });

        return res.json({ 
            success: true, 
            loginResult: {
                userId: user.id,
                name: user.username,
                token: token
            } 
        });
    });
};

const profile = (req, res) => {
    res.json({ success: true, user: req.user });
};

const getAllUsers = (req, res) => {
    User.getAllUsers((err, users) => {
        if (err) {
            return res.status(500).json({ success: false, message: 'Internal Server Error' });
        }

        res.json({ success: true, users });
    });
};

module.exports = {
    register,
    login,
    profile,
    getAllUsers
};
