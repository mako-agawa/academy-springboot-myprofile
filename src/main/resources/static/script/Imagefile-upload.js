const input = document.getElementById("thumbnail");
const label = document.getElementById("file-label");
const hiddenPublicId = document.getElementById("thumbnailPublicId");

const uploadPreset = "unsigned_preset_123"; 

input.addEventListener("change", async () => {
    console.log(cloudinaryCloudName);
    if (input.files.length > 0) {
        const file = input.files[0];

        const max = 3 * 1024 * 1024;
        if (file.size > max) {
            label.textContent = "画像は3MB以下にしてください";
            hiddenPublicId.value = "";
            return;
        }

        label.textContent = "アップロード中...";

        const formData = new FormData();
        formData.append("file", file);
        formData.append("upload_preset", uploadPreset);

        try {
            const res = await fetch(
                `https://api.cloudinary.com/v1_1/${cloudinaryCloudName}/image/upload`,
                { method: "POST", body: formData }
                
            );
            const data = await res.json();

            if (data.error) {
                console.error(data.error);
                label.textContent = "アップロード失敗";
                hiddenPublicId.value = "";
                return;
            }
            hiddenPublicId.value = data.public_id;
            document.getElementById("thumbnailName").value = file.name;
            label.textContent = file.name;
        } catch (err) {
            console.error("Upload failed", err);
            label.textContent = "アップロードに失敗しました";
            hiddenPublicId.value = "";
        }
    } else {
        label.textContent = "ファイル未選択";
        hiddenPublicId.value = "";
    }
});