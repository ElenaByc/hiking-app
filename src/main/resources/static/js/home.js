const searchForm = document.querySelector('#search-form');
const submitBtn = document.querySelector('#submit-button');
const loginBtn = document.querySelector('#login');
const searchResultContainer = document.querySelector('#search-result');
const userMenu = document.querySelector('#user-menu');

const headers = {
  'Content-Type': 'application/json'
};

const baseUrl = '/api/trails';

let userId;
let userName;

let trailsArray = [];

const handleLogin = () => {
  window.location.replace('./login.html');
}

const handleLogout = () => {
  const c = document.cookie.split(";");
  for (let i in c) {
    document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
  }
  loginBtn.removeEventListener('click', handleLogout);
  loginBtn.innerText = 'Login';
  loginBtn.addEventListener('click', handleLogin);
  userMenu.innerHTML = '';
  userMenu.appendChild(loginBtn);
  userId = null;
  userName = null;
}

console.log(document.cookie);
const cookieArr = document.cookie.split('; ');
console.log(cookieArr);
for (let i = 0; i < cookieArr.length; i++) {
  if (cookieArr[i].includes('userId')) {
    userId = cookieArr[i].split('=')[1];
  }
  if (cookieArr[i].includes('userName')) {
    userName = cookieArr[i].split('=')[1];
  }
}
console.log('User Id  = ', userId);
console.log('User Name  = ', userName);
if (userId) {
  loginBtn.addEventListener('click', handleLogout);
} else {
  userMenu.innerHTML = '';
  userMenu.appendChild(loginBtn);
  loginBtn.innerText = 'Log In';
  loginBtn.addEventListener('click', handleLogin);
}

const handleFormSubmit = async (e) => {
  e.preventDefault();
  submitBtn.disabled = true;
  console.log(submitBtn);
  showLoadingSpinner();
  const city = document.querySelector('#city').value;
  const trailName = document.querySelector('#trail-name');
  console.log('url = ', `${baseUrl}/location/${city}`);
  const response = await fetch(`${baseUrl}/location/${city}`, {
    method: 'GET',
    headers: headers
  })
    .catch(err => console.error(err.message));

  if (response.status === 200) {
    const responseArr = await response.json();
    console.log(responseArr);
    createTrailsCards(responseArr);
  } else {
    console.log('ERROR!!!!!!');
    searchResultContainer.innerHTML = '';
  }
  submitBtn.disabled = false;
}

const showLoadingSpinner = () => {
  searchResultContainer.innerHTML = `
    <h3 class="loading-header">Loading...</h3>
    <div class="loading-spinner">
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
      </div>
    `;
}

const handleSaveTrail = async (i) => {
  let trail = trailsArray[i];
  console.log(trail);
  const response = await fetch(`${baseUrl}/save/${userId}`, {
    method: 'POST',
    body: JSON.stringify(trail),
    headers: headers
  })
    .catch(err => console.error(err.message))
  if (response.status == 200) {
    console.log(response.status);
    const responseArr = await response.json();
    console.log(responseArr);
    const saveBtn = document.querySelector(`#save-btn-${i}`);
    saveBtn.innerHTML = 'Saved';
    saveBtn.disabled = true;
  }
}

const createTrailsCards = (trails) => {
  searchResultContainer.innerHTML = '';
  trailsArray = [];
  let i = 0;
  trails.forEach(trail => {
    let trailCard = document.createElement('div');
    trailCard.classList.add('trail-card');
    let latitude = 0;
    let longitude = 0;
    if (trail.googleCoordinates) {
      latitude = trail.googleCoordinates.latitude;
      longitude = trail.coordinates.longitude;
    }
    trailCard.innerHTML = `
          <div class="trail-card__img">
            <img src="${trail.image}" alt="${trail.name} picture">
          </div>
          <div class="trail-card__content">
            <div class="trail-card__info">
              <h3>${trail.name}</h3>
              <div>Yelp Alias: ${trail.yelpAlias}</div>
              <div>Yelp Rating: ${trail.yelpRating}&nbsp;&nbsp;Based on ${trail.yelpReviewCount} reviews</div>
              <div>Google Place Id: ${trail.googlePlaceId}</div>
              <div>Google Places Rating: ${trail.googleRating}&nbsp;&nbsp;Based on ${trail.googleReviewCount} reviews</div>
              <div>Yelp Coordinates: ${trail.coordinates.latitude}&nbsp;&nbsp;${trail.coordinates.longitude}</div>
              <div>Google Coordinates: ${latitude}&nbsp;&nbsp;${longitude}</div>
              <br>
              <div>Address: ${trail.address}</div>
              </div>
            <div class="trail-card__buttons">
              <button class="button">Learn more</button>
              <button class="button" id="save-btn-${i}" onclick="handleSaveTrail(${i})">Save this trail</button>
            </div>
          </div>
      `;
    searchResultContainer.append(trailCard);
    trailsArray.push(trail);
    i++;
  });
  // store search result in local storage 
  localStorage.setItem("trails", JSON.stringify(trailsArray));
}

searchForm.addEventListener('submit', handleFormSubmit);

