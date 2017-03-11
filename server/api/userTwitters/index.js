const axios = require('axios')
const express = require('express')
const { protocol, ip, port } = require('../../config')
const { onError } = require('../lib')
const twitterUrl = `http://master-2c6ikency6yo4.eu.platform.sh/`

const baseUrl = `${protocol}${ip}:${port}`
const router = express.Router()


// nodemon -w ./ -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentiment.json'
// nodemon -w ./ -e js -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentimentBadGerman.json'
// nodemon -w ./ -e js -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentimentGoodGerman.json'
// =>
// {
//     "lang": "en",
//     "sentiment": "good"
// }
const getTwits = (req, res, next) => {
    const { twitter } = req.query

    // http://master-2c6ikency6yo4.eu.platform.sh/?twitter=coop_ch
    return res.json('dasdas' + twitter)

    axios.get(twitterUrl, {
        params: {
            twitter: 'coop_ch'
        }
    }).then(({ data:{ lang, sentiment } }) =>
        axios.post(baseUrl + '/sentiments', {
            lang,
            text,
            productId,
            sentiment
        }))
        .then(({ data }) => res.json(data))
        .catch(onError(next))
}

router.get('/', getTwits)

module.exports = router

