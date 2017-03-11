const fs = require('fs')
const path = require('path')
const axios = require('axios')
const express = require('express')
const rp = require('request-promise')
const { onError } = require('../lib')
const { bingSpeechKey } = require('../../config')
const { BingSpeechClient } = require('bingspeech-api-client')

const router = express.Router()

const mockFile = path.join(__dirname, '../../assets/product1-review.wav')
// const mockFile = path.join(__dirname, '../../assets/product2-review.wav')

let client = new BingSpeechClient(bingSpeechKey)

// curl -X POST 'localhost:3000/voice-recognitions' -d @assets/product1-review.wav
// {"transcript": "hi i'm on my way to be available high quality text to speech voices select download not to install my voice"}
const postTranscript = (req, res, next) => {
    client
        .recognizeStream(fs.createReadStream(mockFile))
        .then(({ results }) => res.json(results[0].name))
        .catch(onError(next))
}

router.post('/', postTranscript)

module.exports = router

