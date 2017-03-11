require('dotenv').config()

const protocol = 'http://'
const ip = process.env.ip || '0.0.0.0'
const port = process.env.port || 3000

module.exports = {
    url: `${protocol}${ip}:${port}`,
    protocol,
    ip,
    port,
    // https://www.microsoft.com/cognitive-services/en-us/subscriptions
    bingSpeechKey: process.env.BING_SPEECH_KEY || ''
}
