// scanRoutes.js
const express = require('express');
const router = express.Router();
const scanController = require('../controllers/scanController');

// Define your routes using the scanController methods
router.get("/dashboard", scanController.getDashboard);
router.get("/getscans", scanController.getScans);
router.get("/getlast10scans", scanController.getLast10Scans);
router.get("/getscan/:id", scanController.getScanById);
router.post("/insertscan", scanController.insertScan);
router.put("/editscan/:id", scanController.editScan);
router.delete("/deletescan/:id", scanController.deleteScan);
router.post("/uploadImage", scanController.uploadImage);

module.exports = router;
