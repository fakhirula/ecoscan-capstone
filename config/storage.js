// config/storage.js
const { Storage } = require('@google-cloud/storage');
const path = require('path');

const gcs = new Storage({
    projectId: 'submission-ecoscan',
    keyFilename: path.resolve('./serviceaccountkey.json')
});

const bucketName = 'ecoscan-submission';
const bucket = gcs.bucket(bucketName);

module.exports = {
    gcs,
    bucket,
    bucketName
};
