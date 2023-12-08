// app/controllers/scanController.js
const Scan = require('../models/scanModel');
const ImgUpload = require('../modules/imgUpload');

const scanController = {
    getDashboard: (req, res) => {
        // Implement logic for the dashboard route
    },

    getScans: (req, res) => {
        Scan.getScans((err, scans) => {
            if (err) {
                res.status(500).send({ message: err.sqlMessage });
            } else {
                res.json(scans);
            }
        });
    },

    getLast10Scans: (req, res) => {
        Scan.getLast10Scans((err, scans) => {
            if (err) {
                res.status(500).send({ message: err.sqlMessage });
            } else {
                res.json(scans);
            }
        });
    },

    getScanById: (req, res) => {
        const id = req.params.id;
        Scan.getScanById(id, (err, scan) => {
            if (err) {
                res.status(500).send({ message: err.sqlMessage });
            } else {
                res.json(scan);
            }
        });
    },

    insertScan: (req, res) => {
        const users_id = req.user.id;
        const username = req.user.username;
        const date = new Date();
        const imageUrl = req.file ? req.file.cloudStoragePublicUrl : '';

        Scan.insertScan(users_id, date, imageUrl, (err, result) => {
            if (err) {
                res.status(500).send({ success: false, message: err.sqlMessage });
            } else {
                res.send({
                    success: true,
                    message: "Insert Successful",
                    result: {
                        userId: users_id,
                        name: username,
                        date: date,
                        url: imageUrl,
                        file: req.file // #########################################################
                    }
                });
            }
        });
    },

    editScan: (req, res) => {
        const id = req.params.id;
        const users_id = req.body.users_id;
        const date = req.body.date;
        const imageUrl = req.file ? req.file.cloudStoragePublicUrl : '';

        Scan.editScan(id, users_id, date, imageUrl, (err, result) => {
            if (err) {
                res.status(500).send({ message: err.sqlMessage });
            } else {
                res.send({ message: "Update Successful" });
            }
        });
    },

    deleteScan: (req, res) => {
        const id = req.params.id;
        Scan.deleteScan(id, (err, result) => {
            if (err) {
                res.status(500).send({ message: err.sqlMessage });
            } else {
                res.send({ message: "Delete successful" });
            }
        });
    },

    uploadImage: (req, res, next) => {
        ImgUpload.uploadToGcs(req, res, next);
    }
};

module.exports = scanController;
