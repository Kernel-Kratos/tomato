document.getElementById('create').addEventListener('submit', function(event) {
    event.preventDefault();
const emailVal = document.getElementById('email').value.trim();
const passVal = document.getElementById('password').value.trim();
const payload = {email: emailVal, 
    password: passVal
};
fetch('http://localhost:8080/tomato/auth/login',{
    method: 'Post',
    headers: {
        'Content-Type': 'application/json'
    },
    body : JSON.stringify(payload)
})
.then(response => {
    if(response.status == 404){
        alert("No account exists with this email or password");
        throw new Error()
    }
    if(response.status == 200){
        alert("Login Success")
    }
    return response.text();
})
.then(data => localStorage.setItem("jwt", data))
.catch(error => console.log('Error: ', error))
})