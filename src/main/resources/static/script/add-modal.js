document.addEventListener("DOMContentLoaded", () => {
  const root  = document.body;
  const modal = document.getElementById("doneModal");
  const msgA  = document.getElementById("doneMessageA");
  const msgB  = document.getElementById("doneMessageB");
  const okBtn = document.querySelector(".modal-ok");

  const addSuccess = root.dataset.addSuccess === "true";
  const title = root.dataset.addTitle || "";
  const time  = root.dataset.addTime || "";
  const cat   = root.dataset.addCategory || "";
  const month = root.dataset.redirectMonth || "";

  // 閉じる（→ リダイレクト）
  okBtn?.addEventListener("click", () => {
    console.log("モーダル2");
    modal.classList.remove("show");          // ← show を外すだけ
    modal.setAttribute("aria-hidden", "true");
    window.location.href = `/skill?month=${encodeURIComponent(month)}`;
  });

  if (!addSuccess) return;

  // 開く
  msgA.textContent = `${cat}に${title}を`;
  msgB.textContent = `${time}分で追加しました。`;
  modal.classList.add("show");               // ← show を付けるだけ
  modal.setAttribute("aria-hidden", "false");
});