require('@google-cloud/debug-agent').start();

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const dotenv = require('dotenv');

dotenv.config();

const authRoutes = require('./app/routes/authRoutes');
const scanRoutes = require('./app/routes/scanRoutes');
const { authenticateToken } = require('./app/middleware/authMiddleware');

const PORT = process.env.PORT || 8000;

app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/auth', authRoutes);
app.use('/api', authenticateToken, scanRoutes);

app.get("/", (req, res) => {
    console.log("Response success");
    res.send("Response Traffic Success!");
});

app.listen(PORT, () => {
    console.log("Server is up and listening on " + PORT);
});