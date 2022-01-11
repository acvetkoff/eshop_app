let checkOutBtn = document.getElementById('checkOutBtn');
let closeBtn = document.getElementById('closeBtn');

closeBtn.addEventListener('click', function(e) {
    e.preventDefault();
    let modal = document.getElementById('modal');
    modal.style.display = 'none';
});

checkOutBtn.addEventListener('onload', function(e) {
    e.preventDefault();
    
    let modal = document.getElementById('modal');
    modal.style.display = 'block';
});