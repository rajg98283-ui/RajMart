/**
 * RAJ MART - API Client & Core Utilities
 */

const API_BASE = '/api/v1';

const apiClient = {
    async request(endpoint, options = {}) {
        const url = endpoint.startsWith('http') || endpoint.startsWith('/api/')
            ? endpoint
            : `${API_BASE}${endpoint.startsWith('/') ? '' : '/'}${endpoint}`;

        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...options.headers
            }
        };

        try {
            const response = await fetch(url, config);
            const data = await response.json().catch(() => null);

            if (!response.ok) {
                const errorMessage = data?.error?.message || `Request failed with status ${response.status}`;
                const fieldErrors = data?.error?.fields || null;
                const err = new Error(errorMessage);
                err.status = response.status;
                err.fieldErrors = fieldErrors;
                err.response = data;
                throw err;
            }

            return data;
        } catch (error) {
            console.error(`API Error on [${options.method || 'GET'} ${url}]:`, error);
            throw error;
        }
    },

    get(endpoint, headers = {}) {
        return this.request(endpoint, { method: 'GET', headers });
    },

    post(endpoint, body = {}, headers = {}) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body),
            headers
        });
    },

    put(endpoint, body = {}, headers = {}) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body),
            headers
        });
    },

    delete(endpoint, headers = {}) {
        return this.request(endpoint, { method: 'DELETE', headers });
    }
};

// UI Toast Notification Helper
function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    let icon = 'ℹ️';
    if (type === 'success') icon = '✅';
    if (type === 'error') icon = '❌';

    toast.innerHTML = `<span>${icon}</span> <span>${escapeHtml(message)}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// XSS Prevention Helper
function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

// Currency Formatter
function formatPrice(amount) {
    if (amount === null || amount === undefined) return '$0.00';
    const num = Number(amount);
    return '$' + num.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// Star Rating Renderer
function renderStars(rating) {
    const r = Math.round(rating || 0);
    let stars = '';
    for (let i = 1; i <= 5; i++) {
        stars += i <= r ? '★' : '☆';
    }
    return `<span class="stars">${stars}</span>`;
}

// Universal Product Card Renderer
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
    if (typeof currentUser === 'undefined' || !currentUser) {
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
        if (typeof updateCartCount === 'function') {
            updateCartCount();
        }
    } catch (err) {
        showToast(err.message || 'Failed to add item to cart', 'error');
    }
}

async function quickAddToWishlist(productId) {
    if (typeof currentUser === 'undefined' || !currentUser) {
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

