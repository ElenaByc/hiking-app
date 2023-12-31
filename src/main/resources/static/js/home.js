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
  showLoadingSpinner(searchResultContainer);
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

const getTrailDetails = async (e) => {
  console.log(e.target.id);
  const i = Number(e.target.id.substring(9));
  let trail = trailsArray[i];
  console.log(trail);
  populateModalBasicData(trail, i);

  // get trail's reviews from DB
  const allReviews = [];
  if (trail.id) {
    await fetch(`/api/reviews/trail/${trail.id}`, {
      method: "GET",
      headers: headers
    })
      .then(res => res.json())
      .then(data => {
        console.log('reviewsData: ', data);
        if (data.length > 0 && trail.reviewed) {
          const userReview = data.find(review => review.userDto.id == userId);
          populateUserReview(userReview);
        }
        if (data.length > 0) {
          data.forEach(review => {
            if (review.userDto.id != userId) {
              allReviews.push(review);
            }
          });
          console.log('all reviews: ', allReviews);
        }
      })
      .catch(err => console.error(err.message));
  }

  // get trail's pictures from DB
  const allPictures = [];
  if (trail.id) {
    await fetch(`/api/pictures/trail/${trail.id}`, {
      method: "GET",
      headers: headers
    })
      .then(res => res.json())
      .then(data => {
        console.log('picturesData: ', data);
        if (data.length > 0) {
          allPictures.push(...data);
          console.log('all trail pictures: ', allPictures);
        }
      })
      .catch(err => console.error(err.message));
  }

  // get website, google link, reviews and photos from Yelp and Google
  await fetch(`${baseUrl}/details/${trail.yelpAlias}/${trail.googlePlaceId}`, {
    method: "GET",
    headers: headers
  })
    .then(res => res.json())
    .then(data => {
      console.log('data: ', data);
      allReviews.push(...data.googleReviews);
      allReviews.push(...data.yelpReviews);
      allPictures.push(...data.googlePictures);
      allPictures.push(...data.yelpPictures);
      populateModal(data);
    })
    .catch(err => console.error(err.message));

  if (allPictures.length > 0) {
    populateTrailPictures(allPictures);
  }
  if (allReviews.length > 0) {
    populateTrailReviews(allReviews);
  }
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
    .catch(err => console.error(err.message));
  if (response.status == 200) {
    console.log(response.status);
    const responseArr = await response.json();
    console.log(responseArr);
    const saveBtn = document.querySelector(`#save-btn-${i}`);
    saveBtn.innerText = 'Saved';
    saveBtn.disabled = true;
    trailsArray[i].saved = true;
  }
}

const createTrailCard = (trail, i) => {
  const trailCard = document.createElement('div');
  trailCard.classList.add('trail-card');

  const divImg = document.createElement('div');
  divImg.classList.add('trail-card__img');
  const img = document.createElement('img');
  img.setAttribute('src', trail.image);
  img.setAttribute('alt', `${trail.name} picture`);
  divImg.appendChild(img);
  trailCard.appendChild(divImg);

  const divContent = document.createElement('div');
  divContent.classList.add('trail-card__content');
  const divInfo = document.createElement('div');
  divInfo.classList.add('trail-card__info');
  const trailName = document.createElement('h3');
  trailName.classList.add('trail-name');
  trailName.innerText = trail.name;
  divInfo.appendChild(trailName);

  const yelpRating = document.createElement('div');
  yelpRating.innerHTML = `Yelp Rating: <span>${trail.yelpRating}</span>`;
  divInfo.appendChild(yelpRating);
  const yelpReviews = document.createElement('div');
  yelpReviews.innerText = `Based on ${trail.yelpReviewCount} reviews`;
  divInfo.appendChild(yelpReviews);

  divInfo.appendChild(document.createElement('br'));

  const googleRating = document.createElement('div');
  googleRating.innerHTML = `Google Rating: <span>${trail.googleRating}</span>`;
  divInfo.appendChild(googleRating);
  const googleReviews = document.createElement('div');
  googleReviews.innerText = `Based on ${trail.googleReviewCount} reviews`;
  divInfo.appendChild(googleReviews);

  divInfo.appendChild(document.createElement('br'));

  const address = document.createElement('div');
  address.classList.add('trail-address')
  address.innerHTML = `<div>Address:</div><div>${trail.address}</div>`;
  divInfo.appendChild(address);

  divContent.appendChild(divInfo);

  const divBtns = document.createElement('div');
  divBtns.classList.add('trail-card__buttons');
  const learnMoreBtn = document.createElement('button');
  learnMoreBtn.classList.add('button');
  learnMoreBtn.setAttribute('data-bs-toggle', 'modal');
  learnMoreBtn.setAttribute('data-bs-target', '#details');
  learnMoreBtn.setAttribute('id', `more-btn-${i}`);
  learnMoreBtn.addEventListener('click', getTrailDetails);
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
