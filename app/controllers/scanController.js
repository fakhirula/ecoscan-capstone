const imageModel = require('../models/scanModel');
const imgUpload = require('../modules/imgUpload');
const Multer = require('multer');
const fs = require('fs');
const path = require('path');
const TeachableMachine = require("@sashido/teachablemachine-node");
const sharp = require('sharp');

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
});

const model = new TeachableMachine({
  modelUrl: "https://teachablemachine.withgoogle.com/models/tEesVahx2/" // Recycle and Organic
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

exports.insertAndPredict = [multer.single('attachment'), async (req, res, next) => {
    if (!req.file) {
        res.status(400).send({message: 'No image file was provided. Please upload an image.'});
        return;
    }

    // Compress the image
    try {
        const image = sharp(req.file.buffer).resize(800);
        let compressedImageBuffer;

        if (req.file.mimetype === 'image/png') {
            compressedImageBuffer = await image.png({ quality: 80 }).toBuffer();
        } else if (req.file.mimetype === 'image/jpeg') {
            compressedImageBuffer = await image.jpeg({ quality: 80 }).toBuffer();
        } else {
            throw new Error('Unsupported image format. Please upload a JPEG or PNG image.');
        }

        req.file.buffer = compressedImageBuffer;

        next();
    } catch (compressionError) {
        console.error("Compression error:", compressionError);
        res.status(500).json({message: 'An unexpected error occurred during image compression. Please try again later.'});
    }
}, imgUpload.uploadToGcs, async (req, res) => {
    const users_id = req.user.id;
    const fullname = req.user.fullname;
    const date = new Date();
    var waste_type = null;
    const filename = req.file ? req.file.cloudStorageObject : '';
    var imageUrl = '';

    if (req.file && req.file.cloudStoragePublicUrl) {
        imageUrl = req.file.cloudStoragePublicUrl;
    }

    // Insert the scan
    imageModel.insertScan(users_id, date, waste_type, filename, imageUrl, async (err, result) => {
        if (err) {
            res.status(500).send({message: 'Oops! Something went wrong on our end. Please try again later.'});
        } else {
            const insertedScanId = result.insertId;

            // Predict the image class
            try {
                const predictions = await model.classify({
                    imageUrl: imageUrl,
                });

                if (predictions && predictions.length > 0) {
                    const predictedClass = predictions[0].class;

                    // Update the waste_type in the inserted scan
                    imageModel.updateWasteType(insertedScanId, predictedClass, (updateErr, updateResult) => {
                        if (updateErr) {
                            console.error("Error updating waste type:", updateErr);
                        }
                    });

                    res.send({
                        success: true,
                        message: 'Image uploaded and classified successfully.',
                        result: {
                            userId: users_id,
                            fullname: fullname,
                            date: date,
                            waste_type: predictedClass,
                            filename: filename,
                            url: imageUrl
                        }
                    });
                } else {
                    res.json({
                        success: false,
                        message: 'No predictions could be made. Please ensure the image is clear and try again.'
                    });
                }
            } catch (predictionError) {
                console.error("Prediction error:", predictionError);
                res.status(500).json({message: 'An unexpected error occurred during prediction. Please try again later.'});
            }
        }
    });
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