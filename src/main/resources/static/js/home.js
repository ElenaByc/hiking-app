console.log('HOME PAGE SCRIPT');

console.log(document.cookie);
const cookieArr = document.cookie.split('; ');
console.log(cookieArr);
console.log(cookieArr[0].split('='));
const userId = cookieArr[0].split('=')[1];
const userName = cookieArr[1].split('=')[1];
console.log('User Id  = ', userId);
console.log('User Name  = ', userName);

const searchForm = document.querySelector('#search-form');
const loginBtn = document.querySelector('#login');

console.log(loginBtn);

const headers = {
  'Content-Type': 'application/json'
};

const baseUrl = '/api/trails';

const handleSubmit = async (e) => {
  e.preventDefault();
  const city = document.querySelector('#city').value;
  const trailName = document.querySelector('#trail-name');
  console.log('url = ', `${baseUrl}/location/${city}`);
  const response = await fetch(`${baseUrl}/location/${city}`, {
    method: 'GET',
    headers: headers
  })
    .catch(err => console.error(err.message));

  const responseArr = await response.json();

  if (response.status === 200) {
    console.log(responseArr);
  }
}

const handleLogin = () => {
  window.location.replace('./login.html');
}

searchForm.addEventListener('submit', handleSubmit);
loginBtn.addEventListener('click', handleLogin);

