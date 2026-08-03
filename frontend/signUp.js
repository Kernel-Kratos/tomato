document.getElementById('signUp').addEventListener('submit', function(event) {
    event.preventDefault();
const firsNameVal =  document.getElementById('firstName').value;
const LastNameVal = document.getElementById('lastName').value;
const phoneNumberVal = document.getElementById('phoneNumber').value;
const emailVal = document.getElementById('email').value;
const passwordVal = document.getElementById('password').value;
const payload = {email: emailVal, 
    password:passwordVal, 
    firsName:firsNameVal, 
    lastName:LastNameVal, 
    phoneNumber: phoneNumberVal
};
fetch('http://localhost:8080/tomato/restaurant/signup', {
    method: 'POST',
    headers: {
        'Content-Type' : 'application/json'
    },
    body : JSON.stringify(payload)
})
.then(response => {
    if (response.status === 200) {
        alert("Please wait..")
        window.location.href= "restaurant.html";
    }
    return response.json()
})
.then(data => console.log('Success: ', data))
.catch(error => console.log('Error: ', error));
})