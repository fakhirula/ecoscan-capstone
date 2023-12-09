const connection = require('../../config/database');
const imgUpload = require('../modules/imgUpload');

exports.getScans = (callback) => {
    const query = "SELECT * FROM image_scan"
    connection.query(query, callback);
};

exports.getScanById = (id, callback) => {
    const query = "SELECT * FROM image_scan WHERE id = ?"
    connection.query(query, [id], callback);
};

exports.getScansByUser = (users_id, callback) => {
    const query = "SELECT * FROM image_scan WHERE users_id = ? ORDER BY date DESC LIMIT 10"
    connection.query(query, [users_id], callback);
};

exports.insertScan = (users_id, date, filename, imageUrl, callback) => {
    const query = "INSERT INTO image_scan (users_id, date, filename, attachment) VALUES (?, ?, ?, ?)"
    connection.query(query, [users_id, date, filename, imageUrl], callback);
};

exports.deleteScan = (id, callback) => {
    // Step 1: Get the filename from the database
    const query = "SELECT filename FROM image_scan WHERE id = ?";
    connection.query(query, [id], (err, result) => {
        if (err) {
            callback(err);
        } else if (result.length > 0) {
            const filename = result[0].filename;

            // Check if filename exists
            if (filename) {
                // Step 2: Delete the file from the bucket
                imgUpload.deleteFromGcs(filename, (err, apiResponse) => {
                    if (err) {
                        callback(err);
                    } else {
                        // Step 3: Delete the data from the database
                        const deleteQuery = "DELETE FROM image_scan WHERE id = ?";
                        connection.query(deleteQuery, [id], (err, result) => {
                            if (err) {
                                callback(err);
                            } else {
                                callback(null, result, apiResponse);
                            }
                        });
                    }
                });
            } else {
                // Step 3: Delete the data from the database if there is no filename
                const deleteQuery = "DELETE FROM image_scan WHERE id = ?";
                connection.query(deleteQuery, [id], (err, result) => {
                    if (err) {
                        callback(err);
                    } else {
                        callback(null, result);
                    }
                });
            }
        } else {
            callback(new Error("Scan not found"));
        }
    });
};