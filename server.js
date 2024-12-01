const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require("body-parser");
const carModel = require('./carModel'); // Đảm bảo đường dẫn đến file model chính xác
const cartModel = require('./cartModel'); // Thêm model giỏ hàng
const apiMobile = require('./api');
const COMMON = require('./COMMON');

const app = express(); // Khởi tạo app trước khi sử dụng

const port = 3000;

// MongoDB URI
const uri = COMMON.uri;

// Kết nối MongoDB
mongoose.connect(uri)
    .then(() => console.log('Kết nối MongoDB thành công'))
    .catch((err) => console.error('Lỗi kết nối MongoDB:', err));

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Định tuyến API
app.use('/api', apiMobile);

// Lấy danh sách xe
app.get('/', async (req, res) => {
    try {
        let cars = await carModel.find();
        console.log(cars);
        res.send(cars);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi lấy dữ liệu xe', error: err.message });
    }
});

// Thêm xe mới
app.post('/add_xe', async (req, res) => {
    try {
        let car = req.body;
        let kq = await carModel.create(car); // Sử dụng `await` để chờ MongoDB thêm xong
        console.log('Thêm xe thành công:', kq);

        let cars = await carModel.find();
        res.send(cars);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi thêm xe', error: err.message });
    }
});

// Xóa xe theo ID
app.delete('/xoa/:id', async (req, res) => {
    let id = req.params.id;

    try {
        let car = await carModel.findById(id);
        if (!car) {
            return res.status(404).send({ message: 'Không thể xóa vì không tìm thấy ID gắn với xe này!' });
        }
        await carModel.deleteOne({ _id: id });
        res.send({ message: 'Xóa xe thành công' });
    } catch (err) {
        res.status(500).send({ message: 'Lỗi xóa xe', error: err.message });
    }
});

// Cập nhật tên xe
/*app.put('/update/:id', async (req, res) => {
    let tenXe = req.params.ten;
    let tenXeMoi = tenXe + ' Phiên bản mới';

    try {
        await carModel.updateOne({ ten: tenXe }, { ten: tenXeMoi });
        let xehois = await carModel.find({}); 
        res.send(xehois);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi cập nhật xe', error: err.message });
    }
});*/

app.put('/update/:id', async (req, res) => {
    let carId = req.params.id; // Sử dụng _id thay vì tên
    let newCarDetails = req.body; // Lấy dữ liệu mới từ body (có thể là tên hoặc các thông tin khác)

    try {
        let car = await carModel.findByIdAndUpdate(carId, newCarDetails, { new: true });
        if (!car) {
            return res.status(404).send({ message: 'Xe không tồn tại!' });
        }
        res.send({ message: 'Cập nhật xe thành công!', car });
    } catch (err) {
        res.status(500).send({ message: 'Lỗi cập nhật xe', error: err.message });
    }
});



// API: Lấy giỏ hàng của người dùng
app.get('/cart/:userId', async (req, res) => {
    try {
        let userId = req.params.userId;
        let cart = await cartModel.findOne({ userId }).populate('items.carId'); // Populate để lấy thông tin xe
        if (!cart) {
            return res.status(404).send({ message: 'Giỏ hàng không tồn tại!' });
        }
        res.send(cart);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi lấy giỏ hàng', error: err.message });
    }
});

// API: Thêm sản phẩm vào giỏ hàng
app.post('/cart/:userId/add', async (req, res) => {
    let userId = req.params.userId;
    let { carId, quantity } = req.body; // Nhận thông tin sản phẩm và số lượng từ body

    try {
        let cart = await cartModel.findOne({ userId });

        if (cart) {
            // Nếu giỏ hàng đã tồn tại, kiểm tra xem sản phẩm có trong giỏ hàng chưa
            let itemIndex = cart.items.findIndex(item => item.carId.toString() === carId);
            if (itemIndex !== -1) {
                // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
                cart.items[itemIndex].quantity += quantity;
            } else {
                // Nếu chưa có, thêm sản phẩm mới vào giỏ hàng
                cart.items.push({ carId, quantity });
            }
        } else {
            // Nếu giỏ hàng chưa có, tạo mới giỏ hàng
            cart = new cartModel({
                userId,
                items: [{ carId, quantity }]
            });
        }

        // Tính lại tổng giá trị giỏ hàng
        let total = 0;
        for (let item of cart.items) {
            let car = await carModel.findById(item.carId);
            total += car.gia * item.quantity;
        }
        cart.total = total;

        await cart.save();
        res.send(cart);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi thêm sản phẩm vào giỏ hàng', error: err.message });
    }
});

// API: Cập nhật số lượng sản phẩm trong giỏ hàng
app.put('/cart/:userId/update', async (req, res) => {
    let userId = req.params.userId;
    let { carId, quantity } = req.body;

    try {
        let cart = await cartModel.findOne({ userId });

        if (!cart) {
            return res.status(404).send({ message: 'Giỏ hàng không tồn tại!' });
        }

        // Cập nhật số lượng sản phẩm
        let itemIndex = cart.items.findIndex(item => item.carId.toString() === carId);
        if (itemIndex !== -1) {
            cart.items[itemIndex].quantity = quantity;
        } else {
            return res.status(404).send({ message: 'Sản phẩm không có trong giỏ hàng!' });
        }

        // Tính lại tổng giá trị giỏ hàng
        let total = 0;
        for (let item of cart.items) {
            let car = await carModel.findById(item.carId);
            total += car.gia * item.quantity;
        }
        cart.total = total;

        await cart.save();
        res.send(cart);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi cập nhật giỏ hàng', error: err.message });
    }
});

// API: Xóa sản phẩm khỏi giỏ hàng
app.delete('/cart/:userId/remove/:carId', async (req, res) => {
    let userId = req.params.userId;
    let carId = req.params.carId;

    try {
        let cart = await cartModel.findOne({ userId });

        if (!cart) {
            return res.status(404).send({ message: 'Giỏ hàng không tồn tại!' });
        }

        // Xóa sản phẩm khỏi giỏ hàng
        cart.items = cart.items.filter(item => item.carId.toString() !== carId);

        // Tính lại tổng giá trị giỏ hàng
        let total = 0;
        for (let item of cart.items) {
            let car = await carModel.findById(item.carId);
            total += car.gia * item.quantity;
        }
        cart.total = total;

        await cart.save();
        res.send(cart);
    } catch (err) {
        res.status(500).send({ message: 'Lỗi xóa sản phẩm khỏi giỏ hàng', error: err.message });
    }
});

// Bắt đầu lắng nghe trên cổng
app.listen(port, () => {
    console.log(`Server đang chạy trên cổng ${port}`);
}); 
