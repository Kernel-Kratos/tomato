document.getElementById('signIn').addEventListener('submit', function(event) {
    event.preventDefault();

const emailVal = document.getElementById('email').value;
const passwordVal = document.getElementById('password').value;
const payload = {email: emailVal, 
    password:passwordVal, 
    firsName:'', 
    lastName:'', 
    phoneNumber: 0
};
fetch('http://localhost:8080/tomato/restaurant/login', {
    method: 'POST',
    headers: {
        'Content-Type' : 'application/json'
    },
    body : JSON.stringify(payload)
})
.then(response => {
    if (response.status === 404) {
        console.log("user not found.");
        alert("User Not Found. Check Email or Passowrd or Sign Up")
        {message : "User Not Found. Please Check Email or Password"};
        return response.json()
    }
    if (response.status === 200){
        return response.text();
    }
})
.then(data => console.log('Success: ', data))
.catch(error => console.log('Error: ', error));
})