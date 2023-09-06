console.log('HOME PAGE SCRIPT');

const searchForm = document.querySelector('#search-form');
const loginBtn = document.querySelector('#login');
const searchResultContainer = document.querySelector('#search-result');

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
  showLoadingSpinner();
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
    createTrailsCards(responseArr);
  }
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

const createTrailsCards = (trails) => {
  searchResultContainer.innerHTML = '';
  trails.forEach(trail => {
    let trailCard = document.createElement("div");
    trailCard.classList.add("trail-card");
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
              <button class="button">Save this trail</button>
            </div>
          </div>
      `;
    searchResultContainer.append(trailCard);
  });
}

searchForm.addEventListener('submit', handleSubmit);

