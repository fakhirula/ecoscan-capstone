const express = require('express');
const router = express.Router();
const { authenticateToken } = require('../middleware/authMiddleware');

router.get('/secureEndpoint', authenticateToken, (req, res) => {
    res.json({ message: 'This is a secure endpoint', user: req.user });
});

module.exports = router;
