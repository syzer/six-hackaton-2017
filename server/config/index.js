require('dotenv').config()

module.exports = {
    protocol: 'http://',
    ip: process.env.ip || '0.0.0.0',
    port: process.env.port || 3000,
    // https://www.microsoft.com/cognitive-services/en-us/subscriptions
    bingSpeechKey: process.env.BING_SPEECH_KEY || ''
}
