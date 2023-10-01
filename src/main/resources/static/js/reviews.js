
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
  reviewRating.innerText = `Rating: ${review.rating}`;
  divInfo.appendChild(reviewRating);

  const reviewBody = document.createElement('p');
  reviewBody.classList.add('review-body');
  reviewBody.setAttribute('id', `review-body-${review.id}`)
  reviewBody.innerText = review.body;
  divInfo.appendChild(reviewBody);


  const editReviewForm = document.createElement('form');
  editReviewForm.setAttribute('id', `edit-form-${review.id}`);
  editReviewForm.classList.add('edit-review-from');
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
  textarea.innerText = `${review.body}`;
  div1.appendChild(textarea);
  editReviewForm.appendChild(div1);

  // reviewEditForm.innerHTML = `
  //           <div class="mb-3">
  //             <textarea id="review-body" class="form-control" rows="7" cols="50"
  //               placeholder="Type your review here..." required></textarea>
  //           </div>
  //           <select class="form-select form-select-md mb-3" name="rating" id="rating" required>
  //             <option value="" selected disabled>--Select your rating--</option>
  //             <option value="1">1 - Not good</option>
  //             <option value="2">2 - Could’ve been better</option>
  //             <option value="3">3 - OK</option>
  //             <option value="4">4 - Good</option>
  //             <option value="5">5 - Great!</option>
  //           </select>
  //           <div class="mb-3 review-btns">
  //             <input type="submit" class="button" value="Submit review">
  //             <button class="button" id="revew-cancel-btn">Cancel</button>
  //           </div>`;

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

const handleEditReview = async (e) => {
  const reviewId = Number(e.target.id.substring(9));
  console.log(reviewId);
  const reviewBody = document.querySelector(`#review-body-${reviewId}`);
  const reviewRating = document.querySelector(`#review-rating-${reviewId}`);
  const reviewDate = document.querySelector(`#review-date-${reviewId}`);
  const editReviewForm = document.querySelector(`#edit-form-${reviewId}`);
  editReviewForm.style.display = 'block';
  const textarea = document.querySelector(`#edit-review-body-${reviewId}`);
  console.log(textarea);
  textarea.focus();
  textarea.setSelectionRange(textarea.value.length, textarea.value.length);
  reviewBody.style.display = 'none';
  reviewRating.style.display = 'none';
  reviewDate.style.display = 'none';
  document.querySelector(`#edit-btn-${reviewId}`).disabled = true;




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
