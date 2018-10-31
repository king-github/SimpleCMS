
document.addEventListener('DOMContentLoaded', function () {

    const menuButton = document.querySelector('#menuBtn');
    const nav = document.querySelector('#navbar');
    const header = document.querySelector('#header');
    const transparentFooter = document.querySelector('#transparentFooter');
    const topOfFooter = document.querySelector('#topOfFooter');

    window.addEventListener('scroll', onScrollPage);
    menuButton.addEventListener('click', toggleMenu);


    function toggleMenu() {

        nav.classList.toggle('showMenu');
    }

    function onScrollPage() {

        let bottomOfHeaderOffset = header.offsetTop + header.offsetHeight;
        let topOfFooterOffset = topOfFooter.offsetTop;

        //console.log (`pageYOffset {} header {} : {} footer {} {}`, window.pageYOffset, nav.offsetTop, bottomOfHeaderOffset, topOfFooter, transparentFooter.offsetTop);

        if (window.pageYOffset > bottomOfHeaderOffset) {
            nav.classList.add('sticky');
        } else {
            nav.classList.remove('sticky');
        }

        if (window.pageYOffset >= topOfFooterOffset) {
            footer.classList.add('nonFixed');
            transparentFooter.style.display = 'none';
        } else {
            footer.classList.remove('nonFixed');
            transparentFooter.style.display = 'block';
        }
    }

}, false);
