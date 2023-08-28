console.log('REGISTER SCRIPT')
const registerForm = document.querySelector('#register-form');
const username = document.querySelector('#name');
const email = document.querySelector('#email');
const password = document.querySelector('#password');

const headers = {
  'Content-Type': 'application/json'
}

const baseUrl = '/api/users';

const handleSubmit = async (e) => {
  e.preventDefault()

  let bodyObj = {
    username: username.value,
    email: email.value,
    password: password.value
  }

  const response = await fetch(`${baseUrl}/register`, {
    method: 'POST',
    body: JSON.stringify(bodyObj),
    headers: headers
  })
    .catch(err => console.error(err.message))

  const responseArr = await response.json();

  if (response.status === 200) {
    window.location.replace(responseArr[2]);
  }
}

registerForm.addEventListener('submit', handleSubmit);