/**
 * RAJ MART - AI Chatbot Floating Widget
 */

class RajMartChatbot {
    constructor() {
        this.isOpen = false;
        this.initWidget();
    }

    initWidget() {
        // Create widget markup if not already present
        if (document.getElementById('rajmart-chatbot-wrapper')) return;

        const wrapper = document.createElement('div');
        wrapper.id = 'rajmart-chatbot-wrapper';
        wrapper.innerHTML = `
            <!-- Floating Launch Button -->
            <button id="chat-launch-btn" class="chat-floating-btn" aria-label="Open AI Assistant">
                <span class="chat-sparkle">✨</span>
                <span>AI Chat</span>
            </button>

            <!-- Chat Panel -->
            <div id="chat-window" class="chat-container chat-hidden">
                <div class="chat-header">
                    <div class="chat-header-info">
                        <div class="chat-avatar">AI</div>
                        <div class="chat-title-group">
                            <h3>RajMart Assistant</h3>
                            <p>● Ready to help</p>
                        </div>
                    </div>
                    <div class="chat-header-actions">
                        <button id="chat-clear-btn" class="chat-header-btn" title="Clear chat">🗑️</button>
                        <button id="chat-close-btn" class="chat-header-btn" title="Close">✕</button>
                    </div>
                </div>

                <div id="chat-messages" class="chat-messages">
                    <div class="chat-bubble bot">
                        👋 Hi! I am your <strong>RajMart AI Assistant</strong>. Ask me about products, order tracking, checkout, cart, or seller features!
                    </div>
                </div>

                <div class="chat-suggestions">
                    <button class="chip-btn" onclick="RajMartChatbot.sendQuickPrompt('Do you have laptops in stock?')">💻 Laptops</button>
                    <button class="chip-btn" onclick="RajMartChatbot.sendQuickPrompt('How does mock checkout work?')">💳 Checkout</button>
                    <button class="chip-btn" onclick="RajMartChatbot.sendQuickPrompt('How can I track my order?')">📦 Orders</button>
                    <button class="chip-btn" onclick="RajMartChatbot.sendQuickPrompt('What is your return policy?')">🔄 Returns</button>
                </div>

                <form id="chat-form" class="chat-input-area">
                    <input type="text" id="chat-input" class="chat-input" placeholder="Ask about products, orders..." maxlength="500" autocomplete="off" />
                    <button type="submit" id="chat-send" class="chat-send-btn" title="Send">➤</button>
                </form>
            </div>
        `;

        document.body.appendChild(wrapper);

        // Bind events
        document.getElementById('chat-launch-btn').addEventListener('click', () => this.toggle());
        document.getElementById('chat-close-btn').addEventListener('click', () => this.toggle());
        document.getElementById('chat-clear-btn').addEventListener('click', () => this.clearMessages());
        document.getElementById('chat-form').addEventListener('submit', (e) => this.handleSubmit(e));
    }

    toggle() {
        const win = document.getElementById('chat-window');
        this.isOpen = !this.isOpen;
        if (this.isOpen) {
            win.classList.remove('chat-hidden');
            document.getElementById('chat-input').focus();
        } else {
            win.classList.add('chat-hidden');
        }
    }

    clearMessages() {
        const msgs = document.getElementById('chat-messages');
        msgs.innerHTML = `
            <div class="chat-bubble bot">
                Chat cleared. How can I assist you with RajMart today?
            </div>
        `;
    }

    async handleSubmit(e) {
        if (e) e.preventDefault();
        const input = document.getElementById('chat-input');
        const text = input.value.trim();
        if (!text) return;

        input.value = '';
        this.appendMessage(text, 'user');

        const typingIndicator = this.showTyping();

        try {
            const response = await fetch('/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text })
            });

            const data = await response.json();
            typingIndicator.remove();

            if (response.ok && data.success && data.data) {
                this.appendMessage(data.data.reply, 'bot');
            } else {
                const errMsg = data?.error?.message || 'Sorry, the AI service is temporarily unavailable. Please try again.';
                this.appendMessage(errMsg, 'bot error-bubble');
            }
        } catch (err) {
            typingIndicator.remove();
            this.appendMessage('Sorry, the AI service is temporarily unavailable. Please try again.', 'bot error-bubble');
        }
    }

    appendMessage(text, type) {
        const container = document.getElementById('chat-messages');
        const bubble = document.createElement('div');
        bubble.className = `chat-bubble ${type}`;
        bubble.innerHTML = escapeHtml(text).replace(/\n/g, '<br/>');
        container.appendChild(bubble);
        container.scrollTop = container.scrollHeight;
    }

    showTyping() {
        const container = document.getElementById('chat-messages');
        const typing = document.createElement('div');
        typing.className = 'chat-typing';
        typing.innerHTML = `
            <div class="typing-dot"></div>
            <div class="typing-dot"></div>
            <div class="typing-dot"></div>
        `;
        container.appendChild(typing);
        container.scrollTop = container.scrollHeight;
        return typing;
    }

    static sendQuickPrompt(prompt) {
        const input = document.getElementById('chat-input');
        if (input) {
            input.value = prompt;
            const event = new Event('submit', { cancelable: true });
            document.getElementById('chat-form').dispatchEvent(event);
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    window.rajMartChatbot = new RajMartChatbot();
});
