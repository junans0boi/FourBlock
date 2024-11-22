let slideIndex = 0;

function showSlide(index) {
    const slides = document.querySelectorAll('.slide');
    const totalSlides = slides.length;

    // Wrap around slide index if out of bounds
    if (index >= totalSlides) slideIndex = 0;
    else if (index < 0) slideIndex = totalSlides - 1;
    else slideIndex = index;

    // Set transform to display the correct slide
    document.querySelector('.slides').style.transform = `translateX(-${slideIndex * 100}%)`;

    // Update dots
    updateDots(slideIndex);
}

function moveSlide(step) {
    showSlide(slideIndex + step);
}

function currentSlide(index) {
    showSlide(index - 1);  // Convert to zero-based index
}

function updateDots(index) {
    const dots = document.querySelectorAll('.dot');
    dots.forEach(dot => dot.classList.remove('active'));
    dots[index].classList.add('active');
}

// Initialize slider
showSlide(slideIndex);
