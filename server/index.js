const fs = require('fs')
const path = require('path')
const jsonServer = require('json-server')
// const users = require('./db/users')
const { port, ip } = require('./config')
const cors = require('cors')
const express = require('express')
const socket = require('socket.io')
const bodyParser = require('body-parser')
const fileUpload = require('express-fileupload')

const server = jsonServer.create()
const app = require('http').createServer(server)

const router = jsonServer.router('./db/db.json')
const middlewares = jsonServer.defaults()

server.use(cors())
server.use(bodyParser.json({ limit: '150mb' }))
server.use(bodyParser.urlencoded({
    limit: '150mb',
    extended: true
}))
server.use(fileUpload())


server.use(middlewares)
server.use('/assets', express.static(path.join(__dirname, 'assets')))
server.use('/frontend', express.static(path.join(__dirname, '../frontend')))

server.use('/voice-recognitions', require('./api/voiceRecognitions'))
server.use('/user-reviews', require('./api/userReviews'))
server.use('/user-sentiments', require('./api/userSentiments'))
server.use('/user-tweets', require('./api/userTweets'))

server.use('/', router)

const io = socket(app)
io.on('connection', (client) => {
    console.log('client connected', client.id)
    io.emit('review', { much: 'wow!' })
})

app.listen(3001, () => {
    console.log('Socket on http://localhost:3001')
})

server.listen(port, ip, () => {
    console.log(`JSON Server is running on http://${ip}:${port}/`)
})
