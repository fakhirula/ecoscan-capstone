require('@google-cloud/debug-agent').start()

const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const dotenv = require('dotenv');

dotenv.config();
const recordRouter = require('./app/routes/record')

app.use(bodyParser.urlencoded({extended: true}))
app.use(recordRouter)

app.get("/", (req, res) => {
    console.log("Response success")
    res.send("Response Success!")
})

const PORT = process.env.PORT || 8000
app.listen(PORT, () => {
    console.log("Server is up and listening on " + PORT)
})