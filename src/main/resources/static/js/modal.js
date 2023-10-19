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
const modalBtnsDiv = document.querySelector('.modal-btns');
const saveBtn = document.querySelector('#save-btn');
const reviewBtn = document.querySelector('#review-btn');
const reviewForm = document.querySelector('#review-form');
const revewCancelBtn = document.querySelector('#review-cancel-btn');

const userReview = document.querySelector('#review');
const reviewsContainer = document.querySelector('.reviews-container');

const addPictureBtn = document.querySelector('#picture-btn');
const addPictureForm = document.querySelector('#add-picture-form');
const addPictureCancelBtn = document.querySelector('#picture-cancel-btn');


let currentTrail;

const populateModalBasicData = (trail) => {
  currentTrail = trail;
  clearUserReview();
  reviewsContainer.innerHTML = '';

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

  hideReviewForm();
  hideAddPictureForm();

  if (userId) {
    modalBtnsDiv.style.display = 'flex';
    if (trail.saved) {
      saveBtn.innerText = 'Saved';
      saveBtn.disabled = true;
    } else {
      saveBtn.innerText = 'Save this trail';
      saveBtn.disabled = false;
    }
    if (trail.reviewed) {
      reviewBtn.innerText = 'My review';
      reviewBtn.removeEventListener('click', showReviewForm);
      reviewBtn.addEventListener('click', toggleUserReview);
    } else {
      reviewBtn.innerText = 'Write review';
      reviewBtn.disabled = false;
      reviewBtn.addEventListener('click', showReviewForm);
      reviewBtn.removeEventListener('click', toggleUserReview);
    }

  } else {
    modalBtnsDiv.style.display = 'none';
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

const clearUserReview = () => {
  userReview.style.display = 'none';
  const reviewBody = document.querySelector('#review p.review-body');
  const reviewDate = document.querySelector('#review p.review-date span');
  const reviewRating = document.querySelector('#review p.review-rating span');
  reviewDate.innerText = '';
  reviewRating.innerText = '';
  reviewBody.innerText = '';
}

const populateUserReview = (review) => {
  const reviewBody = document.querySelector('#review p.review-body');
  const reviewDate = document.querySelector('#review p.review-date span');
  const reviewRating = document.querySelector('#review p.review-rating span');
  reviewDate.innerText = review.date;
  reviewRating.innerText = review.rating;
  reviewBody.innerText = review.body;
}

const populateTrailReviews = (allReviews) => {
  allReviews.forEach(review => {
    reviewsContainer.appendChild(createReviewCard(review));
  });
}

const createReviewCard = (review) => {
  const reviewCard = document.createElement('div');
  reviewCard.classList.add('modal-review');
  const reviewer = document.createElement('p');
  reviewer.classList.add('review-reviewer');
  reviewer.innerHTML = `Name: <span>${review.userDto.username}</span>`;
  reviewCard.appendChild(reviewer);
  const reviewDate = document.createElement('p');
  reviewDate.classList.add('review-date');
  reviewDate.innerHTML = `Date: <span>${review.date}</span>`;
  reviewCard.appendChild(reviewDate);
  const reviewRating = document.createElement('p');
  reviewRating.classList.add('review-rating');
  reviewRating.innerHTML = `Rating: <span>${review.rating}</span>`;
  reviewCard.appendChild(reviewRating);
  const reviewBody = document.createElement('p');
  reviewBody.classList.add('review-body');
  if (review.source == "Yelp") {
    reviewBody.innerHTML = `
    ${review.body}
    <a href="${review.url}" target="_blank" class="review-link">read more</a>`;
  } else {
    reviewBody.innerText = review.body;
  }

  reviewCard.appendChild(reviewBody);
  const source = document.createElement('div');
  source.classList.add('review-source');
  if (review.source) {
    source.innerText = `source: ${review.source}`;
  } else {
    source.innerText = 'source: Let\'s Go Hiking! App';
  }

  reviewCard.appendChild(source);
  return reviewCard;
}

const showReviewForm = () => {
  reviewForm.style.display = 'block';
  reviewBtn.disabled = true;
  document.querySelector('#review-body').focus();
}

const hideReviewForm = () => {
  reviewForm.style.display = 'none';
  reviewBtn.disabled = false;
  document.querySelector('#rating').value = "";
  document.querySelector('#review-body').value = "";
}

const toggleUserReview = () => {
  if (userReview.style.display === 'block') {
    userReview.style.display = 'none';
  } else {
    userReview.style.display = 'block';
  }
}

const handleSubmitReview = async (e) => {
  e.preventDefault();
  const rating = document.querySelector('#rating').value;
  const reviewBody = document.querySelector('#review-body').value;
  const review = {
    'body': reviewBody,
    'rating': rating,
    'date': getCurrentDate(),
    'trailDto': currentTrail
  };

  const response = await fetch(`/api/reviews/save/${userId}`, {
    method: 'POST',
    body: JSON.stringify(review),
    headers: { 'Content-Type': 'application/json' }
  })
    .catch(err => console.error(err.message));
  if (response.status == 200) {
    const responseArr = await response.json();
    console.log(responseArr);
    reviewBtn.innerText = 'My review';
    reviewBtn.removeEventListener('click', showReviewForm);
    reviewBtn.addEventListener('click', toggleUserReview);
    populateUserReview(review);
  }
  hideReviewForm();
}

const showAddPictureForm = () => {
  addPictureForm.style.display = 'block';
  addPictureBtn.disabled = true;
}

const hideAddPictureForm = () => {
  addPictureForm.style.display = 'none';
  addPictureBtn.disabled = false;
  document.querySelector('#file').value = "";
}

const handleUploadPicture = async (e) => {
  e.preventDefault();
  console.log('Upload file!');

  const file = document.querySelector('#file').files[0];
  const formData = new FormData();
  formData.append('file', file);
  formData.append('trail', JSON.stringify(currentTrail));

  hideAddPictureForm();

  const response = await fetch(`/api/pictures/upload/${userId}`, {
    method: 'POST',
    body: formData
  })
    .catch(err => console.error(err.message));
  if (response.status == 200) {
    const responseArr = await response.json();
    console.log(responseArr);
  }
}

const populateTrailPictures = (allPictures) => {
  allPictures.forEach(picture => {
    console.log(picture);
    // picturesContainer.appendChild(createPictureCard(picture));
  });
}

const createPictureCard = (picture) => {
  console.log(picture);
}

revewCancelBtn.addEventListener('click', hideReviewForm);
reviewForm.addEventListener('submit', handleSubmitReview);

addPictureBtn.addEventListener('click', showAddPictureForm);
addPictureCancelBtn.addEventListener('click', hideAddPictureForm);
addPictureForm.addEventListener('submit', handleUploadPicture);
