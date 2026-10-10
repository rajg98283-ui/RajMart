/**
 * RAJ MART - Authentication & Navigation State
 */

let currentUser = null;

async function checkAuthStatus() {
    try {
        const response = await apiClient.get('/auth/me');
        if (response && response.success && response.data) {
            currentUser = response.data;
            updateNavForUser(currentUser);
            if (currentUser.role === 'BUYER') {
                updateCartCount();
            }
            return currentUser;
        }
    } catch (e) {
        // Unauthenticated visitor
        currentUser = null;
        updateNavForGuest();
    }
    return null;
}

function updateNavForUser(user) {
    const userContainer = document.getElementById('nav-user-account');
    if (userContainer) {
        userContainer.innerHTML = `
            <a href="/profile.html" class="nav-item">
                <span>Hello, ${escapeHtml(user.name.split(' ')[0])}</span>
                <span class="nav-item-strong">Account & Lists <span class="role-badge badge-${user.role.toLowerCase()}">${user.role}</span></span>
            </a>
        `;
    }

    const subNavLinks = document.getElementById('nav-sub-links');
    if (subNavLinks) {
        let roleLinks = '';
        if (user.role === 'SELLER') {
            roleLinks = `<a href="/seller-dashboard.html">📦 Seller Dashboard</a>`;
        } else if (user.role === 'ADMIN') {
            roleLinks = `<a href="/admin-dashboard.html">🛡️ Admin Dashboard</a>`;
        } else if (user.role === 'BUYER') {
            roleLinks = `
                <a href="/orders.html">📦 My Orders</a>
                <a href="/wishlist.html">❤️ Wishlist</a>
            `;
        }

        subNavLinks.innerHTML = `
            <a href="/products.html">All Products</a>
            <a href="/products.html?category=Electronics">Electronics</a>
            <a href="/products.html?category=Fashion">Fashion</a>
            <a href="/products.html?category=Home%20%26%20Kitchen">Home & Kitchen</a>
            <a href="/products.html?category=Books">Books</a>
            <a href="/products.html?category=Sports">Sports</a>
            ${roleLinks}
            <a href="#" onclick="handleLogout(event)" style="margin-left: auto; color: #ff9999;">Sign Out</a>
        `;
    }
}

function updateNavForGuest() {
    const userContainer = document.getElementById('nav-user-account');
    if (userContainer) {
        userContainer.innerHTML = `
            <a href="/login.html" class="nav-item">
                <span>Hello, Sign in</span>
                <span class="nav-item-strong">Account & Lists</span>
            </a>
        `;
    }

    const subNavLinks = document.getElementById('nav-sub-links');
    if (subNavLinks) {
        subNavLinks.innerHTML = `
            <a href="/products.html">All Products</a>
            <a href="/products.html?category=Electronics">Electronics</a>
            <a href="/products.html?category=Fashion">Fashion</a>
            <a href="/products.html?category=Home%20%26%20Kitchen">Home & Kitchen</a>
            <a href="/products.html?category=Books">Books</a>
            <a href="/products.html?category=Sports">Sports</a>
            <a href="/login.html" style="margin-left: auto;">Sign In / Register</a>
        `;
    }
}

async function updateCartCount() {
    try {
        const response = await apiClient.get('/cart');
        if (response && response.success && response.data) {
            const countElem = document.getElementById('nav-cart-count');
            if (countElem) {
                countElem.textContent = response.data.itemCount || 0;
            }
        }
    } catch (e) {
        // Not buyer or cart unavailable
    }
}

async function handleLogout(e) {
    if (e) e.preventDefault();
    try {
        await apiClient.post('/auth/logout');
        showToast('Signed out successfully', 'success');
        setTimeout(() => {
            window.location.href = '/login.html';
        }, 500);
    } catch (err) {
        window.location.href = '/login.html';
    }
}

// Global quick search handler
function handleGlobalSearch(e) {
    e.preventDefault();
    const query = document.getElementById('global-search-input')?.value.trim();
    const cat = document.getElementById('global-search-category')?.value;
    let url = '/products.html?';
    if (query) url += `keyword=${encodeURIComponent(query)}&`;
    if (cat && cat !== 'all') url += `category=${encodeURIComponent(cat)}&`;
    window.location.href = url;
}

document.addEventListener('DOMContentLoaded', () => {
    checkAuthStatus();

    const searchForm = document.getElementById('global-search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', handleGlobalSearch);
    }
});
