const axios = require('axios')
const { protocol, ip, port } = require('../../config')
const baseUrl = `${protocol}${ip}:${port}`

const getReviews = (req, res, next) =>
    axios.get(baseUrl + '/products/1')
        .then(({ data }) => data)
        .then(product => res.json(product))
        .catch((error) => {
            console.log(error)
            next(error)
        })

module.exports = getReviews
