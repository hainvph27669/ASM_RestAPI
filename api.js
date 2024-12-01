const express = require('express');

const router = express.Router();

const carModel = require('./carModel');
const mongoose = require('mongoose');

const COMMON = require('./COMMON');

//check 
router.get('/', (req, res) => {
    res.send('vao api mobile');
})


// hiển thị danh sach
router.get('/list', async (req, res) => {

    await mongoose.connect(COMMON.uri);

    let cars = await carModel.find();

    console.log(cars);

    res.send(cars);

})

const bodyParser = require("body-parser");
router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended: true}));

// add xe
router.post('/add_xe', async(req, res)=>{
    await mongoose.connect(COMMON.uri);

    let car = req.body;

    console.log(car);

    let kq = await carModel.create(car);
    console.log(kq)

    let cars = await carModel.find();
    res.send(cars);

})




router.delete('/xoa_xe/:id', async (req, res) => {
    const id = req.params.id;

    try {
        const car = await carModel.findByIdAndDelete(id);

        if (!car) {
            return res.status(404).json({ message: 'Không tìm thấy xe với ID này.' });
        }

        res.json({ message: 'Xóa xe thành công', data: car });
    } catch (err) {
        res.status(500).json({ message: 'Lỗi khi xóa xe', error: err.message });
    }
});

//update
router.put('/update', async(req,res)=>{
    await mongoose.connect(COMMON.uri)

    let car = req.body
    console.log(car)

    //await carModel.updateOne({ten: tenXe}, {ten: tenXeMoi});

    let xehois = await carModel.find({})

    res.json(xehois);
})










module.exports = router;


