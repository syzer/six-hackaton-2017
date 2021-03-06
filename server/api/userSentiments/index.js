const axios = require('axios')
const express = require('express')
const { protocol, ip, port } = require('../../config')
const baseUrl = `${protocol}${ip}:${port}`
const sentimentAnalyserUrl = 'http://master-2c6ikency6yo4.eu.platform.sh/sentiment'
const router = express.Router()

const onError = (next) =>
    (error) => {
        console.log(error)
        next(error)
    }

// nodemon -w ./ -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentiment.json'
// nodemon -w ./ -e js -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentimentBadGerman.json'
// nodemon -w ./ -e js -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @api/userSentiments/sentimentGoodGerman.json'
// =>
// {
//     "lang": "en",
//     "sentiment": "good"
// }
// TODO store that stuff in our db...
const postReview = (req, res, next) => {
    const { text, productId, id } = req.body

    // http://master-2c6ikency6yo4.eu.platform.sh/sentiment?text=awesome
    axios.get(sentimentAnalyserUrl, {
        params: {
            text
        }
    }).then(({ data:{ lang, sentiment } }) =>
        axios.post(baseUrl + '/sentiments', { lang, text, productId, sentiment }))
        .then(({ data: { lang, text, productId, sentiment, id } }) =>
            res.json({ lang, text, productId, sentiment, id }))
        .catch(onError(next))
}

router.post('/', postReview)

module.exports = router
