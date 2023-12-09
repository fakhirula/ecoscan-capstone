const express = require('express');
const router = express.Router();
const scanController = require('../controllers/scanController');

router.get("/getscans", scanController.getScans);
router.get("/getscan/:id", scanController.getScanById);
router.get("/scans/user", scanController.getScansByUser);
router.post("/insertscan", scanController.insertScan);
router.post("/uploadImage", scanController.uploadImage);
router.delete("/deletescan/:id", scanController.deleteScan);

module.exports = router;