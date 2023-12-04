// config/database.js
const mysql = require('mysql');

const connection = mysql.createConnection({
    host: '34.101.88.100',
    user: 'root',
    database: 'ecoscandb',
    password: 'ecoscan606'
});

module.exports = connection;
