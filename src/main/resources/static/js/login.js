console.log('Login page');
let loginForm = document.querySelector('#login-form');
let email = document.querySelector('#email');
let password = document.querySelector('#password');

const headers = {
  'Content-Type': 'application/json'
};

const baseUrl = '/api/users';

const handleSubmit = async (e) => {
  e.preventDefault();

  let bodyObj = {
    email: email.value,
    password: password.value
  };

  const response = await fetch(`${baseUrl}/login`, {
    method: "POST",
    body: JSON.stringify(bodyObj),
    headers: headers
  })
    .catch(err => console.error(err.message));

  const responseArr = await response.json();

  if (response.status === 200) {
    document.cookie = `userId=${responseArr[1]}`;
    window.location.replace(responseArr[0]);
  }
}

loginForm.addEventListener('submit', handleSubmit);

