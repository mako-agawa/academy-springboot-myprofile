const input = document.getElementById("thumbnail");
const label = document.getElementById("file-label");
const hiddenPublicId = document.getElementById("thumbnailPublicId");
const button = document.querySelector(".submit-btn");

const uploadPreset = "unsigned_preset_123";

input.addEventListener("change", async () => {
    if (input.files.length > 0) {
        const file = input.files[0];

        const max = 3 * 1024 * 1024;
        if (file.size > max) {
            label.textContent = "画像は3MB以下にしてください";
            label.classList.add("text-danger"); 
            hiddenPublicId.value = "";
            return;
        }

        // ✅ アップロード中はボタン無効化
        button.disabled = true;
        label.textContent = "アップロード中...";

        const formData = new FormData();
        formData.append("file", file);
        formData.append("upload_preset", uploadPreset);

        try {
            const res = await fetch(
                'https://api.cloudinary.com/v1_1/djklqnmen/image/upload',
                { method: "POST", body: formData }
            );
            const data = await res.json();

            if (data.error) {
                console.error(data.error);
                label.textContent = "アップロード失敗";
                hiddenPublicId.value = "";
            } else {
                hiddenPublicId.value = data.public_id;
                document.getElementById("thumbnailName").value = file.name;
                label.textContent = file.name;
            }
        } catch (err) {
            console.error("Upload failed", err);
            label.textContent = "アップロードに失敗しました";
            hiddenPublicId.value = "";
        } finally {
            // ✅ アップロード完了したらボタンを有効化
            button.disabled = false;
        }
    } else {
        label.textContent = "ファイル未選択";
        label.classList.remove("text-danger"); 
        hiddenPublicId.value = "";
    }
});