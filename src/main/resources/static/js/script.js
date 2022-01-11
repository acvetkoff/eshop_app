changeMainImage();

function changeMainImage() {
    setInterval(randomImage, 3000);

    function randomImage() {
        let imageIndexes = [ "1", "2", "3", "4", "5", "6", "7", "8", "9" ];
        let randomIndex = Math.floor(Math.random() * imageIndexes.length);
        document.getElementById("main-image").src = `/img/${randomIndex}.jpg`;
    }
}

let inputNumber = document.querySelector("input[type='number']");
let decrementDiv = document.querySelector(".decrement");

function increment() {
    let newValue = parseInt(inputNumber.value);
    newValue++;
    inputNumber.value = newValue;
    changeColor(newValue, decrementDiv);
}

function decrement() {
    let newValue = parseInt(inputNumber.value);
    newValue--;
    newValue = newValue < 0 ? 0 : newValue;
    inputNumber.value = newValue;
    changeColor(newValue, decrementDiv);
}

function changeColor(newValue, decrementDiv) {
    if (newValue > 1) {
        decrementDiv.style.backgroundColor = "rgb(250, 135, 97)";
        decrementDiv.style.color = "#fff";
    } else {
        decrementDiv.style.backgroundColor = "rgb(242, 242, 242)";
        decrementDiv.style.color = "#333333";
    }
}