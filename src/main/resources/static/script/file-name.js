const input = document.getElementById("thumbnail");
const label = document.getElementById("file-label");

input.addEventListener("change", () => {
    label.setAttribute("data-file-name",
        input.files.length > 0 ? input.files[0].name : "ファイル未選択");
});