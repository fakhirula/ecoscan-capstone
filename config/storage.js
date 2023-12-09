const { Storage } = require('@google-cloud/storage');
const path = require('path');
const dotenv = require('dotenv');

dotenv.config();

const gcs = new Storage({
    projectId: process.env.PROJECT_ID,
    keyFilename: path.resolve('./serviceaccountkey.json')
});

const bucketName = process.env.BUCKET_NAME;
const bucket = gcs.bucket(bucketName);

module.exports = {
    gcs,
    bucket,
    bucketName
};