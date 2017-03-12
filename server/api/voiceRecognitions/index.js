const fs = require('fs')
const path = require('path')
const _ = require('lodash')
const axios = require('axios')
const express = require('express')
const rp = require('request-promise')
const { onError, getData } = require('../lib')
const { bingSpeechKey, url } = require('../../config')
const { BingSpeechClient } = require('bingspeech-api-client')

const router = express.Router()

const mockFile = path.join(__dirname, '../../assets/product1-review.wav')
// const mockFile = path.join(__dirname, '../../assets/product2-review.wav')

const sentimentUrl = url + '/user-sentiments'
let client = new BingSpeechClient(bingSpeechKey)

const newReview = (opinion, productId, userId) => {
    // feeling lazy tonight
    opinion.stars = opinion.sentiment === 'good' ? _.random(4, 5) : _.random(0, 3)
    return _.extend(opinion, { productId, userId })
}

// curl -X POST 'localhost:3000/voice-recognitions' -d @assets/product1-review.wav
// {"transcript": "hi i'm on my way to be available high quality text to speech voices select download not to install my voice"}
const postTranscript = (req, res, next) => {
    const { productId = 1, userId = 1 } = req.query

    client
        .recognizeStream(fs.createReadStream(mockFile))
        .then(({ results }) => results[0].name)
        .then(text => ({
            productId,
            userId,
            text
        }))
        .then(transcribed =>
            axios.post(sentimentUrl, transcribed)
                .then(getData))
        // { lang: 'en',
        //   text: 'Awesome knifes',
        //   productId: '1',
        //   sentiment: 'good',
        //   id: 9 }
        .then(sentiment => {
            const review = newReview(sentiment, _.parseInt(productId), _.parseInt(userId))
            return axios.post(url + '/reviews', review).then(getData)
        })
        .then(data => res.json(data))
        .catch(onError(next))
}

router.post('/', postTranscript)

module.exports = router

