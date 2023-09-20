const showLoadingSpinner = (container) => {
  container.innerHTML = '';
  const loadingHeader = document.createElement('h3');
  loadingHeader.classList.add('loading-header');
  loadingHeader.innerText = 'Loading';
  container.appendChild(loadingHeader);
  const spinnerDiv = document.createElement('div');
  spinnerDiv.classList.add('loading-spinner');
  let circle;
  for (let i = 0; i < 12; i++) {
    circle = document.createElement('div');
    circle.classList.add('circle');
    spinnerDiv.appendChild(circle);
  }
  container.appendChild(spinnerDiv);
}