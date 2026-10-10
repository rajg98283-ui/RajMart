/**
 * RAJ MART - Product Details & Reviews Logic
 */

let currentProductId = null;
let currentProductData = null;

document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    currentProductId = params.get('id');

    if (!currentProductId) {
        document.getElementById('product-details-container').innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 40px;">
                <h2>Product Not Specified</h2>
                <a href="/products.html" class="btn-primary" style="margin-top: 16px;">Browse All Products</a>
            </div>
        `;
        return;
    }

    loadProductDetails();
    loadProductReviews();

    const reviewForm = document.getElementById('add-review-form');
    if (reviewForm) {
        reviewForm.addEventListener('submit', handleReviewSubmit);
    }
});

async function loadProductDetails() {
    const container = document.getElementById('product-details-container');
    try {
        const res = await apiClient.get(`/products/${currentProductId}`);
        if (res && res.success && res.data) {
            currentProductData = res.data;
            document.title = `${currentProductData.name} - RajMart`;
            renderProductDetails(currentProductData);
        }
    } catch (err) {
        container.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #b12704;">
                <h2>Product Not Found</h2>
                <p>${escapeHtml(err.message)}</p>
                <a href="/products.html" class="btn-primary" style="margin-top: 16px;">Return to Shop</a>
            </div>
        `;
    }
}

function renderProductDetails(p) {
    const container = document.getElementById('product-details-container');
    const isAvailable = p.stockQuantity > 0;
    const stockClass = p.stockQuantity > 5 ? 'in-stock' : (p.stockQuantity > 0 ? 'low-stock' : 'out-stock');
    const stockText = p.stockQuantity > 5 ? 'In Stock' : (p.stockQuantity > 0 ? `Only ${p.stockQuantity} left in stock - order soon` : 'Currently Unavailable');

    container.innerHTML = `
        <!-- Column 1: Image Showcase (Recessed Neomorphic Frame) -->
        <div style="display: flex; justify-content: center; align-items: flex-start;">
            <div style="max-width: 380px; width: 100%; border-radius: var(--radius-lg); background: var(--surface); box-shadow: var(--neo-inset); border: var(--neo-border); padding: 24px; display: flex; align-items: center; justify-content: center;">
                <img src="${escapeHtml(p.imageUrl)}" alt="${escapeHtml(p.name)}" style="max-width: 100%; max-height: 360px; object-fit: contain; filter: drop-shadow(0 4px 10px rgba(0,0,0,0.06));" />
            </div>
        </div>

        <!-- Column 2: Info & Specs -->
        <div>
            <div style="font-size: 12.5px; color: var(--primary); font-weight: 800; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px;">
                Department: <a href="/products.html?category=${encodeURIComponent(p.category)}">${escapeHtml(p.category)}</a>
            </div>
            <h1 style="font-size: 26px; font-weight: 800; line-height: 1.3; margin-bottom: 10px; color: var(--text-primary);">${escapeHtml(p.name)}</h1>
            <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 18px; font-size: 14px;">
                ${renderStars(p.averageRating)}
                <span style="color: var(--primary); font-weight: 700;">${p.averageRating > 0 ? p.averageRating.toFixed(1) : 'No ratings yet'}</span>
                <span style="color: var(--text-muted);">(${p.reviewCount || 0} verified reviews)</span>
            </div>

            <div style="background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-md); padding: 18px 20px; margin-bottom: 22px;">
                <div style="font-size: 30px; font-weight: 800; color: var(--text-primary); letter-spacing: -0.5px;">
                    ${formatPrice(p.price)}
                </div>
                <div style="font-size: 12.5px; color: var(--text-secondary); margin-top: 4px;">
                    Inclusive of all mock taxes. Free Delivery on orders over $50.
                </div>
            </div>

            <div>
                <h3 style="font-size: 16px; font-weight: 800; margin-bottom: 10px; color: var(--text-primary);">About this item</h3>
                <p style="font-size: 14.5px; line-height: 1.65; color: var(--text-secondary); white-space: pre-line;">${escapeHtml(p.description)}</p>
            </div>
        </div>

        <!-- Column 3: Buy Box (Extruded Neomorphic Card) -->
        <div style="background: var(--surface); border-radius: var(--radius-lg); padding: 24px; box-shadow: var(--neo-raised); border: var(--neo-border); display: flex; flex-direction: column; gap: 16px;">
            <div style="font-size: 24px; font-weight: 800; color: var(--text-primary);">
                ${formatPrice(p.price)}
            </div>

            <div>
                <span class="stock-tag ${stockClass}" style="font-size: 13px; padding: 4px 10px;">
                    ${stockText}
                </span>
            </div>

            <div style="font-size: 13px; color: var(--text-secondary);">
                Sold by: <strong>${escapeHtml(p.sellerName || 'Verified Merchant')}</strong>
            </div>

            ${isAvailable ? `
                <div class="form-group" style="margin-bottom: 6px;">
                    <label class="form-label" style="font-size: 13px;">Quantity:</label>
                    <select id="buy-box-qty" class="form-control" style="padding: 10px 14px;">
                        ${Array.from({ length: Math.min(p.stockQuantity, 10) }, (_, i) => `<option value="${i + 1}">${i + 1}</option>`).join('')}
                    </select>
                </div>

                <button class="btn-primary" style="width: 100%; justify-content: center;" onclick="handleBuyBoxAddToCart()">
                    Add to Cart 🛒
                </button>
            ` : `
                <button class="btn-primary" disabled style="width: 100%; justify-content: center; opacity: 0.6; cursor: not-allowed;">
                    Out of Stock
                </button>
            `}

            <button class="btn-secondary" style="width: 100%; justify-content: center;" onclick="quickAddToWishlist(${p.id})">
                ❤️ Save to Wishlist
            </button>
        </div>
    `;
}

async function handleBuyBoxAddToCart() {
    if (!currentUser) {
        showToast('Please sign in as a buyer to purchase items.', 'error');
        setTimeout(() => location.href = '/login.html', 1200);
        return;
    }
    if (currentUser.role !== 'BUYER') {
        showToast('Only buyer accounts can add items to cart.', 'error');
        return;
    }

    const qty = parseInt(document.getElementById('buy-box-qty')?.value || '1', 10);
    try {
        await apiClient.post('/cart/items', { productId: currentProductId, quantity: qty });
        showToast(`Added ${qty} item(s) to cart!`, 'success');
        updateCartCount();
    } catch (err) {
        showToast(err.message || 'Failed to add item to cart', 'error');
    }
}

async function loadProductReviews() {
    const listContainer = document.getElementById('reviews-list');
    const summaryContainer = document.getElementById('reviews-summary');
    if (!listContainer) return;

    try {
        const res = await apiClient.get(`/reviews/product/${currentProductId}`);
        if (res && res.success && res.data) {
            const reviews = res.data || [];

            if (reviews.length === 0) {
                listContainer.innerHTML = `<p style="color: #666; font-style: italic;">No reviews yet. Be the first verified buyer to leave feedback!</p>`;
                if (summaryContainer) {
                    summaryContainer.innerHTML = `<span>No reviews yet</span>`;
                }
                return;
            }

            const totalRating = reviews.reduce((sum, r) => sum + r.rating, 0);
            const avg = (totalRating / reviews.length).toFixed(1);

            if (summaryContainer) {
                summaryContainer.innerHTML = `
                    ${renderStars(Number(avg))}
                    <span style="font-size: 16px; font-weight: 700;">${avg} out of 5</span>
                    <span style="color: #555;">(${reviews.length} customer ratings)</span>
                `;
            }

            listContainer.innerHTML = reviews.map(r => `
                <div style="background: var(--surface); box-shadow: var(--neo-raised-sm); border: var(--neo-border); border-radius: var(--radius-md); padding: 18px 20px;">
                    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px;">
                        <span style="font-weight: 700; font-size: 14px; color: var(--text-primary);">👤 ${escapeHtml(r.buyerName)}</span>
                        <span style="color: var(--success); font-size: 11px; font-weight: 800; background: #ecfdf5; padding: 2px 8px; border-radius: var(--radius-pill); box-shadow: var(--neo-raised-sm);">✓ Verified Buyer</span>
                    </div>
                    <div style="display: flex; align-items: center; gap: 6px; margin-bottom: 8px;">
                        ${renderStars(r.rating)}
                    </div>
                    <p style="font-size: 14px; color: var(--text-secondary); line-height: 1.5;">${escapeHtml(r.comment)}</p>
                </div>
            `).join('');
        }
    } catch (err) {
        listContainer.innerHTML = `<p style="color: #b12704;">Error loading reviews.</p>`;
    }
}

async function handleReviewSubmit(e) {
    e.preventDefault();
    if (!currentUser) {
        showToast('Please sign in as a buyer to write a review.', 'error');
        setTimeout(() => location.href = '/login.html', 1200);
        return;
    }
    if (currentUser.role !== 'BUYER') {
        showToast('Only buyer accounts can leave customer reviews.', 'error');
        return;
    }

    const rating = parseInt(document.getElementById('review-rating').value, 10);
    const comment = document.getElementById('review-comment').value.trim();

    try {
        await apiClient.post(`/reviews?productId=${currentProductId}`, { rating, comment });
        showToast('Review submitted successfully! Thank you.', 'success');
        document.getElementById('review-comment').value = '';
        loadProductReviews();
        loadProductDetails();
    } catch (err) {
        showToast(err.message || 'Failed to submit review', 'error');
    }
}
