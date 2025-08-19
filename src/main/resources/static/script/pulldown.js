const dropdownBtn = document.getElementById("btn");
const dropdownMenu = document.getElementById("dropdown");
const toggleArrow = document.getElementById("arrow");
// Toggle dropdown function
const toggleDropdown = function () {
    dropdownMenu.classList.toggle("show");
    toggleArrow.classList.toggle("arrow");
    console.log("toggleDropdown called");
};

// Toggle dropdown open/close when dropdown button is clicked
dropdownBtn.addEventListener("click", function (e) {
    e.stopPropagation();
    toggleDropdown();
    console.log("addEventListener called");
});

// Close dropdown when dom element is clicked
document.documentElement.addEventListener("click", function () {
    if (dropdownMenu.classList.contains("show")) {
        toggleDropdown();
        console.log("document.documentElement called");
    }
});


console.log("Pulldown script loaded");