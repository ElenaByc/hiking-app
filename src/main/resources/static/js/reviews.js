
const reviewsContainer = document.querySelector('.reviews-container');

const headers = {
  'Content-Type': 'application/json'
};

const getReviews = async () => {
  const response = await fetch(`/api/reviews/${userId}`, {
    method: 'GET',
    headers: headers
  }).catch(err => console.error(err.message));
  const responseArr = await response.json();
  if (responseArr.length === 0) {
    reviewsContainer.innerHTML = '';
    const header = document.createElement('h3');
    header.classList.add('notfound-header');
    header.innerHTML = 'You haven\'t submitted any review so far';
    reviewsContainer.appendChild(header);
  } else {
    console.log(responseArr);
    createReviewsCards(responseArr);
  }
}

const createReviewCard = (review) => {
  let reviewCard = document.createElement('div');
  reviewCard.classList.add('review-card');

  const divImg = document.createElement('div');
  divImg.classList.add('review-card__img');
  const img = document.createElement('img');
  img.setAttribute("src", review.trailDto.image);
  img.setAttribute("alt", `${review.trailDto.name} picture`);
  divImg.appendChild(img);
  reviewCard.appendChild(divImg);

  const divContent = document.createElement('div');
  divContent.classList.add('review-card__content');
  const divInfo = document.createElement('div');
  divInfo.classList.add('review-card__info');

  const trailName = document.createElement('h3');
  trailName.classList.add('trail-name');
  trailName.innerText = review.trailDto.name;
  divInfo.appendChild(trailName);

  const reviewDate = document.createElement('p');
  reviewDate.classList.add('review-date');
  reviewDate.innerText = `Date: ${review.date}`;
  divInfo.appendChild(reviewDate);

  const reviewRating = document.createElement('p');
  reviewRating.classList.add('review-rating');
  reviewRating.innerText = `Rating: ${review.rating}`;
  divInfo.appendChild(reviewRating);

  const reviewBody = document.createElement('p');
  reviewBody.classList.add('review-body');
  reviewBody.innerText = review.body;
  divInfo.appendChild(reviewBody);
  divContent.appendChild(divInfo);

  const divBtns = document.createElement('div');
  divBtns.classList.add('review-card__buttons');
  const editReviewBtn = document.createElement('button');
  editReviewBtn.classList.add('button');
  editReviewBtn.innerText = 'Edit review';
  editReviewBtn.setAttribute('id', `edit-btn-${review.id}`);
  editReviewBtn.addEventListener('click', handleEditReview);
  divBtns.appendChild(editReviewBtn);
  const deleteReviewBtn = document.createElement('button');
  deleteReviewBtn.classList.add('button');
  deleteReviewBtn.innerText = 'Delete';
  deleteReviewBtn.setAttribute('id', `delete-btn-${review.id}`);
  deleteReviewBtn.addEventListener('click', handleDeleteReview);
  divBtns.appendChild(deleteReviewBtn);
  divContent.appendChild(divBtns);
  reviewCard.appendChild(divContent);
  return reviewCard;
}

const handleDeleteReview = async (e) => {
  const reviewCard = e.target.closest('.review-card');
  const reviewId = Number(e.target.id.substring(11));
  console.log(reviewId);
  reviewCard.style.display = 'none';
  const response = await fetch(`/api/reviews/delete/${reviewId}`, {
    method: 'POST',
    headers: headers
  })
    .catch(err => console.error(err.message));
  console.log(response.status);
  const responseArr = await response.json();
  console.log(responseArr);
}

const handleEditReview = async (e) => {
  // const trailCard = e.target.closest('.trail-card');
  // const trailId = Number(e.target.id.substring(11));
  // console.log(trailId);
  // trailCard.style.display = 'none';
  // const response = await fetch(`/api/users/trails/${userId}/remove/${trailId}`, {
  //   method: 'POST',
  //   headers: headers
  // })
  //   .catch(err => console.error(err.message));
  // console.log(response.status);
  // const responseArr = await response.json();
  // console.log(responseArr);
}

const createReviewsCards = (reviews) => {
  reviewsContainer.innerHTML = '';
  reviews.forEach(review => {
    const reviewCard = createReviewCard(review);
    reviewsContainer.append(reviewCard);
  });
}

showLoadingSpinner(reviewsContainer);
getReviews();
