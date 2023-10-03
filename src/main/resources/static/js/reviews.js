
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
  reviewDate.setAttribute('id', `review-date-${review.id}`);
  reviewDate.innerText = `Date: ${review.date}`;
  divInfo.appendChild(reviewDate);

  const reviewRating = document.createElement('p');
  reviewRating.classList.add('review-rating');
  reviewRating.setAttribute('id', `review-rating-${review.id}`);
  reviewRating.innerHTML = `Rating: <span>${review.rating}</span>`;
  divInfo.appendChild(reviewRating);

  const reviewBody = document.createElement('p');
  reviewBody.classList.add('review-body');
  reviewBody.setAttribute('id', `review-body-${review.id}`)
  reviewBody.innerText = review.body;
  divInfo.appendChild(reviewBody);


  const editReviewForm = document.createElement('form');
  editReviewForm.setAttribute('id', `edit-form-${review.id}`);
  editReviewForm.classList.add('edit-review-form');
  editReviewForm.style.display = 'none';
  const editFormHeader = document.createElement('h4');
  editFormHeader.innerText = 'Edit review';
  editReviewForm.appendChild(editFormHeader);
  const div1 = document.createElement('div');
  div1.classList.add('mb-3');
  const textarea = document.createElement('textarea');
  textarea.classList.add('form-control');
  textarea.setAttribute('id', `edit-review-body-${review.id}`);
  textarea.setAttribute('rows', '7');
  textarea.setAttribute('cols', '50');
  textarea.required = true;
  textarea.value = `${review.body}`;
  div1.appendChild(textarea);
  editReviewForm.appendChild(div1);
  const div2 = document.createElement('div');
  div2.classList.add('mb-3');
  div2.classList.add('edit-form-rating');
  const p = document.createElement('div');
  p.innerText = 'Rating:';
  div2.appendChild(p);
  const select = document.createElement('select');
  select.required = true;
  select.classList.add('form-select');
  select.classList.add('form-select-md');
  select.setAttribute('name', `edit-review-rating-${review.id}`);
  select.setAttribute('id', `edit-review-rating-${review.id}`);
  select.value = review.rating;
  const option1 = document.createElement('option');
  option1.setAttribute('value', '1');
  // if (review.rating == 1) {
  //   option1.selected = true;
  // }
  option1.innerText = '1 - Not good';
  select.appendChild(option1);
  const option2 = document.createElement('option');
  option2.setAttribute('value', '2');
  // if (review.rating == 2) {
  //   option2.selected = true;
  // }
  option2.innerText = '2 - Could’ve been better';
  select.appendChild(option2);
  const option3 = document.createElement('option');
  option3.setAttribute('value', '3');
  // if (review.rating == 3) {
  //   option3.selected = true;
  // }
  option3.innerText = '3 - OK';
  select.appendChild(option3);
  const option4 = document.createElement('option');
  option4.setAttribute('value', '4');
  // if (review.rating == 4) {
  //   option4.selected = true;
  // }
  option4.innerText = '4 - Good';
  select.appendChild(option4);
  const option5 = document.createElement('option');
  option5.setAttribute('value', '5');
  // if (review.rating == 5) {
  //   option5.selected = true;
  // }
  option5.innerText = '5 - Great!';
  select.appendChild(option5);
  select.value = review.rating;
  div2.appendChild(select);
  editReviewForm.appendChild(div2);

  const div3 = document.createElement('div');
  div3.classList.add('mb-3');
  div3.classList.add('edit-form-btns');
  const inputBtn = document.createElement('input');
  inputBtn.setAttribute('type', 'submit');
  inputBtn.classList.add('button');
  inputBtn.setAttribute('value', 'Submit review');
  div3.appendChild(inputBtn);
  const cancelBtn = document.createElement('button');
  cancelBtn.setAttribute('type', 'button');
  cancelBtn.classList.add('button');
  cancelBtn.setAttribute('id', `edit-cancel-btn-${review.id}`);
  cancelBtn.innerText = 'Cancel editing';
  cancelBtn.addEventListener('click', handleCancelEditing);
  div3.appendChild(cancelBtn);
  editReviewForm.appendChild(div3);
  editReviewForm.addEventListener('submit', handleUpdateReview);
  divInfo.appendChild(editReviewForm);
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

const handleEditReview = (e) => {
  const reviewId = Number(e.target.id.substring(9));
  console.log(reviewId);
  const reviewBody = document.querySelector(`#review-body-${reviewId}`);
  const reviewRating = document.querySelector(`#review-rating-${reviewId}`);
  const reviewDate = document.querySelector(`#review-date-${reviewId}`);
  const editReviewForm = document.querySelector(`#edit-form-${reviewId}`);
  editReviewForm.style.display = 'block';
  const textarea = document.querySelector(`#edit-review-body-${reviewId}`);
  textarea.focus();
  textarea.setSelectionRange(textarea.value.length, textarea.value.length);
  reviewBody.style.display = 'none';
  reviewRating.style.display = 'none';
  reviewDate.style.display = 'none';
  document.querySelector(`#edit-btn-${reviewId}`).disabled = true;
}

const handleCancelEditing = (e) => {
  const i = e.target.id.lastIndexOf('-');
  const reviewId = Number(e.target.id.substring(i + 1));
  console.log('Cancel editing, review Id = ', reviewId);
  // restore review data
  const reviewBody = document.querySelector(`#review-body-${reviewId}`);
  const reviewRating = document.querySelector(`#review-rating-${reviewId}`);
  const reviewRatingValue = document.querySelector(`#review-rating-${reviewId} span`);
  const reviewDate = document.querySelector(`#review-date-${reviewId}`);
  const textarea = document.querySelector(`#edit-review-body-${reviewId}`);
  const selectRating = document.querySelector(`#edit-review-rating-${reviewId}`);
  // textarea.value = '';
  textarea.value = reviewBody.innerText;
  selectRating.value = reviewRatingValue.innerText;
  // hide editing form
  const editReviewForm = document.querySelector(`#edit-form-${reviewId}`);
  editReviewForm.style.display = 'none';
  document.querySelector(`#edit-btn-${reviewId}`).disabled = false;
  // show prev review vfields
  reviewBody.style.display = 'block';
  reviewRating.style.display = 'block';
  reviewDate.style.display = 'block';

}

const handleUpdateReview = async (e) => {
  e.preventDefault();
  console.log('Update Review!!!!')
}


// trailCard.style.display = 'none';
// const response = await fetch(`/api/users/trails/${userId}/remove/${trailId}`, {
//   method: 'POST',
//   headers: headers
// })
//   .catch(err => console.error(err.message));
// console.log(response.status);
// const responseArr = await response.json();
// console.log(responseArr);


const createReviewsCards = (reviews) => {
  reviewsContainer.innerHTML = '';
  reviews.forEach(review => {
    const reviewCard = createReviewCard(review);
    reviewsContainer.append(reviewCard);
  });
}

showLoadingSpinner(reviewsContainer);
getReviews();
