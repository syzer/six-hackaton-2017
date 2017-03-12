const axios = require('axios')
const express = require('express')
const _ = require('lodash')
const { url } = require('../../config')
const { onError, getData } = require('../lib')
const router = express.Router()

const nearby = ({ lat, lng }, r = 0.5) =>
    `lat_gte=${lat - r}&lat_lte=${lat + r}&lng_gte=${lng - r}&lng_lte=${lng + r}&_expand=product`

const getHotDeals = ({ lat, lng }) =>
    axios.get(url + `/purchases?${nearby({ lat, lng })}&_expand=product`)
        .then(getData)

module.exports = io => {

    // TODO user
    io.on('getNotifications', ({ lat = 47, lng = 8.5, userId = 1 }) => {
        console.warn('getting', lat, lng)

        getHotDeals({lat, lng}).then(deals =>
            io.emit('newHotDeals', deals))
    })

    const getNotifications = (req, res, next) => {
        let { lat = 47, lng = 8.5, userId = 1 } = req.query

        // ehh..
        lat = Number(lat)
        lng = Number(lng)
        userId = _.parseInt(userId)

        // return res.json({ lat, lng, userId })
        // find hot nearby hot purchases
        // curl 'localhost:3000/purchases?lat_gte=45&lat_lte=47.9&lng_gte=8&lng_lte=8.5&_expand=product'
        return getHotDeals({ lat, lng })
            .then(data => res.json(data))
            .catch(onError(next))
    }

    router.get('/', getNotifications)

    console.log('done')

    return router
}

