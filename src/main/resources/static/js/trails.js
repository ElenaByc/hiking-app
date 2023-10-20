
const savedTrailsContainer = document.querySelector('.trails-container');

const headers = {
  'Content-Type': 'application/json'
};

const getSavedTrails = async () => {
  const response = await fetch(`/api/users/trails/${userId}`, {
    method: 'GET',
    headers: headers
  }).catch(err => console.error(err.message));
  const responseArr = await response.json();
  if (responseArr.length === 0) {
    savedTrailsContainer.innerHTML = '';
    const header = document.createElement('h3');
    header.classList.add('notfound-header');
    header.innerHTML = 'There is no trails<br>in your Saved Trails List';
    savedTrailsContainer.appendChild(header);
  } else {
    createSavedTrailsCards(responseArr);
    console.log(responseArr);
  }
}

const createSavedTrailCard = (trail) => {
  const trailCard = document.createElement('div');
  trailCard.classList.add('trail-card');

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
  learnMoreBtn.innerText = 'Learn more';
  divBtns.appendChild(learnMoreBtn);
  const removeTrailBtn = document.createElement('button');
  removeTrailBtn.classList.add('button');
  removeTrailBtn.classList.add('remove-btn');
  removeTrailBtn.innerText = 'Remove';
  removeTrailBtn.setAttribute('id', `remove-btn-${trail.id}`);
  removeTrailBtn.addEventListener('click', handleRemoveTrail);
  divBtns.appendChild(removeTrailBtn);
  divContent.appendChild(divBtns);
  trailCard.appendChild(divContent);
  return trailCard;
}

const handleRemoveTrail = async (e) => {
  const trailCard = e.target.closest('.trail-card');
  const trailId = Number(e.target.id.substring(11));
  console.log(trailId);
  trailCard.style.display = 'none';
  const response = await fetch(`/api/users/trails/${userId}/remove/${trailId}`, {
    method: 'POST',
    headers: headers
  })
    .catch(err => console.error(err.message));
  console.log(response.status);
  const responseArr = await response.json();
}

const createSavedTrailsCards = (trails) => {
  savedTrailsContainer.innerHTML = '';
  trails.forEach(trail => {
    const trailCard = createSavedTrailCard(trail);
    savedTrailsContainer.append(trailCard);
  });
}

showLoadingSpinner(savedTrailsContainer);
getSavedTrails();
