const scrollToTopBtn = document.querySelector('#scroll-to-top-btn');

window.onscroll = () => {
  if (document.documentElement.scrollTop > 200) {
    scrollToTopBtn.style.display = 'block';
  } else {
    scrollToTopBtn.style.display = 'none';
  }
};

const scrollToTop = () => document.documentElement.scroll({
  top: 0,
  behavior: "smooth"
});

scrollToTopBtn.addEventListener('click', scrollToTop)