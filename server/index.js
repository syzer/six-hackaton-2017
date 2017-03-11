const fs = require('fs')
const path = require('path')
const jsonServer = require('json-server')
// const users = require('./db/users')
const { port, ip } = require('./config')
const cors = require('cors')
const express = require('express')
const bodyParser = require('body-parser')

const server = jsonServer.create()
const router = jsonServer.router('./db/db.json')
const middlewares = jsonServer.defaults()

server.use(cors())
server.use(bodyParser.json({ limit: '150mb' }))
 server.use(bodyParser.urlencoded({
    limit: '150mb',
    extended: false,
    parameterLimit: 1000000
}))

server.use(bodyParser.raw({
    inflate: true,
    limit: '150mb',
    // type: 'application/*'
    // type: 'multipart/form-data'
    // type: 'application/x-www-form-urlencoded'
    // type: '*/*'
}))


server.use(middlewares)
server.use('/assets', express.static(path.join(__dirname, 'assets')))

server.use('/voice-recognitions', require('./api/voiceRecognitions'))
server.use('/user-reviews', require('./api/userReviews'))
server.use('/user-sentiments', require('./api/userSentiments'))
server.use('/user-tweets', require('./api/userTweets'))

server.use('/', router)

server.listen(port, ip, () => {
    console.log(`JSON Server is running on http://${ip}:${port}/`)
})
