
function init() {
	var instance = axios.create({
		timeout: 1000,
		headers: {'X-custom-header': 'foobar'}
	});

	axios.get('http://0.0.0.0:3000/sentiments')
		 .then(function(response) {
		 	console.log(response);
		 	// $("#aggregate").append(response.data[0].productId);


			for (int i=0;i<response.data.length;i++){
				console.log(response);
				//console.log(response.data.[i]);
				return response.data.[i];
			}

		    // console.log(response.data[0].author); //amazing jeff
		    // $('#name0').append(response.data[0].author);
		    // $('#load-reviews0').append(" '" + response.data[0].review  + "' ");
		    // $('#satisfaction0').append(response.data[0].sentiment);

		    // $('#name1').append(response.data[1].author);
		    // $('#load-reviews1').append(" ' " + response.data[1].review  + " ' ");
		    // $('#satisfaction1').append(response.data[1].sentiment);		   

		 })

		 .catch(function(error) {
		 	console.log(response);
		 });

}