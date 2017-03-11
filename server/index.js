const fs = require('fs')
const path = require('path')
const jsonServer = require('json-server')
// const users = require('./db/users')
const { port, ip } = require('./config')
const cors = require('cors')
const express = require('express')

const server = jsonServer.create()
const router = jsonServer.router('./db/db.json')
const middlewares = jsonServer.defaults()

server.use(cors())
server.use(middlewares)
server.use('/assets', express.static(path.join(__dirname, 'assets')))
server.use('/', router)

server.listen(port, ip, () => {
    console.log(`JSON Server is running on http://${ip}:${port}/`)
})
