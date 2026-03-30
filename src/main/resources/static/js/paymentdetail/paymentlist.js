document.addEventListener("DOMContentLoaded", () => {
    const listRoot = document.querySelector(".my-order-list");
    if (!listRoot) {
        return;
    }

    const titleElement = document.querySelector(".content_title .title h3");
    const tabItems = Array.from(document.querySelectorAll(".purchase_list_tab .tab_item"));
    const tabConfig = [
        { key: "bid", label: "입찰 내역" },
        { key: "buy", label: "구매 내역" },
        { key: "sell", label: "판매 내역" }
    ];

    if (titleElement) {
        titleElement.textContent = "입찰 내역";
    }

    tabItems.forEach((tab, index) => {
        const config = tabConfig[index];
        if (!config) {
            return;
        }

        tab.classList.toggle("tab_on", config.key === "bid");
        const title = tab.querySelector(".title");
        const count = tab.querySelector(".count");

        if (title) {
            title.textContent = config.label;
        }

        if (count) {
            count.textContent = "0";
        }

        const link = tab.querySelector("a");
        if (link) {
            link.addEventListener("click", (event) => event.preventDefault());
        }
    });

    function formatPrice(value) {
        return Number(value || 0).toLocaleString("ko-KR");
    }

    function formatDate(value) {
        if (!value) return "-";
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) return "-";
        return date.toLocaleDateString("ko-KR");
    }

    function escapeHtml(value) {
        return String(value ?? "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function getStatusLabel(item) {
        if (item.isWinning) {
            return "낙찰완료";
        }
        return "입찰종료";
    }

    function getStatusColor(item) {
        return item.isWinning ? "#0f9d58" : "#F15746";
    }

    function renderEmpty() {
        listRoot.innerHTML = '<p class="type_empty">입찰 내역이 없습니다.</p>';
    }

    function renderBidHistory(items) {
        if (!items.length) {
            renderEmpty();
            return;
        }

        listRoot.innerHTML = items.map((item, index) => `
            ${index > 0 ? `
                <div class="divider_horizontal pc:h-1 pc:pr-16 pc:pl-16 tablet:h-1 tablet:pr-16 tablet:pl-16 mo:h-1 mo:pr-16 mo:pl-16">
                    <div class="divider" style="--818b18a2: #F0F0F0; --1696def5: #F0F0F0; --4b2763fc: #F0F0F0;"></div>
                </div>
            ` : ""}
            <a class="product_list_info_action pc:pt-16 pc:pr-16 pc:pb-16 pc:pl-16 tablet:pt-16 tablet:pr-16 tablet:pb-16 tablet:pl-16 mo:pt-16 mo:pr-16 mo:pb-16 mo:pl-16 sdui-cursor-pointer"
               href="#"
               draggable="false">
                <div class="content">
                    <div class="content__thumbnail-text">
                        <div class="thumbnail pc:overflow-hidden flex-shrink-0 pc:br-tl-10 pc:br-tr-10 pc:br-br-10 pc:br-bl-10 tablet:overflow-hidden tablet:br-tl-10 tablet:br-tr-10 tablet:br-br-10 tablet:br-bl-10 mo:overflow-hidden mo:br-tl-10 mo:br-tr-10 mo:br-br-10 mo:br-bl-10 pc:bg-color tablet:bg-color mo:bg-color thumbnail"
                             style="--background-color-pc: #ffffff; --background-color-tablet: #ffffff; --background-color: #ffffff;">
                            <div class="image-element pc:w-72 pc:h-72 tablet:w-72 tablet:h-72 mo:w-72 mo:h-72"
                                 style="--object-fit: cover; --object-fit-tablet: cover; --object-fit-pc: cover;">
                                <div class="image_container">
                                    <picture class="picture image">
                                        <img class="img base-image-responsive__image"
                                             loading="lazy"
                                             width="72"
                                             height="72"
                                             src="${escapeHtml(item.workThumbnail || "/images/BIDEO_LOGO/BIDEO_favicon.png")}"
                                             style="width: 72px; height: 72px;">
                                    </picture>
                                </div>
                            </div>
                        </div>
                        <div class="layout_list_vertical pc:rgap-4 tablet:rgap-4 mo:rgap-4 list-vertical-fill-available text_item">
                            <div class="text_body pc:cgap-2 tablet:cgap-2 mo:cgap-2">
                                <p class="text-lookup display_paragraph line_break_by_truncating_tail text-element"
                                   style="width: inherit; -webkit-line-clamp: 2; --sd-text-element-size: 13px; --sd-text-element-color: #222222;">
                                    ${escapeHtml(item.workTitle || "작품")}
                                </p>
                            </div>
                            <div class="text-table-options text-table-options--action option_item">
                                <div class="text_body pc:cgap-3 tablet:cgap-3 mo:cgap-3 option option1_item">
                                    <p class="semibold text-lookup display_paragraph line_break_by_truncating_tail ellipsis-1 text-element"
                                       style="width: inherit; -webkit-line-clamp: 1; --sd-text-element-size: 12px; --sd-text-element-color: #222222;">
                                        ${escapeHtml(item.sellerNickname || "-")}
                                    </p>
                                </div>
                                <div class="option-right-text"></div>
                            </div>
                        </div>
                    </div>
                    <div class="text_body pc:cgap-2 tablet:cgap-2 mo:cgap-2 caption_item"
                         style="--sd-text-body-justify-content: flex-end;">
                        <p class="text-lookup display_paragraph text-element"
                           style="width: inherit; --sd-text-element-size: 13px; --sd-text-element-color: #222222; --sd-text-element-alignment: right;">
                            내 입찰가 ${formatPrice(item.myBidPrice)}원
                        </p>
                        <p class="text-lookup display_paragraph text-element"
                           style="width: inherit; --sd-text-element-size: 12px; --sd-text-element-color: #555; --sd-text-element-alignment: right; margin-top: 4px;">
                            종료 ${formatDate(item.closingAt)}
                        </p>
                    </div>
                    <div class="layout_list_vertical pc:rgap-2 tablet:rgap-2 mo:rgap-2 list-vertical-fill-available label_item">
                        <div class="text_body pc:w-fill pc:cgap-2 tablet:w-fill tablet:cgap-2 mo:w-fill mo:cgap-2"
                             style="--sd-text-body-justify-content: flex-end;">
                            <p class="text-lookup display_paragraph text-element"
                               style="width: inherit; word-break: keep-all; --sd-text-element-size: 13px; --sd-text-element-color: ${getStatusColor(item)}; --sd-text-element-alignment: right;">
                                ${getStatusLabel(item)}
                            </p>
                        </div>
                        <div class="text_body pc:w-fill pc:cgap-2 tablet:w-fill tablet:cgap-2 mo:w-fill mo:cgap-2"
                             style="--sd-text-body-justify-content: flex-end;">
                            <p class="text-lookup display_paragraph text-element"
                               style="width: inherit; --sd-text-element-size: 12px; --sd-text-element-color: #222222; --sd-text-element-alignment: right;">
                                ${item.isWinning ? `낙찰가 ${formatPrice(item.finalPrice || item.currentPrice)}원` : `최종가 ${formatPrice(item.finalPrice || item.currentPrice)}원`}
                            </p>
                        </div>
                    </div>
                </div>
            </a>
        `).join("");

        const bidCountElement = tabItems[0]?.querySelector(".count");
        if (bidCountElement) {
            bidCountElement.textContent = String(items.length);
        }
    }

    fetch("/api/auctions/my-bids")
        .then(async (response) => {
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || "입찰 내역을 불러오지 못했습니다.");
            }
            return response.json();
        })
        .then((items) => {
            renderBidHistory(Array.isArray(items) ? items : []);
        })
        .catch(() => {
            renderEmpty();
        });
});
