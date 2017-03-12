const axios = require('axios')
const express = require('express')
const { url } = require('../../config')
const { onError } = require('../lib')
const router = express.Router()

const getReviews = (req, res, next) =>
    axios.get(url + '/products/1')
        .then(({ data }) => data)
        .then(product => res.json(product))
        .catch(onError(next))

const postUserAudioReview = (req, res, next) => {
    const id = req.param('id') || 1

    // TODO here call to microsoft

    axios.get(url + `/products/${id}?_embed=reviews&_embed=sentiments`)
        .then(({ data }) => data)
        .then(product => res.json(product))
        .catch(onError(next))
}


router.get('/', getReviews)
router.post('/', postUserAudioReview)

module.exports = router
