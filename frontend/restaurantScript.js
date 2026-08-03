document.getElementById('create').addEventListener('submit', function(event) {
    event.preventDefault();
emailVal = document.getElementById('email').value;
const licenseNo =  document.getElementById('licenseNo').value;
const restoName = document.getElementById('nameRestaurant').value;
const addressVal = document.getElementById('address').value;
const payload = {email: emailVal, 
    licenseNo: licenseNo,
     name: restoName, 
    address: addressVal
    
};
fetch('http://localhost:8080/tomato/restaurant/create', {
    method: 'POST',
    headers: {
        'Content-Type' : 'application/json'
    },
    body : JSON.stringify(payload)
})
.then(response => {
    if (response.status === 404) {
        alert("No account exists with this email. Try again");
    }
    if (response.status === 200) {
        alert("Login Success");
    }
    return response.text();
})
.then(data => console.log('Success: ', data))
.catch(error => console.log('Error: ', error));
})