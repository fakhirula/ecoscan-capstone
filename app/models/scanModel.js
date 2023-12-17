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
    const query = "SELECT * FROM image_scan WHERE users_id = ? ORDER BY date DESC"
    connection.query(query, [users_id], callback);
};

exports.insertScan = (users_id, date, waste_type, filename, imageUrl, callback) => {
    const query = "INSERT INTO image_scan (users_id, date, waste_type, filename, attachment) VALUES (?, ?, ?, ?, ?)"
    connection.query(query, [users_id, date, waste_type, filename, imageUrl], callback);
};

exports.updateWasteType = (scanId, wasteType, callback) => {
    const query = "UPDATE image_scan SET waste_type = ? WHERE id = ?";
    connection.query(query, [wasteType, scanId], callback);
};

exports.deleteScan = (id, callback) => {
    const query = "SELECT filename FROM image_scan WHERE id = ?";
    connection.query(query, [id], (err, result) => {
        if (err) {
            callback(err);
        } else if (result.length > 0) {
            const filename = result[0].filename;

            // Check if filename exists
            if (filename) {
                // Check if file exists in the bucket
                imgUpload.fileExistsInGcs(filename, (err, exists) => {
                    if (err) {
                        callback(err);
                    } else if (exists) {
                        // Delete the file from the bucket
                        imgUpload.deleteFromGcs(filename, (err, apiResponse) => {
                            if (err) {
                                callback(err);
                            } else {
                                // Delete the data from the database
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
                        // Delete the data from the database if the file does not exist in the bucket
                        const deleteQuery = "DELETE FROM image_scan WHERE id = ?";
                        connection.query(deleteQuery, [id], (err, result) => {
                            if (err) {
                                callback(err);
                            } else {
                                callback(null, result);
                            }
                        });
                    }
                });
            } else {
                // Delete the data from the database if there is no filename
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