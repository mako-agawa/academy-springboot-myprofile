(function () {
  const body = document.body;
  const doneModal = document.getElementById("doneModal");
  if (!body || !doneModal) return;

  const okBtn = doneModal.querySelector(".modal-ok");
  const msgElA = doneModal.querySelector("#doneMessageA");
  const msgElB = doneModal.querySelector("#doneMessageB");

  function openDoneModal(messageA, messageB) {
    if (msgElA) msgElA.textContent = messageA ?? "";
    if (msgElB) msgElB.textContent = messageB ?? "";
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
    const msgA = `${category}に${title}を`;
    const msgB = `${time}分で追加しました！`;

    // DOM描画のあとに開くなら1回で十分
    window.requestAnimationFrame(() => openDoneModal(msgA, msgB));
  }

  okBtn?.addEventListener("click", redirectToSkill);
})();