(function () {
    const body = document.body;
    const doneModal = document.getElementById("doneModal");
    if (!body || !doneModal) return;

    const okBtn = doneModal.querySelector(".modal-ok");
    const msgEl = doneModal.querySelector("#doneMessage");

    function openDoneModal(message) {
        if (message && msgEl) msgEl.textContent = message;
        doneModal.classList.add("show");
        doneModal.setAttribute("aria-hidden", "false");
    }
    function closeDoneModal() {
        doneModal.classList.remove("show");
        doneModal.setAttribute("aria-hidden", "true");
    }

    function redirectToSkill() {
        const month = body.dataset.redirectMonth || "";
        const url = month ? `/skill?month=${encodeURIComponent(month)}` : "/skill";
        window.location.href = url;
    }

    const success = body.dataset.addSuccess === "true";
    if (success) {
        const title = body.dataset.addTitle || "";
        const time = body.dataset.addTime || "";
        const category = body.dataset.addCategory || "";
        const msg = `${category}「${title}」を ${time} 分で登録しました。`;
        window.requestAnimationFrame(() => openDoneModal(msg));
    }

    okBtn?.addEventListener("click", redirectToSkill);
    // 背景クリックで閉じたいなら↓を有効化（今回はすぐ戻る導線なので不要なら消してOK）
    // window.addEventListener("click", (e) => { if (e.target === doneModal) closeDoneModal(); });
})();