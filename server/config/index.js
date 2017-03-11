require('dotenv').config()

module.exports = {
    protocol: 'http://',
    ip: process.env.ip || '0.0.0.0',
    port: process.env.port || 3000
}
