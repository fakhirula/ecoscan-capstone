const jwt = require('jsonwebtoken');
const User = require('../models/userModel');
const dotenv = require('dotenv');

dotenv.config();

const register = (req, res) => {
    const { fullname, email, password, city } = req.body;

    User.createUser(fullname, email, password, city, (err, result) => {
        if (err) {
            if (err.message === 'Email is already taken') {
                return res.status(409).json({ success: false, message: 'Registration failed. This email is already registered. Please use a different email.' });
            }

            return res.status(500).json({ success: false, message: 'Oops! Something went wrong on our end. Please try again later.' });
        }

        res.status(201).json({ success: true, message: 'Registration successful. Welcome aboard!' });
    });
};

const login = (req, res, next) => {
    const { email, password } = req.body;

    User.getUserByEmailAndPassword(email, password, (err, user) => {
        if (err) {
            return res.status(500).json({ success: false, message: 'Oops! Something went wrong on our end. Please try again later.' });
        }

        if (!user) {
            return res.status(401).json({ success: false, message: 'Login failed. Invalid email or password. Please try again.' });
        }

        const token = jwt.sign({ id: user.id, email: user.email }, process.env.JWT_SECRET, { expiresIn: '7d' });

        return res.json({ 
            success: true, 
            message: "Login successful. Enjoy your day!",
            userDetail: {
                userId: user.id,
                fullname: user.fullname,
                email: user.email,
                token: token
            } 
        });
    });
};

const profile = (req, res) => {
    res.json({ success: true, userProfile: req.user });
};

const getAllUsers = (req, res) => {
    User.getAllUsers((err, users) => {
        if (err) {
            return res.status(500).json({ success: false, message: 'Oops! Something went wrong on our end. Please try again later.' });
        }

        res.json({ success: true, userList: users });
    });
};

module.exports = {
    register,
    login,
    profile,
    getAllUsers
};
