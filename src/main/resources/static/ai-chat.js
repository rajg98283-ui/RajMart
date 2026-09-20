(function () {

    "use strict";

    if (window.__RAJ_AI_LOADED__) {
        return;
    }

    window.__RAJ_AI_LOADED__ = true;

    const style = document.createElement("style");

    style.textContent = `
        #raj-ai-button {
            position: fixed;
            right: 24px;
            bottom: 24px;
            width: 62px;
            height: 62px;
            border: none;
            border-radius: 50%;
            background: linear-gradient(135deg, #111827, #2563eb);
            color: white;
            font-size: 28px;
            cursor: pointer;
            z-index: 99999;
            box-shadow: 0 12px 30px rgba(0,0,0,.25);
            transition: transform .2s ease, box-shadow .2s ease;
        }

        #raj-ai-button:hover {
            transform: translateY(-3px) scale(1.04);
            box-shadow: 0 16px 35px rgba(0,0,0,.30);
        }

        #raj-ai-window {
            position: fixed;
            right: 24px;
            bottom: 98px;
            width: 390px;
            max-width: calc(100vw - 32px);
            height: 560px;
            max-height: calc(100vh - 120px);
            background: white;
            border-radius: 22px;
            overflow: hidden;
            z-index: 99998;
            box-shadow: 0 25px 70px rgba(0,0,0,.25);
            border: 1px solid #e5e7eb;
            display: none;
            flex-direction: column;
            font-family: Arial, sans-serif;
        }

        #raj-ai-window.open {
            display: flex;
            animation: rajAiOpen .2s ease;
        }

        @keyframes rajAiOpen {
            from {
                opacity: 0;
                transform: translateY(15px) scale(.97);
            }

            to {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
        }

        .raj-ai-header {
            background: linear-gradient(135deg, #111827, #2563eb);
            color: white;
            padding: 17px 18px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .raj-ai-brand {
            display: flex;
            align-items: center;
            gap: 11px;
        }

        .raj-ai-logo {
            width: 40px;
            height: 40px;
            border-radius: 13px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(255,255,255,.16);
            font-size: 22px;
        }

        .raj-ai-title {
            font-weight: 800;
            font-size: 16px;
        }

        .raj-ai-status {
            font-size: 11px;
            opacity: .8;
            margin-top: 3px;
        }

        .raj-ai-close {
            border: none;
            background: transparent;
            color: white;
            font-size: 24px;
            cursor: pointer;
        }

        #raj-ai-messages {
            flex: 1;
            padding: 17px;
            overflow-y: auto;
            background: #f8fafc;
        }

        .raj-ai-message {
            display: flex;
            margin-bottom: 12px;
        }

        .raj-ai-message.user {
            justify-content: flex-end;
        }

        .raj-ai-bubble {
            max-width: 82%;
            padding: 11px 14px;
            border-radius: 16px;
            font-size: 14px;
            line-height: 1.5;
            white-space: pre-wrap;
            word-break: break-word;
        }

        .raj-ai-message.bot .raj-ai-bubble {
            background: white;
            color: #1f2937;
            border: 1px solid #e5e7eb;
            border-bottom-left-radius: 5px;
        }

        .raj-ai-message.user .raj-ai-bubble {
            background: #2563eb;
            color: white;
            border-bottom-right-radius: 5px;
        }

        .raj-ai-typing {
            display: inline-flex;
            gap: 4px;
            padding: 13px 15px;
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 15px;
        }

        .raj-ai-typing span {
            width: 6px;
            height: 6px;
            border-radius: 50%;
            background: #64748b;
            animation: rajTyping 1s infinite;
        }

        .raj-ai-typing span:nth-child(2) {
            animation-delay: .15s;
        }

        .raj-ai-typing span:nth-child(3) {
            animation-delay: .3s;
        }

        @keyframes rajTyping {
            0%, 60%, 100% {
                transform: translateY(0);
            }

            30% {
                transform: translateY(-4px);
            }
        }

        .raj-ai-suggestions {
            padding: 8px 12px;
            display: flex;
            gap: 7px;
            overflow-x: auto;
            background: white;
            border-top: 1px solid #e5e7eb;
        }

        .raj-ai-suggestion {
            flex: 0 0 auto;
            border: 1px solid #dbeafe;
            background: #eff6ff;
            color: #1d4ed8;
            border-radius: 20px;
            padding: 7px 11px;
            font-size: 12px;
            cursor: pointer;
        }

        .raj-ai-input-area {
            padding: 12px;
            background: white;
            border-top: 1px solid #e5e7eb;
            display: flex;
            gap: 8px;
        }

        #raj-ai-input {
            flex: 1;
            min-width: 0;
            border: 1px solid #d1d5db;
            border-radius: 14px;
            padding: 11px 13px;
            outline: none;
            font-size: 14px;
        }

        #raj-ai-input:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37,99,235,.10);
        }

        #raj-ai-send {
            width: 45px;
            height: 45px;
            border: none;
            border-radius: 13px;
            background: #2563eb;
            color: white;
            cursor: pointer;
            font-size: 18px;
        }

        #raj-ai-send:disabled {
            opacity: .5;
            cursor: not-allowed;
        }

        @media (max-width: 520px) {

            #raj-ai-window {
                right: 12px;
                bottom: 86px;
                width: calc(100vw - 24px);
                height: 70vh;
            }

            #raj-ai-button {
                right: 16px;
                bottom: 16px;
            }
        }
    `;

    document.head.appendChild(style);

    const button = document.createElement("button");

    button.id = "raj-ai-button";
    button.type = "button";
    button.title = "Chat with RAJ AI";
    button.innerHTML = "🤖";

    const chatWindow = document.createElement("div");

    chatWindow.id = "raj-ai-window";

    chatWindow.innerHTML = `
        <div class="raj-ai-header">

            <div class="raj-ai-brand">

                <div class="raj-ai-logo">
                    🤖
                </div>

                <div>
                    <div class="raj-ai-title">
                        RAJ AI
                    </div>

                    <div class="raj-ai-status">
                        Online • Shopping Assistant
                    </div>
                </div>

            </div>

            <button
                class="raj-ai-close"
                id="raj-ai-close"
                type="button">
                ×
            </button>

        </div>

        <div id="raj-ai-messages">

            <div class="raj-ai-message bot">

                <div class="raj-ai-bubble">
                    Vanakkam 👋 I'm RAJ AI.

                    Products, price, stock, cart or order pathi enna venalum kelu macha! 🤖
                </div>

            </div>

        </div>

        <div class="raj-ai-suggestions">

            <button
                class="raj-ai-suggestion"
                data-message="macha enna products available ah iruku?">
                Products
            </button>

            <button
                class="raj-ai-suggestion"
                data-message="cheap ah headphone irukka?">
                Headphones
            </button>

            <button
                class="raj-ai-suggestion"
                data-message="cart epdi use panrathu?">
                Cart Help
            </button>

            <button
                class="raj-ai-suggestion"
                data-message="order epdi place panrathu?">
                Order Help
            </button>

        </div>

        <form
            id="raj-ai-form"
            class="raj-ai-input-area">

            <input
                id="raj-ai-input"
                type="text"
                autocomplete="off"
                placeholder="Ask in Tamil / Tanglish / English..."
            />

            <button
                id="raj-ai-send"
                type="submit">
                ➤
            </button>

        </form>
    `;

    document.body.appendChild(button);
    document.body.appendChild(chatWindow);

    const messages = document.getElementById(
        "raj-ai-messages"
    );

    const input = document.getElementById(
        "raj-ai-input"
    );

    const form = document.getElementById(
        "raj-ai-form"
    );

    const sendButton = document.getElementById(
        "raj-ai-send"
    );

    function openChat() {

        chatWindow.classList.add("open");

        setTimeout(function () {
            input.focus();
        }, 100);
    }

    function closeChat() {
        chatWindow.classList.remove("open");
    }

    button.addEventListener("click", function () {

        if (chatWindow.classList.contains("open")) {
            closeChat();
        } else {
            openChat();
        }

    });

    document.getElementById(
        "raj-ai-close"
    ).addEventListener("click", closeChat);

    function addMessage(text, type) {

        const wrapper = document.createElement("div");

        wrapper.className =
            "raj-ai-message " + type;

        const bubble = document.createElement("div");

        bubble.className =
            "raj-ai-bubble";

        bubble.textContent = text;

        wrapper.appendChild(bubble);

        messages.appendChild(wrapper);

        messages.scrollTop = messages.scrollHeight;
    }

    function addTyping() {

        const wrapper = document.createElement("div");

        wrapper.id = "raj-ai-typing-message";

        wrapper.className =
            "raj-ai-message bot";

        wrapper.innerHTML = `
            <div class="raj-ai-typing">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;

        messages.appendChild(wrapper);

        messages.scrollTop = messages.scrollHeight;
    }

    function removeTyping() {

        const typing =
            document.getElementById(
                "raj-ai-typing-message"
            );

        if (typing) {
            typing.remove();
        }
    }

    async function sendMessage(message) {

        message = String(message || "").trim();

        if (!message) {
            return;
        }

        addMessage(message, "user");

        addTyping();

        sendButton.disabled = true;
        input.disabled = true;

        try {

            const response = await fetch(
                "/api/chatbot",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        message: message
                    })
                }
            );

            const data = await response.json();

            removeTyping();

            if (data && data.reply) {

                addMessage(
                    data.reply,
                    "bot"
                );

            } else {

                addMessage(
                    "Macha, AI response varala. Again try pannu.",
                    "bot"
                );
            }

        } catch (error) {

            console.error(
                "RAJ AI error:",
                error
            );

            removeTyping();

            addMessage(
                "Macha, server connection problem. Konjam later try pannu.",
                "bot"
            );

        } finally {

            sendButton.disabled = false;
            input.disabled = false;
            input.focus();
        }
    }

    form.addEventListener(
        "submit",
        function (event) {

            event.preventDefault();

            const message = input.value.trim();

            if (!message) {
                return;
            }

            input.value = "";

            sendMessage(message);
        }
    );

    document
        .querySelectorAll(".raj-ai-suggestion")
        .forEach(function (suggestion) {

            suggestion.addEventListener(
                "click",
                function () {

                    const message =
                        suggestion.dataset.message;

                    openChat();

                    sendMessage(message);
                }
            );
        });

})();
