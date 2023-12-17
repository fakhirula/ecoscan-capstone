'use strict'
const {Storage} = require('@google-cloud/storage')
const fs = require('fs')
const dateFormat = require('dateformat')
const path = require('path');
const dotenv = require('dotenv');

dotenv.config();

const pathKey = path.resolve('./serviceaccountkey.json')

const gcs = new Storage({
    projectId: process.env.HOST,
    keyFilename: pathKey
})

const bucketName = process.env.BUCKET_NAME
const bucket = gcs.bucket(bucketName)

function getPublicUrl(filename) {
    return 'https://storage.googleapis.com/' + bucketName + '/' + filename;
}

let ImgUpload = {}

ImgUpload.uploadToGcs = (req, res, next) => {
    if (!req.file) return next()

    const gcsname = dateFormat(new Date(), "yyyymmdd-HHMMss")
    const file = bucket.file(gcsname)

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    })

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        next(err)
    })

    stream.on('finish', () => {
        req.file.cloudStorageObject = gcsname
        req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
    })

    stream.end(req.file.buffer)
}

ImgUpload.fileExistsInGcs = (filename, callback) => {
    const file = bucket.file(filename);

    file.exists().then(data => {
        const exists = data[0];
        callback(null, exists);
    }).catch(err => {
        callback(err);
    });
};

ImgUpload.deleteFromGcs = (filename, callback) => {
    const file = bucket.file(filename);

    file.delete((err, apiResponse) => {
        if (err) {
            callback(err);
        } else {
            callback(null, apiResponse);
        }
    });
};

module.exports = ImgUpload