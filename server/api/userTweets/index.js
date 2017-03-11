const axios = require('axios')
const express = require('express')
const { protocol, ip, port } = require('../../config')
const { onError } = require('../lib')
const twitterUrl =  (user) => `http://master-2c6ikency6yo4.eu.platform.sh/twitter_sentiment/${user}`

const baseUrl = `${protocol}${ip}:${port}`
const router = express.Router()


// curl 'localhost:3000/user-tweets?twitter=syzer3'
const getTwits = (req, res, next) => {
    const { twitter } = req.query

    return axios.get(twitterUrl(twitter))
        .then(({ data} ) => res.json(data))
        .catch(onError(next))
}

router.get('/', getTwits)

module.exports = router

