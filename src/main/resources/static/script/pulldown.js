const dropdownBtn = document.getElementById("btn");
const dropdownMenu = document.getElementById("dropdown");
const toggleArrow = document.getElementById("arrow");

const toggleDropdown = () => {
  dropdownMenu.classList.toggle("show");
  toggleArrow.classList.toggle("arrow-up");
  dropdownBtn.setAttribute(
    "aria-expanded",
    dropdownMenu.classList.contains("show")
  );
};

// ボタンクリックで開閉
dropdownBtn.addEventListener("click", (e) => {
  e.stopPropagation();
  toggleDropdown();
});

// 外をクリックしたら閉じる
document.addEventListener("click", (e) => {
  if (dropdownMenu.classList.contains("show") && !dropdownBtn.contains(e.target)) {
    toggleDropdown();
  }
});

console.log("Pulldown script loaded");