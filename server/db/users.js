const faker = require('faker')

const newUsers = () => {
    return Array.from({ length: 50 }, (x, i) => ({
        id: i + 1,
        firstName: faker.name.firstName(),
        lastName: faker.name.firstName(),
        phoneNumber: faker.phone.phoneNumberFormat(),
        friends: []
    }))
}

// json-server requires that you export
// a function which generates the data set
module.exports = newUsers
require('fs')
    .writeFileSync(__dirname + '/users.json', JSON.stringify(newUsers(), null, 2), 'utf-8')
