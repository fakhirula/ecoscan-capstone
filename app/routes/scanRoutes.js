const express = require('express');
const router = express.Router();
const scanController = require('../controllers/scanController');

router.get("/getscans", scanController.getScans);
router.get("/getscan/:id", scanController.getScanById);
router.get("/getscans/user", scanController.getScansByUser);
router.post('/insertAndPredict', scanController.insertAndPredict);
router.delete("/deletescan/:id", scanController.deleteScan);

module.exports = router;