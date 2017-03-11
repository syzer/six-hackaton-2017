// server.js
const fs = require('fs')
const jsonServer = require('json-server')
// const users = require('./db/users')
const { port, ip } = require('./config')
const cors = require('cors')

const server = jsonServer.create()
const router = jsonServer.router('./db/db.json')
const middlewares = jsonServer.defaults()

server.use(cors())
// server.use('/users', (req, res, next) => {
//     console.log(req)
//     res.json(users())
// })
server.use(middlewares)
server.use(router)
server.listen(port, ip, () => {
    console.log(`JSON Server is running on http://${ip}:${port}/`)
})
