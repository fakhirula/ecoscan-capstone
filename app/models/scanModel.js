const connection = require('../../config/database');
const imgUpload = require('../modules/imgUpload');

exports.getScans = (callback) => {
    const query = "SELECT * FROM image_scan"
    connection.query(query, callback);
};

exports.insertScan = (users_id, date, filename, imageUrl, callback) => {
    const query = "INSERT INTO image_scan (users_id, date, filename, attachment) VALUES (?, ?, ?, ?)"
    connection.query(query, [users_id, date, filename, imageUrl], callback);
};

exports.deleteScan = (id, callback) => {
    // Langkah 1: Ambil nama file dari database
    const query = "SELECT filename FROM image_scan WHERE id = ?";
    connection.query(query, [id], (err, result) => {
        if (err) {
            callback(err);
        } else if (result.length > 0) {
            const filename = result[0].filename;

            // Cek jika filename ada
            if (filename) {
                // Langkah 2: Hapus file dari bucket
                imgUpload.deleteFromGcs(filename, (err, apiResponse) => {
                    if (err) {
                        callback(err);
                    } else {
                        // Langkah 3: Hapus data dari database
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
                // Langkah 3: Hapus data dari database jika tidak ada filename
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