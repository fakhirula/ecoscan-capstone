// app.js
require('@google-cloud/debug-agent').start();

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const dotenv = require('dotenv');

dotenv.config();

const PORT = process.env.PORT || 8000;

const scanRouter = require('./app/routes/scanRoutes');

app.use(bodyParser.urlencoded({ extended: true }));

// Use scanRouter for scan-related routes
app.use(scanRouter);

app.get("/", (req, res) => {
    console.log("Response success");
    res.send("Response Traffic-2 Success!");
});

app.listen(PORT, () => {
    console.log("Server is up and listening on " + PORT);
});