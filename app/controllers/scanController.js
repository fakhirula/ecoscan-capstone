const imageModel = require('../models/scanModel');
const imgUpload = require('../modules/imgUpload');
const Multer = require('multer');
const fs = require('fs');
const path = require('path');
const TeachableMachine = require("@sashido/teachablemachine-node");

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
});

const model = new TeachableMachine({
  modelUrl: "https://teachablemachine.withgoogle.com/models/r_FJjjBKN/" // Recycle and Organic
});

exports.getScans = (req, res) => {
    imageModel.getScans((err, scans) => {
        if(err) {
            res.status(500).send({message: 'Oops! Something went wrong on our end. Please try again later.'})
        } else {
            if(scans.length === 0) {
                res.status(200).send({
                    success: false,
                    message: 'No scans found'
                });
            } else {
                res.status(200).json({
                    success: true,
                    message: "Scans fetched successfully",
                    listScans: scans
                });
            }
        }
    });
};

exports.getScanById = (req, res) => {
    const id = req.params.id;
    imageModel.getScanById(id, (err, scan) => {
        if(err) {
            res.status(500).send({message: 'Oops! Something went wrong on our end. Please try again later.'})
        } else if (scan.length > 0) {
            res.status(200).json(scan[0]);
        } else {
            res.status(404).send({
                success: false,
                message: 'Scan not found.'
            });
        }
    });
};

exports.getScansByUser = (req, res) => {
    const users_id = req.user.id;
    imageModel.getScansByUser(users_id, (err, listScans) => {
        if (err) {
            res.status(500).send({ message: 'Oops! Something went wrong on our end. Please try again later.' });
        } else {
            if (listScans.length === 0) {
                res.json({
                    success: false,
                    message: "No scans found"
                });
            } else {
                res.json({
                    success: true,
                    message: "Scans fetched successfully",
                    listScans: listScans
                });
            }
        }
    });
};

exports.predictImage = async (req, res, next) => {
    try {
        const { url } = req.query;

        if (!url || typeof url !== 'string') {
            return res.status(400).json({ message: 'Invalid or missing URL parameter. Please provide a valid image URL.' });
        }

        const predictions = await model.classify({
            imageUrl: url,
        });

        console.log("Predictions:", predictions);
        res.json(predictions);

    } catch (e) {
        console.error("ERROR", e);
        if (e instanceof TypeError) {
            res.status(400).json({ message: 'An error occurred. Please ensure the URL is correct and points to a valid image.' });
        } else {
            res.status(500).json({ message: 'An unexpected error occurred on our end. Please try again later.' });
        }
    }
};

exports.insertScan = [multer.single('attachment'), imgUpload.uploadToGcs, (req, res) => {
    const users_id = req.user.id;
    const fullname = req.user.fullname;
    const date = new Date();
    var waste_type = null;
    const filename = req.file ? req.file.cloudStorageObject : '';
    var imageUrl = '';

    if (!req.file) {
        res.status(400).send({message: 'No image file was provided. Please upload an image.'});
        return;
    }

    if (req.file && req.file.cloudStoragePublicUrl) {
        imageUrl = req.file.cloudStoragePublicUrl
    }

    imageModel.insertScan(users_id, date, waste_type, filename, imageUrl, (err, result) => {
        if (err) {
            res.status(500).send({message: 'Oops! Something went wrong on our end. Please try again later.'})
        } else {
            res.send({
                success: true,
                message: "Image uploaded successfully",
                result: {
                    userId: users_id,
                    fullname: fullname,
                    date: date,
                    waste_type: waste_type,
                    filename: filename,
                    url: imageUrl
                }
            });
        }
    });
}];

exports.uploadImage = [multer.single('image'), imgUpload.uploadToGcs, (req, res, next) => {
    const data = req.body
    if (req.file && req.file.cloudStoragePublicUrl) {
        data.imageUrl = req.file.cloudStoragePublicUrl
    }

    res.send(data)
}];

exports.deleteScan = (req, res) => {
    const id = req.params.id;

    imageModel.deleteScan(id, (err, result) => {
        if (err) {
            if (err.message === "Scan not found") {
                res.status(404).send({message: 'Scan not found.'});
            } else {
                res.status(500).send({message: 'Oops! Something went wrong on our end. Please try again later.'});
            }
        } else {
            res.send({message: "Image deleted successfully"});
        }
    });
};