// curl localhost:5000/transcript-voice -F "file=@./server/assets/english.wav"
// {"transcript": "hi i'm on my way to be available high quality text to speech voices select download not to install my voice"}

module.exports = (req, res, next) => {
    res.json('working')
}
