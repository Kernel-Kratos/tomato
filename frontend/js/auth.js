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
.then (() => {
    const token = localStorage.getItem("jwt")
    const role = extractRole(token)
    //console.log(role)
    if (role.toLowerCase() == "role_customer"){
        window.location.href = "customer-home.html"
    } else if (role.toLowerCase() == "restaurant_owner"){
        window.location.href = "restaurant-home.html"
    }
})
.catch(error => console.log('Error: ', error))
})


function extractRole(token){
    const tokenParts = token.split(".")
    console.log(JSON.parse(atob(tokenParts[1])))
    return JSON.parse(atob(tokenParts[1])).roles[0] 
}