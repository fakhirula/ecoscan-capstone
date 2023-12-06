require('@google-cloud/debug-agent').start();

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const dotenv = require('dotenv');
const authRoutes = require('./app/routes/authRoutes');
const scanRouter = require('./app/routes/scanRoutes');

// Load environment variables from .env file
dotenv.config();

const PORT = process.env.PORT || 8000;

app.use(bodyParser.urlencoded({ extended: true }));
app.use('/auth', authRoutes);
app.use('/scan', scanRouter);

app.get("/", (req, res) => {
    console.log("Response success");
    res.send("Response Traffic Success!");
});

app.listen(PORT, () => {
    console.log("Server is up and listening on " + PORT);
});
