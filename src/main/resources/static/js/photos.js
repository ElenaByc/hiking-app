const userPhotosContainer = document.querySelector('#user-photos-container');

const getUserPhotos = async () => {
  const response = await fetch(`/api/pictures/user/${userId}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
  }).catch(err => console.error(err.message));
  const responseArr = await response.json();
  if (responseArr.length === 0) {
    savedTrailsContainer.innerHTML = '';
    const header = document.createElement('h3');
    header.classList.add('notfound-header');
    header.innerHTML = 'You haven\'t uploaded any photo so far';
    userPhotosContainer.appendChild(header);
  } else {
    createUserPhotoCards(responseArr);
  }
}

const createUserPhotoCards = (photos) => {
  console.log(photos);
  userPhotosContainer.innerHTML = '';
  userPhotosContainer.style.flexDirection = 'row';
  photos.forEach(photo => {
    const photoCard = createPhotoCard(photo);
    userPhotosContainer.appendChild(photoCard);
  });
}

const createPhotoCard = (photo) => {
  const photoCard = document.createElement('div');
  photoCard.classList.add('photo-card');
  photoCard.setAttribute('id', `photo-${photo.id}`);

  const img = document.createElement('img');
  img.classList.add('photo-card__img');
  img.setAttribute("src", photo.url);
  img.setAttribute("alt", `${photo.trailDto.name} picture`);
  photoCard.appendChild(img);

  const trailName = document.createElement('h4');
  trailName.classList.add('photo-card__trail');
  trailName.innerText = photo.trailDto.name;
  photoCard.appendChild(trailName);


  return photoCard;
}

// userPhotosContainer.style.flexDirection = 'row';
showLoadingSpinner(userPhotosContainer);
getUserPhotos();