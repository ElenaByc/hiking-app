console.log('Login page');
const loginForm = document.querySelector('#login-form');
const email = document.querySelector('#email');
const password = document.querySelector('#password');

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
    // console.log(response);
    document.cookie = `userId=${responseArr[1]}`;
    document.cookie = `userName=${responseArr[2]}`;
    window.location.replace(responseArr[0]);
  }
}

loginForm.addEventListener('submit', handleSubmit);

