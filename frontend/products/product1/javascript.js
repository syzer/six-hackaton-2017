// function init() {
// 	var instance = axios.create({
// 		timeout: 1000,
// 		headers: {'X-custom-header': 'foobar'}
// 	});

// 	axios.get('http://0.0.0.0:3000/reviews')
// 		 .then(function(response) {
// 		 	console.log(response);
// 		    console.log(response.data[0].author); //amazing jeff
// 		    $('#name0').append(response.data[0].author);
// 		    $('#load-reviews0').append(response.data[0].review);
// 		    $('#satisfaction0').append(response.data[0].sentiment);

// 		    $('#name1').append(response.data[1].author);
// 		    $('#load-reviews1').append(response.data[1].review);
// 		    $('#satisfaction1').append(response.data[1].sentiment);

// 		 })

// 		 .catch(function(error) {
// 		 	console.log(response);
// 		 });

// }


function init() {
	var instance = axios.create({
		timeout: 1000,
		headers: {'X-custom-header': 'foobar'}
	});

	axios.get('http://0.0.0.0:3000/reviews')
		 .then(function(response) {
		 	console.log(response);
		    console.log(response.data[0].author); //amazing jeff
		    $('#name0').append(response.data[0].author);
		    $('#load-reviews0').append(" '" + response.data[0].review  + "' ");
		    $('#satisfaction0').append(response.data[0].sentiment);

		    $('#name1').append(response.data[1].author);
		    $('#load-reviews1').append(" ' " + response.data[1].review  + " ' ");
		    $('#satisfaction1').append(response.data[1].sentiment);		   

		 })

		 .catch(function(error) {
		 	console.log(response);
		 });

}