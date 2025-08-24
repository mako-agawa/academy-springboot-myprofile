// ===== 削除完了モーダル =====
(function () {
    const body = document.body;
    const doneModal = document.getElementById("doneModal");
    if (!body || !doneModal) return;

    const closeBtn = doneModal.querySelector(".close");
    const okBtn = doneModal.querySelector(".modal-ok");
    const msgEl = doneModal.querySelector("#doneMessage");

    function openDoneModal(message) {
        if (message && msgEl) msgEl.textContent = message;
        doneModal.classList.add("show");      // ← これだけで中央に
        doneModal.setAttribute("aria-hidden", "false");
    }
    function closeDoneModal() {
        doneModal.classList.remove("show");
        doneModal.style.display = "none";
        doneModal.setAttribute("aria-hidden", "true");
    }

    // フラッシュ属性が true のときだけ開く
    const success = body.dataset.deleteSuccess === "true";
    if (success) {
        const title = body.dataset.deletedTitle;
        console.log(`削除完了: ${title}`);
        const message = `${title}を削除しました！`;
        // DOM が描画された後に開く（ちらつき防止）
        window.requestAnimationFrame(() => openDoneModal(message));
    }

    // 閉じる操作
    closeBtn?.addEventListener("click", closeDoneModal);
    okBtn?.addEventListener("click", closeDoneModal);
    window.addEventListener("click", (e) => {
        if (e.target === doneModal) closeDoneModal();
    });
})();