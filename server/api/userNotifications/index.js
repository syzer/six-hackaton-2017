const axios = require('axios')
const express = require('express')
const _ = require('lodash')
const { url } = require('../../config')
const { onError, getData } = require('../lib')
const router = express.Router()

const mock = {
    "id": 9,
    "userId": 4,
    "productId": 5,
    "lat": 47.3769,
    "lng": 8.2,
    "product": {
        "id": 5,
        "name": "Kaffeeloffel PRIMO",
        "price": 0.45,
        "discount": 0.5,
        "picture": "http://localhost:3000/assets/product5.png",
        "reviewIds": {
            "id": 1,
            "author": "Amazing Jeff",
            "userId": 1,
            "review": "This product is awesome!",
            "sentiment": "good",
            "productId": 1,
            "stars": 4
        },
        "sentimentIds": []
    }
}


module.exports = io => {

    // TODO user
    io.on('getNotifications', ({ lat = 47, lng = 8.5, userId = 1 }) => {
        console.warn('getting', lat, lng)
        io.emit('newHotDeals', [mock])
    })

    const getNotifications = (req, res, next) => {
        let { lat = 47, lng = 8.5, userId = 1 } = req.query

        // ehh..
        lat = Number(lat)
        lng = Number(lng)
        userId = _.parseInt(userId)

        const nearby = ({ lat, lng }, r = 0.5) =>
            `lat_gte=${lat - r}&lat_lte=${lat + r}&lng_gte=${lng - r}&lng_lte=${lng + r}`

        // return res.json({ lat, lng, userId })
        // find hot nearby hot purchases
        // curl 'localhost:3000/purchases?lat_gte=45&lat_lte=47.9&lng_gte=8&lng_lte=8.5&_expand=product'
        return axios.get(url + `/purchases?${nearby({ lat, lng })}&_expand=product`)
            .then(getData)
            .then(data => res.json(data))
            .catch(onError(next))
    }

    router.get('/', getNotifications)

    console.log('done')

    return router
}

