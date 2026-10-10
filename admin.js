/**
 * RAJ MART - Admin Dashboard Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in as Admin.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }
    if (user.role !== 'ADMIN') {
        showToast('Access denied: Admin privileges required.', 'error');
        setTimeout(() => location.href = '/', 1200);
        return;
    }

    loadAdminStats();
    loadAdminUsers();
    loadAdminProducts();
    loadAdminOrders();
});

async function loadAdminStats() {
    try {
        const res = await apiClient.get('/admin/stats');
        if (res && res.success && res.data) {
            const s = res.data;
            document.getElementById('adm-users').textContent = s.totalUsers || 0;
            document.getElementById('adm-buyers').textContent = s.totalBuyers || 0;
            document.getElementById('adm-sellers').textContent = s.totalSellers || 0;
            document.getElementById('adm-products').textContent = s.totalProducts || 0;
            document.getElementById('adm-orders').textContent = s.totalOrders || 0;
            document.getElementById('adm-revenue').textContent = formatPrice(s.totalRevenue);
        }
    } catch (e) {
        console.error('Failed to load admin stats', e);
    }
}

async function loadAdminUsers() {
    const tbody = document.getElementById('admin-users-table');
    const roleFilter = document.getElementById('user-role-filter')?.value;
    let url = '/admin/users';
    if (roleFilter) url += `?role=${roleFilter}`;

    try {
        const res = await apiClient.get(url);
        if (res && res.success && res.data) {
            const users = res.data || [];
            tbody.innerHTML = users.map(u => `
                <tr>
                    <td><strong>#${u.id}</strong></td>
                    <td>${escapeHtml(u.name)}</td>
                    <td>${escapeHtml(u.email)}</td>
                    <td><span class="role-badge badge-${u.role.toLowerCase()}">${u.role}</span></td>
                    <td>${new Date(u.createdAt).toLocaleDateString()}</td>
                </tr>
            `).join('');
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="5" style="color: #b12704; text-align: center;">Error loading users.</td></tr>`;
    }
}

async function loadAdminProducts() {
    const tbody = document.getElementById('admin-products-table');
    try {
        const res = await apiClient.get('/products?page=0&size=50&sort=newest');
        if (res && res.success && res.data) {
            const products = res.data.products || [];
            tbody.innerHTML = products.map(p => `
                <tr>
                    <td>
                        <div style="width: 44px; height: 44px; background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; padding: 4px;">
                            <img src="${escapeHtml(p.imageUrl)}" style="max-width: 100%; max-height: 100%; object-fit: contain;" />
                        </div>
                    </td>
                    <td><a href="/product.html?id=${p.id}" target="_blank" style="font-weight: 700; color: var(--text-primary);">${escapeHtml(p.name)}</a></td>
                    <td><span class="role-badge" style="background: var(--surface); box-shadow: var(--neo-raised-sm); color: var(--primary);">${escapeHtml(p.category)}</span></td>
                    <td style="font-weight: 800; color: var(--primary);">${formatPrice(p.price)}</td>
                    <td><strong>${p.stockQuantity}</strong></td>
                    <td>${escapeHtml(p.sellerName || 'Seller #' + p.sellerId)}</td>
                    <td>
                        <button class="btn-danger" style="padding: 6px 14px; font-size: 12px;" onclick="moderateDelete(${p.id})">Remove Listing</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="color: var(--danger); text-align: center;">Error loading products.</td></tr>`;
    }
}

async function moderateDelete(productId) {
    if (!confirm('Are you sure you want to remove this product as Admin?')) return;
    try {
        await apiClient.delete(`/admin/products/${productId}`);
        showToast('Product removed by administrator', 'info');
        loadAdminProducts();
        loadAdminStats();
    } catch (err) {
        showToast(err.message || 'Failed to remove product', 'error');
    }
}

async function loadAdminOrders() {
    const tbody = document.getElementById('admin-orders-table');
    try {
        const res = await apiClient.get('/admin/orders');
        if (res && res.success && res.data) {
            const orders = res.data || [];
            tbody.innerHTML = orders.map(o => `
                <tr>
                    <td><strong>#${o.id}</strong></td>
                    <td>${escapeHtml(o.buyerName)}</td>
                    <td>${new Date(o.createdAt).toLocaleDateString()}</td>
                    <td style="font-weight: 800; color: var(--primary);">${formatPrice(o.totalAmount)}</td>
                    <td>${escapeHtml(o.paymentMethod)}</td>
                    <td><span class="status-badge status-${o.status}">${o.status}</span></td>
                    <td>
                        <select onchange="adminUpdateStatus(${o.id}, this.value)" class="form-control" style="width: auto; padding: 6px 12px; font-size: 12px;">
                            <option value="">Update Status...</option>
                            <option value="CONFIRMED">CONFIRMED</option>
                            <option value="PACKED">PACKED</option>
                            <option value="SHIPPED">SHIPPED</option>
                            <option value="DELIVERED">DELIVERED</option>
                            <option value="CANCELLED">CANCELLED</option>
                        </select>
                    </td>
                </tr>
            `).join('');
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="color: #b12704; text-align: center;">Error loading orders.</td></tr>`;
    }
}

async function adminUpdateStatus(orderId, newStatus) {
    if (!newStatus) return;
    try {
        await apiClient.put(`/admin/orders/${orderId}/status`, { status: newStatus, comments: 'Admin status override' });
        showToast(`Order #${orderId} updated to ${newStatus}`, 'success');
        loadAdminOrders();
    } catch (err) {
        showToast(err.message || 'Failed to update order status', 'error');
        loadAdminOrders();
    }
}
