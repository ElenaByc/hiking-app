const loginBtn = document.querySelector('#login');
const userMenu = document.querySelector('#user-menu');
const userNameEl = document.querySelector('#user-name');

let userId;
let userName;

const handleLogin = () => {
  window.location.replace('./login.html');
}

const hideSaveBtns = () => {
  const allSaveBtns = document.querySelectorAll('.save-btn');
  if (allSaveBtns) {
    allSaveBtns.forEach(btn => {
      btn.style.display = 'none';
    });
    const cardsBtnsDivs = document.querySelectorAll('.trail-card__buttons');
    cardsBtnsDivs.forEach(div => {
      div.style.justifyContent = 'center';
    });
  }
}

const handleLogout = () => {
  const c = document.cookie.split(";");
  for (let i in c) {
    document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
  }
  loginBtn.removeEventListener('click', handleLogout);
  loginBtn.innerText = 'Log In';
  loginBtn.addEventListener('click', handleLogin);
  userMenu.innerHTML = '';
  userMenu.appendChild(loginBtn);
  userId = null;
  userName = null;
  console.log(window.location);
  hideSaveBtns();
}

const getUser = () => {
  console.log(document.cookie);
  const cookieArr = document.cookie.split('; ');
  console.log(cookieArr);
  for (let i = 0; i < cookieArr.length; i++) {
    if (cookieArr[i].includes('userId')) {
      userId = cookieArr[i].split('=')[1];
    }
    if (cookieArr[i].includes('userName')) {
      userName = cookieArr[i].split('=')[1];
    }
  }
  console.log('User Id = ', userId);
  console.log('User Name = ', userName);
  if (userId) {
    loginBtn.addEventListener('click', handleLogout);
    userNameEl.innerText = userName;
  } else {
    userMenu.innerHTML = '';
    userMenu.appendChild(loginBtn);
    loginBtn.innerText = 'Log In';
    loginBtn.addEventListener('click', handleLogin);
  }
}

getUser();