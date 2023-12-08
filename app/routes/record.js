const express = require('express')
const mysql = require('mysql')
const router = express.Router()
const Multer = require('multer')
const imgUpload = require('../modules/imgUpload')
const dotenv = require('dotenv');

dotenv.config();

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
})

// TODO: Sesuaikan konfigurasi database
const connection = mysql.createConnection({
    host: process.env.HOST,
    user: process.env.USER,
    database: process.env.DATABASE,
    password: process.env.PASSWORD
})

router.get("/getrecords", (req, res) => {
    const query = "SELECT * FROM image_scan"
    connection.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
        } else {
            res.json(rows)
        }
    })
})

router.post("/insertrecord", multer.single('attachment'), imgUpload.uploadToGcs, (req, res) => {
    console.log('Filename after middleware:', req.file.gcsname)
    const users_id = 1;
    const date = new Date();
    const filename = req.file.cloudStorageObject;;
    var imageUrl = '';

    if (req.file && req.file.cloudStoragePublicUrl) {
        imageUrl = req.file.cloudStoragePublicUrl
    }

    const query = "INSERT INTO image_scan (users_id, date, filename, attachment) VALUES (?, ?, ?, ?)"

    connection.query(query, [users_id, date, filename, imageUrl], (err, result, fields) => {
        if (err) {
            res.status(500).send({message: err.sqlMessage})
        } else {
            res.send({
                    success: true,
                    message: "Insert Successful",
                    result: {
                        userId: users_id,
                        date: date,
                        filename: filename,
                        url: imageUrl
                    }
                });
        }
    })
})

router.post("/uploadImage", multer.single('image'), imgUpload.uploadToGcs, (req, res, next) => {
    const data = req.body
    if (req.file && req.file.cloudStoragePublicUrl) {
        data.imageUrl = req.file.cloudStoragePublicUrl
    }

    res.send(data)
})

router.delete("/deleterecord/:id", (req, res) => {
    const id = req.params.id;

    // Langkah 1: Ambil nama file dari database
    const query = "SELECT filename FROM image_scan WHERE id = ?";
    connection.query(query, [id], (err, result) => {
        if (err) {
            res.status(500).send({message: err.sqlMessage});
        } else if (result.length > 0) {
            const filename = result[0].filename;

            // Cek jika filename ada
            if (filename) {
                // Langkah 2: Hapus file dari bucket
                imgUpload.deleteFromGcs(filename, (err, apiResponse) => {
                    if (err) {
                        res.status(500).send({message: err.message});
                    } else {
                        // Langkah 3: Hapus data dari database
                        const deleteQuery = "DELETE FROM image_scan WHERE id = ?";
                        connection.query(deleteQuery, [id], (err, result) => {
                            if (err) {
                                res.status(500).send({message: err.sqlMessage});
                            } else {
                                res.send({message: "Delete Successful", response: apiResponse});
                            }
                        });
                    }
                });
            } else {
                // Langkah 3: Hapus data dari database jika tidak ada filename
                const deleteQuery = "DELETE FROM image_scan WHERE id = ?";
                connection.query(deleteQuery, [id], (err, result) => {
                    if (err) {
                        res.status(500).send({message: err.sqlMessage});
                    } else {
                        res.send({message: "Delete Successful"});
                    }
                });
            }
        } else {
            res.status(404).send({message: "Record not found"});
        }
    });
});

module.exports = router