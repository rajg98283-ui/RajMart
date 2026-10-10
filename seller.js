/**
 * RAJ MART - Seller Dashboard Logic
 */

let sellerProducts = [];

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in to access Seller Central.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }
    if (user.role !== 'SELLER' && user.role !== 'ADMIN') {
        showToast('Access denied: Seller account required.', 'error');
        setTimeout(() => location.href = '/', 1200);
        return;
    }

    loadSellerStats();
    loadSellerProducts();
    loadIncomingOrders();

    const form = document.getElementById('product-form');
    if (form) {
        form.addEventListener('submit', handleProductSubmit);
    }
});

async function loadSellerStats() {
    try {
        const res = await apiClient.get('/seller/stats');
        if (res && res.success && res.data) {
            const s = res.data;
            document.getElementById('metric-products').textContent = s.totalProducts || 0;
            document.getElementById('metric-sold').textContent = s.totalItemsSold || 0;
            document.getElementById('metric-revenue').textContent = formatPrice(s.totalRevenue);
            document.getElementById('metric-orders').textContent = s.totalOrders || 0;
        }
    } catch (e) {
        console.error('Failed to load seller stats', e);
    }
}

async function loadSellerProducts() {
    const tbody = document.getElementById('seller-products-table');
    try {
        const res = await apiClient.get('/seller/products');
        if (res && res.success && res.data) {
            sellerProducts = res.data || [];
            if (sellerProducts.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; padding: 40px;">No products listed yet. Click "+ Add New Product" to start selling!</td></tr>`;
                return;
            }

            tbody.innerHTML = sellerProducts.map(p => `
                <tr>
                    <td>
                        <div style="width: 48px; height: 48px; background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; padding: 4px;">
                            <img src="${escapeHtml(p.imageUrl)}" style="max-width: 100%; max-height: 100%; object-fit: contain;" />
                        </div>
                    </td>
                    <td>
                        <a href="/product.html?id=${p.id}" target="_blank" style="font-weight: 700; color: var(--text-primary);">${escapeHtml(p.name)}</a>
                        <div style="font-size: 11px; color: var(--text-muted); margin-top: 2px;">ID: #${p.id}</div>
                    </td>
                    <td><span class="role-badge" style="background: var(--surface); box-shadow: var(--neo-raised-sm); color: var(--primary);">${escapeHtml(p.category)}</span></td>
                    <td style="font-weight: 800; color: var(--primary);">${formatPrice(p.price)}</td>
                    <td>
                        <span class="stock-tag ${p.stockQuantity > 5 ? 'in-stock' : (p.stockQuantity > 0 ? 'low-stock' : 'out-stock')}">
                            ${p.stockQuantity} units
                        </span>
                    </td>
                    <td>${renderStars(p.averageRating)} <span style="font-size: 12px; color: var(--text-muted);">(${p.reviewCount || 0})</span></td>
                    <td>
                        <div style="display: flex; gap: 6px;">
                            <button class="btn-secondary" style="padding: 6px 12px; font-size: 12px;" onclick="openEditModal(${p.id})">Edit</button>
                            <button class="btn-danger" style="padding: 6px 12px; font-size: 12px;" onclick="deleteProduct(${p.id})">Delete</button>
                        </div>
                    </td>
                </tr>
            `).join('');
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="color: var(--danger); text-align: center;">Error loading products: ${escapeHtml(err.message)}</td></tr>`;
    }
}

async function loadIncomingOrders() {
    const tbody = document.getElementById('seller-orders-table');
    try {
        const res = await apiClient.get('/seller/orders');
        if (res && res.success && res.data) {
            const orders = res.data || [];
            if (orders.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; padding: 40px; color: var(--text-muted);">No incoming orders yet.</td></tr>`;
                return;
            }

            tbody.innerHTML = orders.map(o => `
                <tr>
                    <td><strong>#${o.id}</strong></td>
                    <td>${escapeHtml(o.buyerName)}<br/><small style="color: var(--text-muted);">${escapeHtml(o.buyerEmail)}</small></td>
                    <td>${new Date(o.createdAt).toLocaleDateString()}</td>
                    <td style="font-weight: 800; color: var(--primary);">${formatPrice(o.totalAmount)}</td>
                    <td style="max-width: 200px; font-size: 12px; color: var(--text-secondary);">${escapeHtml(o.shippingAddress)}</td>
                    <td><span class="status-badge status-${o.status}">${o.status}</span></td>
                    <td>
                        <select onchange="updateOrderStatus(${o.id}, this.value)" class="form-control" style="width: auto; padding: 6px 12px; font-size: 12px;">
                            <option value="">Change Status...</option>
                            <option value="CONFIRMED" ${o.status === 'CONFIRMED' ? 'disabled' : ''}>CONFIRMED</option>
                            <option value="PACKED" ${o.status === 'PACKED' ? 'disabled' : ''}>PACKED</option>
                            <option value="SHIPPED" ${o.status === 'SHIPPED' ? 'disabled' : ''}>SHIPPED</option>
                            <option value="DELIVERED" ${o.status === 'DELIVERED' ? 'disabled' : ''}>DELIVERED</option>
                            <option value="CANCELLED" ${o.status === 'CANCELLED' ? 'disabled' : ''}>CANCELLED</option>
                        </select>
                    </td>
                </tr>
            `).join('');
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="color: #b12704; text-align: center;">Error loading orders.</td></tr>`;
    }
}

function openProductModal() {
    document.getElementById('modal-product-title').textContent = 'Add New Product';
    document.getElementById('form-product-id').value = '';
    document.getElementById('product-form').reset();
    document.getElementById('product-modal').classList.add('active');
}

function openEditModal(productId) {
    const p = sellerProducts.find(item => item.id === productId);
    if (!p) return;

    document.getElementById('modal-product-title').textContent = 'Edit Product Listing';
    document.getElementById('form-product-id').value = p.id;
    document.getElementById('p-name').value = p.name;
    document.getElementById('p-category').value = p.category;
    document.getElementById('p-price').value = p.price;
    document.getElementById('p-stock').value = p.stockQuantity;
    document.getElementById('p-image').value = p.imageUrl;
    document.getElementById('p-desc').value = p.description;

    document.getElementById('product-modal').classList.add('active');
}

function closeProductModal() {
    document.getElementById('product-modal').classList.remove('active');
}

async function handleProductSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('form-product-id').value;
    const payload = {
        name: document.getElementById('p-name').value.trim(),
        category: document.getElementById('p-category').value,
        price: parseFloat(document.getElementById('p-price').value),
        stockQuantity: parseInt(document.getElementById('p-stock').value, 10),
        imageUrl: document.getElementById('p-image').value.trim(),
        description: document.getElementById('p-desc').value.trim()
    };

    try {
        if (id) {
            await apiClient.put(`/seller/products/${id}`, payload);
            showToast('Product updated successfully', 'success');
        } else {
            await apiClient.post('/seller/products', payload);
            showToast('Product created successfully', 'success');
        }
        closeProductModal();
        loadSellerProducts();
        loadSellerStats();
    } catch (err) {
        showToast(err.message || 'Operation failed', 'error');
    }
}

async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;
    try {
        await apiClient.delete(`/seller/products/${productId}`);
        showToast('Product deleted', 'info');
        loadSellerProducts();
        loadSellerStats();
    } catch (err) {
        showToast(err.message || 'Failed to delete product', 'error');
    }
}

async function updateOrderStatus(orderId, newStatus) {
    if (!newStatus) return;
    try {
        await apiClient.put(`/seller/orders/${orderId}/status`, { status: newStatus, comments: 'Status updated by seller' });
        showToast(`Order status updated to ${newStatus}`, 'success');
        loadIncomingOrders();
        loadSellerStats();
    } catch (err) {
        showToast(err.message || 'Failed to update order status', 'error');
        loadIncomingOrders();
    }
}
