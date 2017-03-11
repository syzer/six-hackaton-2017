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

// nodemon -w ./ -d 1 -x 'curl localhost:3000/user-sentiments -H "Content-Type: application/json" -d @userSentiments/sentiment.json'
// =>
// {
//     "lang": "en",
//     "sentiment": "good"
// }
// TODO store that stuff in our db...
const postReview = (req, res, next) => {
    const { text, productId, id } = req.body
    console.log(productId, id)

    // http://master-2c6ikency6yo4.eu.platform.sh/sentiment?text=asdaasd%20awesome
    axios.get(sentimentAnalyserUrl, {
        params: {
            text
        }
    }).then(({ data }) => res.json(data))
        .catch(onError(next))
}

router.post('/', postReview)

module.exports = router
