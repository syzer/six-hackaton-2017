const axios = require('axios')
const express = require('express')
const { protocol, ip, port } = require('../../config')
const baseUrl = `${protocol}${ip}:${port}`
const router = express.Router()

const onError = (next) =>
    (error) => {
        console.log(error)
        next(error)
    }

const getReviews = (req, res, next) =>
    axios.get(baseUrl + '/products/1')
        .then(({ data }) => data)
        .then(product => res.json(product))
        .catch(onError(next))

const postUserAudioReview = (req, res, next) => {
    const id = req.param('id') || 1

    // TODO here call to microsoft

    // console.warn('resr')
    // res.send('test')

    axios.get(baseUrl + `/products/${id}?_embed=reviews`)
        .then(({ data }) => data)
        .then(product => res.json(product))
        .catch(onError(next))
}


router.get('/', getReviews)
router.post('/', postUserAudioReview)

module.exports = router
