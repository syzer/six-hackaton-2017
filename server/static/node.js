const axios = requre('axios')
const sentiment = require('sentiment')
const { protocol, ip, port } = require('../config')


var instance = axios.create({
     baseURL: 'http://localhost:3000',
     timeout: 1000,
     headers: {'X-Custom-Header': 'foobar'}
});

axios.get('/voice-recognition')
    .then(function (response) {
    console.log(response);
    })
    .catch(function (error) {
    console.log(error);
    });


var r1 = sentiment('This is a great product');
console.dir(r1);        // Score: -2, Comparative: -0.666 
 
var r2 = sentiment('This project is not');
console.dir(r2); 