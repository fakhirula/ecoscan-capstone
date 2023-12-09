const imageModel = require('../models/scanModel');
const imgUpload = require('../modules/imgUpload');
const Multer = require('multer');

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
});

exports.getScans = (req, res) => {
    imageModel.getScans((err, rows) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
        } else {
            res.json(rows)
        }
    });
};

exports.insertScan = [multer.single('attachment'), imgUpload.uploadToGcs, (req, res) => {
    const users_id = req.user.id;
    const fullname = req.user.fullname;
    const date = new Date();
    const filename = req.file.cloudStorageObject;;
    var imageUrl = '';

    if (req.file && req.file.cloudStoragePublicUrl) {
        imageUrl = req.file.cloudStoragePublicUrl
    }

    imageModel.insertScan(users_id, date, filename, imageUrl, (err, result) => {
        if (err) {
            res.status(500).send({message: err.sqlMessage})
        } else {
            res.send({
                    success: true,
                    message: "Insert Successful",
                    result: {
                        userId: users_id,
                        fullname: fullname,
                        date: date,
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
            res.status(500).send({message: err.sqlMessage});
        } else {
            res.send({message: "Delete Successful"});
        }
    });
};