console.log('HOME PAGE SCRIPT');

const searchForm = document.querySelector('#search-form');
const loginBtn = document.querySelector('#login');

const handleLogin = () => {
  window.location.replace('./login.html');
}

const handleLogout = () => {
  console.log(document.cookie);
  const c = document.cookie.split(";");
  for (let i in c) {
    document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
  }
  console.log(document.cookie);
  loginBtn.removeEventListener('click', handleLogout);
  loginBtn.innerText = 'Login';
  loginBtn.addEventListener('click', handleLogin);
}

console.log(document.cookie);
const cookieArr = document.cookie.split('; ');
console.log(cookieArr);
if (cookieArr.length > 1) {
  const userId = cookieArr[1].split('=')[1];
  const userName = cookieArr[0].split('=')[1];
  console.log('User Id  = ', userId);
  console.log('User Name  = ', userName);
  loginBtn.addEventListener('click', handleLogout);
  loginBtn.innerText = 'Logout';
} else {
  loginBtn.addEventListener('click', handleLogin);
}


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

searchForm.addEventListener('submit', handleSubmit);
