console.log('HOME PAGE SCRIPT');

//Cookie
const cookieArr = document.cookie.split('=');
const userId = cookieArr[1];
console.log("User Id  = ", userId);

//DOM Elements
const searchForm = document.querySelector('#search-form');

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

