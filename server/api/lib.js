const onError = (next) =>
    (error) => {
        console.log(error)
        next(error)
    }

module.exports = {
    onError
}