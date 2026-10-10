/**
 * RAJ MART - Homepage Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
    loadFeaturedProducts();
});

async function loadFeaturedProducts() {
    const grid = document.getElementById('featured-products-grid');
    if (!grid) return;

    try {
        const response = await apiClient.get('/products?page=0&size=8&sort=newest');
        if (response && response.success && response.data) {
            const products = response.data.products || [];
            if (products.length === 0) {
                grid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 40px;">No products available right now.</div>`;
                return;
            }

            grid.innerHTML = products.map(product => createProductCardHtml(product)).join('');
        }
    } catch (error) {
        grid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #b12704;">Failed to load products. Please refresh.</div>`;
    }
}

function createProductCardHtml(p) {
    const stockClass = p.stockQuantity > 5 ? 'in-stock' : (p.stockQuantity > 0 ? 'low-stock' : 'out-stock');
    const stockText = p.stockQuantity > 5 ? 'In Stock' : (p.stockQuantity > 0 ? `Only ${p.stockQuantity} left` : 'Out of Stock');
    const isAvailable = p.stockQuantity > 0;

    return `
        <div class="product-card">
            <a href="/product.html?id=${p.id}" class="product-img-wrap">
                <img src="${escapeHtml(p.imageUrl)}" alt="${escapeHtml(p.name)}" loading="lazy" />
            </a>
            <div class="product-category">${escapeHtml(p.category)}</div>
            <a href="/product.html?id=${p.id}" class="product-title">${escapeHtml(p.name)}</a>
            
            <div class="product-rating">
                ${renderStars(p.averageRating)}
                <span class="rating-count">(${p.reviewCount || 0})</span>
            </div>

            <div class="product-price-row">
                <span class="product-price">${formatPrice(p.price)}</span>
                <span class="stock-tag ${stockClass}">${stockText}</span>
            </div>

            <div class="card-actions">
                ${isAvailable ? `
                    <button class="btn-add-cart" onclick="quickAddToCart(${p.id})">Add to Cart</button>
                ` : `
                    <button class="btn-add-cart" disabled style="opacity: 0.6; cursor: not-allowed;">Out of Stock</button>
                `}
                <button class="btn-wishlist" onclick="quickAddToWishlist(${p.id})" title="Save to Wishlist">❤️</button>
            </div>
        </div>
    `;
}

async function quickAddToCart(productId) {
    if (!currentUser) {
        showToast('Please sign in as a buyer to add items to cart.', 'error');
        setTimeout(() => location.href = '/login.html', 1200);
        return;
    }
    if (currentUser.role !== 'BUYER') {
        showToast('Only buyer accounts can add items to cart.', 'error');
        return;
    }

    try {
        await apiClient.post('/cart/items', { productId, quantity: 1 });
        showToast('Item added to cart!', 'success');
        updateCartCount();
    } catch (err) {
        showToast(err.message || 'Failed to add item to cart', 'error');
    }
}

async function quickAddToWishlist(productId) {
    if (!currentUser) {
        showToast('Please sign in to save items to your wishlist.', 'error');
        setTimeout(() => location.href = '/login.html', 1200);
        return;
    }
    if (currentUser.role !== 'BUYER') {
        showToast('Only buyer accounts can use the wishlist.', 'error');
        return;
    }

    try {
        await apiClient.post(`/wishlist/${productId}`);
        showToast('Added to Wishlist!', 'success');
    } catch (err) {
        showToast(err.message || 'Item already in wishlist', 'info');
    }
}
