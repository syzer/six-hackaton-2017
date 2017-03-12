const onError = (next) =>
    (error) => {
        console.log(error)
        next(error)
    }

const getData = ({data}) => data

module.exports = {
    onError,
    getData
}