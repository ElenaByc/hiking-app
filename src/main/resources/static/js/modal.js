const title = document.querySelector('.modal-title');
const img = document.querySelector('.modal-trail__img');
const yelpRating = document.querySelector('.yelp-rating__number');
const googleRating = document.querySelector('.google-rating__number');
const yelpReviews = document.querySelector('.yelp-rating__reviews');
const googleReviews = document.querySelector('.google-rating__reviews');
const yelpStars = document.querySelector('.yelp-rating__stars');
const googleStars = document.querySelector('.google-rating__stars');
const googleRatingDiv = document.querySelector('.google-rating');
const address = document.querySelector('.modal-address div:nth-child(2)');
const yelpLink = document.querySelector('.yelp-link__link');
const googleLink = document.querySelector('.google-link__link');
const websiteDiv = document.querySelector('.modal-website');
const website = document.querySelector('.modal-website a');
const saveBtn = document.querySelector(`#save-btn`);


const populateModalBasicData = (trail) => {
  title.innerText = trail.name;
  img.setAttribute('src', trail.image);
  img.setAttribute('alt', `${trail.name} picture`);

  yelpRating.innerText = trail.yelpRating;
  yelpReviews.innerText = trail.yelpReviewCount;
  yelpLink.setAttribute('href', trail.yelpLink);
  if (trail.yelpRating < 1) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-0.png)';
  } else if (trail.yelpRating < 1.5) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-1.png)';
  } else if (trail.yelpRating < 2) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-1-half.png)';
  } else if (trail.yelpRating < 2.5) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-2.png)';
  } else if (trail.yelpRating < 3) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-2-half.png)';
  } else if (trail.yelpRating < 3.5) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-3.png)';
  } else if (trail.yelpRating < 4) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-3-half.png)';
  } else if (trail.yelpRating < 4.5) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-4.png)';
  } else if (trail.yelpRating < 5) {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-4-half.png)';
  } else {
    yelpStars.style.backgroundImage = 'url(../assets/images/yelp-5.png)';
  }

  if (trail.googleRating == 0) {
    googleRatingDiv.style.display = 'none';
  } else {
    googleRatingDiv.style.display = 'block';
    googleRating.innerText = trail.googleRating;
    googleReviews.innerText = trail.googleReviewCount;
    googleStars.innerHTML = '';
    let googleStarElement;
    let i;
    for (i = 1; i <= trail.googleRating; i++) {
      googleStarElement = document.createElement('div');
      googleStarElement.classList.add('google-star');
      googleStars.appendChild(googleStarElement);
    }
    if (trail.googleRating - i + 1 > 0) {
      googleStarElement = document.createElement('div');
      googleStarElement.classList.add('google-star-half');
      googleStars.appendChild(googleStarElement);
    }
  }

  address.innerText = trail.address;

  console.log("trail.saved = ", trail.saved);

  if (trail.saved) {
    saveBtn.innerText = 'Saved';
    saveBtn.disabled = true;
  } else {
    saveBtn.innerText = 'Save this trail';
    saveBtn.disabled = false;
  }
}

const populateModal = (trail) => {
  googleLink.setAttribute('href', trail.googleLink);

  if (trail.website == null) {
    websiteDiv.style.display = 'none';
  } else {
    websiteDiv.style.display = 'flex';
    website.setAttribute('href', trail.website);
    website.innerText = trail.website;
  }
}