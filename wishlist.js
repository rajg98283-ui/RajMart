/**
 * RAJ MART - Wishlist Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in to view your wishlist.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }
    loadWishlist();
});

async function loadWishlist() {
    const container = document.getElementById('wishlist-container');
    const countText = document.getElementById('wishlist-count-text');

    try {
        const res = await apiClient.get('/wishlist');
        if (res && res.success && res.data) {
            const items = res.data || [];
            countText.textContent = `${items.length} item(s)`;

            if (items.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 40px;">
                        <div style="font-size: 40px; margin-bottom: 10px;">❤️</div>
                        <h3>Your Wishlist is empty</h3>
                        <p style="color: #666; margin-top: 6px;">Save items you love to revisit or purchase later!</p>
                        <a href="/products.html" class="btn-primary" style="margin-top: 16px;">Explore Catalog</a>
                    </div>
                `;
                return;
            }

            container.innerHTML = items.map(item => `
                <div style="display: flex; gap: 20px; padding: 18px 20px; background: var(--surface); box-shadow: var(--neo-raised-sm); border: var(--neo-border); border-radius: var(--radius-md); margin-bottom: 16px; align-items: center;">
                    <a href="/product.html?id=${item.productId}" style="width: 80px; height: 80px; display: flex; align-items: center; justify-content: center; background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-sm); padding: 8px;">
                        <img src="${escapeHtml(item.productImageUrl)}" alt="${escapeHtml(item.productName)}" style="max-width: 100%; max-height: 100%; object-fit: contain;" />
                    </a>

                    <div style="flex: 1;">
                        <a href="/product.html?id=${item.productId}" style="font-size: 15.5px; font-weight: 700; color: var(--text-primary);">
                            ${escapeHtml(item.productName)}
                        </a>
                        <div style="font-size: 12px; color: var(--text-muted); margin-top: 4px;">
                            ${escapeHtml(item.category)} • <span class="stock-tag ${item.stockQuantity > 0 ? 'in-stock' : 'out-stock'}">${item.stockQuantity > 0 ? 'In Stock' : 'Out of Stock'}</span>
                        </div>
                        <div style="font-size: 18px; font-weight: 800; color: var(--primary); margin-top: 6px;">
                            ${formatPrice(item.productPrice)}
                        </div>
                    </div>

                    <div style="display: flex; gap: 10px;">
                        <button class="btn-add-cart" style="padding: 8px 18px; font-size: 13px;" onclick="moveToCart(${item.productId})">Move to Cart</button>
                        <button class="btn-secondary" style="padding: 8px 14px; font-size: 13px;" onclick="removeFromWishlist(${item.productId})">Remove</button>
                    </div>
                </div>
            `).join('');
        }
    } catch (err) {
        container.innerHTML = `<div style="color: #b12704;">Error loading wishlist: ${escapeHtml(err.message)}</div>`;
    }
}

async function removeFromWishlist(productId) {
    try {
        await apiClient.delete(`/wishlist/${productId}`);
        showToast('Removed from wishlist', 'info');
        loadWishlist();
    } catch (err) {
        showToast(err.message || 'Failed to remove item', 'error');
    }
}

async function moveToCart(productId) {
    try {
        await apiClient.post(`/wishlist/${productId}/move-to-cart`);
        showToast('Moved item to Cart!', 'success');
        updateCartCount();
        loadWishlist();
    } catch (err) {
        showToast(err.message || 'Failed to move to cart', 'error');
    }
}
