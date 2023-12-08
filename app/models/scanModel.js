// app/models/scanModel.js
const connection = require('../../config/database');

const Scan = {
    getScans: (callback) => {
        const query = "SELECT * FROM image_scan";
        connection.query(query, (err, rows, field) => {
            callback(err, rows);
        });
    },

    getLast10Scans: (callback) => {
        const query = "SELECT * FROM image_scan ORDER BY date DESC LIMIT 10";
        connection.query(query, (err, rows, field) => {
            callback(err, rows);
        });
    },

    getScanById: (id, callback) => {
        const query = "SELECT * FROM image_scan WHERE id = ?";
        connection.query(query, [id], (err, rows, field) => {
            callback(err, rows);
        });
    },

    insertScan: (users_id, date, imageUrl, callback) => {
        const query = "INSERT INTO image_scan (users_id, date, attachment) VALUES (?, ?, ?)";
        connection.query(query, [users_id, date, imageUrl], (err, result, fields) => {
            callback(err, result);
        });
    },

    editScan: (id, users_id, date, imageUrl, callback) => {
        const query = "UPDATE image_scan SET users_id = ?, date = ?, attachment = ? WHERE id = ?";
        connection.query(query, [users_id, date, imageUrl, id], (err, result, fields) => {
            callback(err, result);
        });
    },

    deleteScan: (id, callback) => {
        const query = "DELETE FROM image_scan WHERE id = ?";
        connection.query(query, [id], (err, result, fields) => {
            callback(err, result);
        });
    }
};

module.exports = Scan;
