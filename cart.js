/**
 * RAJ MART - Shopping Cart Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in to view your shopping cart.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }
    if (user.role !== 'BUYER') {
        document.getElementById('cart-items-container').innerHTML = `
            <div style="text-align: center; padding: 40px;">
                <h3>Cart Unavailable for ${user.role}s</h3>
                <p style="color: #666; margin-top: 8px;">Shopping carts are reserved for buyer accounts.</p>
                <a href="/" class="btn-primary" style="margin-top: 16px;">Go to Homepage</a>
            </div>
        `;
        document.getElementById('cart-summary-box').style.display = 'none';
        return;
    }

    loadCart();
});

async function loadCart() {
    const container = document.getElementById('cart-items-container');
    try {
        const res = await apiClient.get('/cart');
        if (res && res.success && res.data) {
            const cart = res.data;
            renderCart(cart);
            updateCartSummary(cart);
        }
    } catch (err) {
        container.innerHTML = `<div style="color: #b12704; padding: 20px;">Failed to load cart: ${escapeHtml(err.message)}</div>`;
    }
}

function renderCart(cart) {
    const container = document.getElementById('cart-items-container');
    const checkoutBtn = document.getElementById('btn-proceed-checkout');

    if (!cart.items || cart.items.length === 0) {
        container.innerHTML = `
            <div style="text-align: center; padding: 50px 20px;">
                <div style="font-size: 48px; margin-bottom: 12px;">🛒</div>
                <h2>Your RajMart Cart is empty</h2>
                <p style="color: #666; margin-top: 8px;">Your shopping cart is waiting. Give it purpose — fill it with electronics, fashion, and books!</p>
                <a href="/products.html" class="btn-primary" style="margin-top: 20px;">Continue Shopping</a>
            </div>
        `;
        if (checkoutBtn) checkoutBtn.disabled = true;
        return;
    }

    if (checkoutBtn) checkoutBtn.disabled = false;

    container.innerHTML = cart.items.map(item => `
        <div style="display: flex; gap: 20px; padding: 20px; background: var(--surface); box-shadow: var(--neo-raised-sm); border: var(--neo-border); border-radius: var(--radius-md); margin-bottom: 16px; align-items: flex-start;">
            <a href="/product.html?id=${item.productId}" style="width: 110px; height: 110px; display: flex; align-items: center; justify-content: center; background: var(--surface); box-shadow: var(--neo-inset); border: var(--neo-border); border-radius: var(--radius-md); padding: 10px; flex-shrink: 0;">
                <img src="${escapeHtml(item.productImageUrl)}" alt="${escapeHtml(item.productName)}" style="max-width: 100%; max-height: 100%; object-fit: contain;" />
            </a>

            <div style="flex: 1;">
                <a href="/product.html?id=${item.productId}" style="font-size: 16px; font-weight: 700; color: var(--text-primary); margin-bottom: 6px; display: inline-block;">
                    ${escapeHtml(item.productName)}
                </a>
                <div style="font-size: 12px; margin-bottom: 12px;">
                    ${item.stockQuantity > 0 ? '<span class="stock-tag in-stock">In Stock</span>' : '<span class="stock-tag out-stock">Out of Stock</span>'}
                </div>

                <div style="display: flex; align-items: center; gap: 14px;">
                    <div style="display: flex; align-items: center; background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-pill); overflow: hidden; padding: 2px;">
                        <button style="border: none; background: transparent; padding: 6px 14px; cursor: pointer; font-size: 16px; font-weight: 800; color: var(--text-primary);" onclick="updateItemQty(${item.id}, ${item.quantity - 1})">-</button>
                        <span style="padding: 4px 12px; font-weight: 800; font-size: 14px; color: var(--text-primary);">${item.quantity}</span>
                        <button style="border: none; background: transparent; padding: 6px 14px; cursor: pointer; font-size: 16px; font-weight: 800; color: var(--text-primary);" onclick="updateItemQty(${item.id}, ${item.quantity + 1})">+</button>
                    </div>

                    <button class="btn-secondary" style="padding: 7px 14px; font-size: 12.5px;" onclick="removeItem(${item.id})">Delete</button>
                    <button class="btn-secondary" style="padding: 7px 14px; font-size: 12.5px;" onclick="saveForLater(${item.productId}, ${item.id})">Save for later</button>
                </div>
            </div>

            <div style="font-size: 20px; font-weight: 800; color: var(--text-primary); letter-spacing: -0.5px;">
                ${formatPrice(item.subtotal)}
            </div>
        </div>
    `).join('');
}

function updateCartSummary(cart) {
    document.getElementById('summary-items-count').textContent = cart.itemCount || 0;
    document.getElementById('summary-subtotal').textContent = formatPrice(cart.subtotal);
    document.getElementById('summary-tax').textContent = formatPrice(cart.tax);
    document.getElementById('summary-shipping').textContent = cart.shipping > 0 ? formatPrice(cart.shipping) : 'FREE';
    document.getElementById('summary-total').textContent = formatPrice(cart.total);
    updateCartCount();
}

async function updateItemQty(itemId, newQty) {
    try {
        const res = await apiClient.put(`/cart/items/${itemId}?quantity=${newQty}`);
        if (res && res.success && res.data) {
            renderCart(res.data);
            updateCartSummary(res.data);
            showToast('Cart updated', 'info');
        }
    } catch (err) {
        showToast(err.message || 'Failed to update item quantity', 'error');
    }
}

async function removeItem(itemId) {
    try {
        const res = await apiClient.delete(`/cart/items/${itemId}`);
        if (res && res.success && res.data) {
            renderCart(res.data);
            updateCartSummary(res.data);
            showToast('Item removed from cart', 'info');
        }
    } catch (err) {
        showToast(err.message || 'Failed to remove item', 'error');
    }
}

async function saveForLater(productId, cartItemId) {
    try {
        await apiClient.post(`/wishlist/${productId}`);
        await apiClient.delete(`/cart/items/${cartItemId}`);
        showToast('Moved to wishlist!', 'success');
        loadCart();
    } catch (err) {
        showToast(err.message || 'Failed to move to wishlist', 'error');
    }
}
