const axios = require('axios')
const express = require('express')
const _ = require('lodash')
const { url } = require('../../config')
const { onError, getData } = require('../lib')
const router = express.Router()

module.exports = io => {

    // TODO user
    io.on('getNotifications', ({ lat, lng, userId }) => {
        console.warn('getting', lat, lng)

    })

    const getNotifications = (req, res, next) => {
        let { lat = 47, lng = 8.5, userId = 1 } = req.query

        // ehh..
        lat = Number(lat)
        lng = Number(lng)
        userId = _.parseInt(userId)

        // return res.json({ lat, lng, userId })
        // find hot nearby hot purchases
        // curl 'localhost:3000/purchases?lat_gte=45&lat_lte=47.9&lng_gte=8&lng_lte=8.5'
        return axios.get(url + '/purchases?lat_gte=45&lat_lte=47.9&lng_gte=8&lng_lte=8.5')
            .then(getData)
            .then(data => res.json(data))
            .catch(onError(next))
    }

    router.get('/', getNotifications)

    console.log('done')

    return router
}

