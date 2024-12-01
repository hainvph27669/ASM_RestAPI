const mongoose = require('mongoose');

const CartItemSchema = new mongoose.Schema({
    carId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'car',  // Tham chiếu đến mô hình 'car' trong carModel.js
        required: true
    },
    quantity: {
        type: Number,
        required: true,
        default: 1
    }
});

const CartSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true,
    },
    items: [CartItemSchema],
    total: {
        type: Number,
        default: 0
    }
});

const Cart = mongoose.model('Cart', CartSchema);
module.exports = Cart;
