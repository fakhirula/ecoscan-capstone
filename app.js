require('@google-cloud/debug-agent').start();
const express = require('express');
const bodyParser = require('body-parser');
const dotenv = require('dotenv');

const authRoutes = require('./app/routes/authRoutes');
const scanRoutes = require('./app/routes/scanRoutes');
const { authenticateToken } = require('./app/middleware/authMiddleware');

dotenv.config();

const app = express();
const PORT = process.env.PORT || 8000;

// Middleware
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Routes
app.use('/auth', authRoutes);
app.use('/api', authenticateToken, scanRoutes);

// Default route
app.get("/", (req, res) => {
    res.send("Response Success!");
});

// Start server
app.listen(PORT, () => {
    console.log(`Server is up and listening on ${PORT}`);
});