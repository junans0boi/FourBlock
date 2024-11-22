document.addEventListener("DOMContentLoaded", () => {
    const totalPriceElement = document.getElementById("total-price");
    const finalTotalElement = document.getElementById("final-total");

    // 전체 선택/해제 기능
    document.getElementById("select-all").addEventListener("change", (e) => {
        const isChecked = e.target.checked;
        document.querySelectorAll(".item-select").forEach((checkbox) => {
            checkbox.checked = isChecked;
        });
        calculateTotalPrice();
    });

    // 개별 선택시 가격 계산
    document.querySelectorAll(".item-select").forEach((checkbox) => {
        checkbox.addEventListener("change", calculateTotalPrice);
    });

    // 총 금액 계산
    function calculateTotalPrice() {
        let total = 0;
        document.querySelectorAll(".item-select:checked").forEach((checkbox) => {
            const price = parseFloat(checkbox.dataset.price);
            const quantity = parseInt(checkbox.dataset.quantity);
            total += price * quantity;
        });

        totalPriceElement.textContent = `₩${total.toLocaleString()}`;
        finalTotalElement.textContent = totalPriceElement.textContent;
    }

    // 개별 상품 삭제
    document.querySelectorAll(".delete-item").forEach((button) => {
        button.addEventListener("click", () => {
            const productId = button.dataset.productId;
            deleteItem(productId);
        });
    });

    // 선택 상품 삭제
    document.querySelector(".delete-selected").addEventListener("click", () => {
        const selectedItems = Array.from(document.querySelectorAll(".item-select:checked"));
        if (selectedItems.length === 0) {
            alert("삭제할 상품을 선택하세요.");
            return;
        }

        const promises = selectedItems.map((checkbox) => {
            const productId = checkbox.closest(".cart-item").querySelector(".delete-item").dataset.productId;
            return deleteItem(productId, false);
        });

        Promise.all(promises).then(() => {
            alert("선택한 상품이 삭제되었습니다.");
            location.reload();
        });
    });

    // 상품 삭제 API 호출
    async function deleteItem(productId, reload = true) {
        try {
            const response = await fetch("deleteFromCart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({ productId }),
            });

            if (response.ok && reload) {
                alert("상품이 삭제되었습니다.");
                location.reload();
            }
        } catch (error) {
            console.error("Error:", error);
            alert("상품 삭제 중 오류가 발생했습니다.");
        }
    }
});
