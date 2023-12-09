const express = require('express');
const router = express.Router();
const imageController = require('../controllers/scanController');

router.get("/getscans", imageController.getScans);
router.post("/insertscan", imageController.insertScan);
router.post("/uploadImage", imageController.uploadImage);
router.delete("/deletescan/:id", imageController.deleteScan);

module.exports = router;