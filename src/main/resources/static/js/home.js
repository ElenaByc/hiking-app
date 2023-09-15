const searchForm = document.querySelector('#search-form');
const submitBtn = document.querySelector('#submit-button');
const loginBtn = document.querySelector('#login');
const searchResultContainer = document.querySelector('#search-result');
const userMenu = document.querySelector('#user-menu');
const userNameEl = document.querySelector('#user-name');

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
  loginBtn.innerText = 'Log In';
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
console.log('User Id = ', userId);
console.log('User Name = ', userName);
if (userId) {
  loginBtn.addEventListener('click', handleLogout);
  userNameEl.innerText = userName;
} else {
  userMenu.innerHTML = '';
  userMenu.appendChild(loginBtn);
  loginBtn.innerText = 'Log In';
  loginBtn.addEventListener('click', handleLogin);
}


const handleFormSubmit = async (e) => {
  e.preventDefault();
  submitBtn.disabled = true;
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
    searchResultContainer.innerHTML = '';
    const loadingHeader = document.createElement('h3');
    loadingHeader.classList.add('notfound-header');
    loadingHeader.innerHTML = `Sorry, nothing was found for city = ${city} <br>
    Please try again with different search terms`
    searchResultContainer.appendChild(loadingHeader);
  }
  submitBtn.disabled = false;
}

const showLoadingSpinner = () => {
  searchResultContainer.innerHTML = '';
  const loadingHeader = document.createElement('h3');
  loadingHeader.classList.add('loading-header');
  searchResultContainer.appendChild(loadingHeader);
  const spinnerDiv = document.createElement('div');
  spinnerDiv.classList.add('loading-spinner');
  let circle;
  for (let i = 0; i < 12; i++) {
    circle = document.createElement('div');
    circle.classList.add('circle');
    spinnerDiv.appendChild(circle);
  }
  searchResultContainer.appendChild(spinnerDiv);
}

const handleSaveTrail = async (e) => {
  const i = Number(e.target.id.substring(9));
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

const createTrailCard = (trail, i) => {
  let trailCard = document.createElement('div');
  trailCard.classList.add('trail-card');
  let latitude = 0;
  let longitude = 0;
  if (trail.googleCoordinates) {
    latitude = trail.googleCoordinates.latitude;
    longitude = trail.coordinates.longitude;
  }
  const divImg = document.createElement('div');
  divImg.classList.add('trail-card__img');
  const img = document.createElement('img');
  img.setAttribute("src", trail.image);
  img.setAttribute("alt", `${trail.name} picture`);
  divImg.appendChild(img);
  trailCard.appendChild(divImg);

  const divContent = document.createElement('div');
  divContent.classList.add('trail-card__content');
  const divInfo = document.createElement('div');
  divInfo.classList.add('trail-card__info');
  // const trailName = document.createElement('h3');
  // trailName.classList.add('trail-name');
  // trailName.innerText = trail.name;
  // divInfo.appendChild(trailName);
  divInfo.innerHTML = `
  <h3>${trail.name}</h3>
  <div>Yelp Alias: ${trail.yelpAlias}</div>
  <div>Yelp Rating: ${trail.yelpRating}&nbsp;&nbsp;Based on ${trail.yelpReviewCount} reviews</div>
  <div>Google Place Id: ${trail.googlePlaceId}</div>
  <div>Google Places Rating: ${trail.googleRating}&nbsp;&nbsp;Based on ${trail.googleReviewCount} reviews</div>
  <div>Yelp Coordinates: ${trail.coordinates.latitude}&nbsp;&nbsp;${trail.coordinates.longitude}</div>
  <div>Google Coordinates: ${latitude}&nbsp;&nbsp;${longitude}</div>
  <br>
  <div>Address: ${trail.address}</div>`;
  divContent.appendChild(divInfo);
  const divBtns = document.createElement('div');
  divBtns.classList.add('trail-card__buttons');
  const learnMoreBtn = document.createElement('button');
  learnMoreBtn.classList.add('button');
  learnMoreBtn.innerText = 'Learn more';
  divBtns.appendChild(learnMoreBtn);
  if (userId) {
    const saveTrailBtn = document.createElement('button');
    saveTrailBtn.classList.add('button');
    saveTrailBtn.innerText = 'Save this trail';
    saveTrailBtn.setAttribute('id', `save-btn-${i}`);
    saveTrailBtn.addEventListener('click', handleSaveTrail);
    divBtns.appendChild(saveTrailBtn);
  } else {
    divBtns.style.justifyContent = 'center';
  }
  divContent.appendChild(divBtns);
  trailCard.appendChild(divContent);
  return trailCard;
}

const createTrailsCards = (trails) => {
  searchResultContainer.innerHTML = '';
  trailsArray = [];
  let i = 0;
  trails.forEach(trail => {
    const trailCard = createTrailCard(trail, i);
    searchResultContainer.append(trailCard);
    trailsArray.push(trail);
    i++;
  });
  // store search result in local storage 
  localStorage.setItem("trails", JSON.stringify(trailsArray));
}

// trailCard.innerHTML = `
//           <div class="trail-card__img">
//             <img src="${trail.image}" alt="${trail.name} picture">
//           </div>
//           <div class="trail-card__content">
//             <div class="trail-card__info">
//               <h3>${trail.name}</h3>
//               <div>Yelp Alias: ${trail.yelpAlias}</div>
//               <div>Yelp Rating: ${trail.yelpRating}&nbsp;&nbsp;Based on ${trail.yelpReviewCount} reviews</div>
//               <div>Google Place Id: ${trail.googlePlaceId}</div>
//               <div>Google Places Rating: ${trail.googleRating}&nbsp;&nbsp;Based on ${trail.googleReviewCount} reviews</div>
//               <div>Yelp Coordinates: ${trail.coordinates.latitude}&nbsp;&nbsp;${trail.coordinates.longitude}</div>
//               <div>Google Coordinates: ${latitude}&nbsp;&nbsp;${longitude}</div>
//               <br>
//               <div>Address: ${trail.address}</div>
//             </div>
//             <div class="trail-card__buttons">
//               <button class="button">Learn more</button>
//               <button class="button" id="save-btn-${i}" onclick="handleSaveTrail(${i})">Save this trail</button>
//             </div>
//           </div>
//       `;




searchForm.addEventListener('submit', handleFormSubmit);

