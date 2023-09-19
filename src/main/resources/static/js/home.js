const searchForm = document.querySelector('#search-form');
const submitBtn = document.querySelector('#submit-button');
const searchResultContainer = document.querySelector('#search-result');

const headers = {
  'Content-Type': 'application/json'
};

const baseUrl = '/api/trails';
let trailsArray = [];

const handleFormSubmit = async (e) => {
  e.preventDefault();
  submitBtn.disabled = true;
  showLoadingSpinner();
  const city = document.querySelector('#city').value;
  const trailName = document.querySelector('#trail-name');

  let searchUrl;
  if (userId) {
    searchUrl = `${baseUrl}/location/${city}/${userId}`;
  } else {
    searchUrl = `${baseUrl}/location/${city}/0`;
  }
  console.log(searchUrl);
  const response = await fetch(searchUrl, {
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
    saveBtn.innerText = 'Saved';
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
    saveTrailBtn.classList.add('save-btn');
    if (trail.saved) {
      saveTrailBtn.innerText = 'Saved';
      saveTrailBtn.disabled = true;
    } else {
      saveTrailBtn.innerText = 'Save this trail';
      saveTrailBtn.setAttribute('id', `save-btn-${i}`);
      saveTrailBtn.addEventListener('click', handleSaveTrail);
    }
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

searchForm.addEventListener('submit', handleFormSubmit);
