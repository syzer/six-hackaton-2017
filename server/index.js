// server.js
const fs = require('fs')
const jsonServer = require('json-server')
const server = jsonServer.create()
const router = jsonServer.router('./db/db.json')
const users = require('./db/users')
const middlewares = jsonServer.defaults()
server.use('/users', (req, res, next) => {
    console.log(req)
    res.json(users())
})
server.use(middlewares)
server.use(router)
server.listen(3000, () => {
  console.log('JSON Server is running on http://localhost:3000/')
})
