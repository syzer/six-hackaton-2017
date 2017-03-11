
	var instance = axios.create({
		  baseURL: 'http://localhost:3000',
		  timeout: 1000,
		  headers: {'X-Custom-Header': 'foobar'}
		});

	axios.get('/voice-recognitions?')
		.then(function (response) {
		console.log(response);
		})
		.catch(function (error) {
		console.log(error);
		});