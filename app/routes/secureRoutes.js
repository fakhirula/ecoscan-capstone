// app/routes/secureRoutes.js
const express = require('express');
const router = express.Router();
const { authenticateToken } = require('../middleware/authMiddleware');

router.get('/secureEndpoint', authenticateToken, (req, res) => {
    // Hanya dapat diakses jika token valid
    res.json({ message: 'This is a secure endpoint', user: req.user });
});

module.exports = router;
