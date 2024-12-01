// carModel.js
const mongoose = require('mongoose');

const CarSchema = new mongoose.Schema({
    ten: {
        type: String,
        required: true
    },
    namSX: {
        type: Number
    },
    hang: {
        type: String,
        required: true
    },
    gia: {
        type: Number
    },
    anh: {
        type: String
    }
});

const CarModel = mongoose.model('car', CarSchema);
module.exports = CarModel;
