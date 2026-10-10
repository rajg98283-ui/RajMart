/**
 * RAJ MART - Orders Management Logic
 */

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in to view your orders.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }

    loadOrders();
});

async function loadOrders() {
    const container = document.getElementById('orders-list-container');
    try {
        const res = await apiClient.get('/orders');
        if (res && res.success && res.data) {
            const orders = res.data || [];
            if (orders.length === 0) {
                container.innerHTML = `
                    <div class="card-box" style="text-align: center; padding: 60px 20px;">
                        <div style="font-size: 48px; margin-bottom: 12px;">📦</div>
                        <h2>No orders placed yet</h2>
                        <p style="color: #666; margin-top: 8px;">Explore our catalog and find something you love!</p>
                        <a href="/products.html" class="btn-primary" style="margin-top: 20px;">Start Shopping</a>
                    </div>
                `;
                return;
            }

            container.innerHTML = orders.map(o => createOrderCardHtml(o)).join('');
        }
    } catch (err) {
        container.innerHTML = `<div style="color: #b12704; padding: 20px;">Failed to load orders: ${escapeHtml(err.message)}</div>`;
    }
}

function createOrderCardHtml(o) {
    const canCancel = (o.status === 'PENDING' || o.status === 'CONFIRMED');
    const dateStr = o.createdAt ? new Date(o.createdAt).toLocaleDateString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
    }) : 'Recent';

    return `
        <div class="card-box" style="margin-bottom: 24px; padding: 0; overflow: hidden;">
            <!-- Header bar -->
            <div style="background: var(--surface); padding: 18px 24px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(210, 220, 235, 0.7); font-size: 13px;">
                <div style="display: flex; gap: 32px;">
                    <div>
                        <div style="color: var(--text-muted); text-transform: uppercase; font-size: 11px; font-weight: 700;">Order Placed</div>
                        <div style="font-weight: 700; color: var(--text-primary);">${dateStr}</div>
                    </div>
                    <div>
                        <div style="color: var(--text-muted); text-transform: uppercase; font-size: 11px; font-weight: 700;">Total</div>
                        <div style="font-weight: 800; color: var(--primary); font-size: 14.5px;">${formatPrice(o.totalAmount)}</div>
                    </div>
                    <div>
                        <div style="color: var(--text-muted); text-transform: uppercase; font-size: 11px; font-weight: 700;">Ship To</div>
                        <div style="font-weight: 700; max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; color: var(--text-primary);" title="${escapeHtml(o.shippingAddress)}">${escapeHtml(o.shippingAddress)}</div>
                    </div>
                </div>

                <div style="text-align: right; display: flex; align-items: center; gap: 14px;">
                    <div style="color: var(--text-muted); font-weight: 700; font-size: 12px;">ORDER #${o.id}</div>
                    <div><span class="status-badge status-${o.status}">${o.status}</span></div>
                </div>
            </div>

            <!-- Body -->
            <div style="padding: 24px;">
                <div style="font-size: 13px; color: var(--text-secondary); margin-bottom: 18px;">
                    Payment Method: <strong style="color: var(--text-primary);">${escapeHtml(o.paymentMethod || 'Mock UPI')}</strong>
                </div>

                <div style="display: flex; flex-direction: column; gap: 16px;">
                    ${o.items.map(i => `
                        <div style="display: flex; gap: 16px; align-items: center; border-bottom: 1px solid rgba(210, 220, 235, 0.5); padding-bottom: 16px;">
                            <a href="/product.html?id=${i.productId}" style="width: 72px; height: 72px; display: flex; align-items: center; justify-content: center; background: var(--surface); box-shadow: var(--neo-inset-sm); border: var(--neo-border); border-radius: var(--radius-sm); padding: 6px;">
                                <img src="${escapeHtml(i.productImageUrl)}" alt="${escapeHtml(i.productName)}" style="max-width: 100%; max-height: 100%; object-fit: contain;" />
                            </a>
                            <div style="flex: 1;">
                                <a href="/product.html?id=${i.productId}" style="font-size: 15px; font-weight: 700; color: var(--text-primary);">
                                    ${escapeHtml(i.productName)}
                                </a>
                                <div style="font-size: 13px; color: var(--text-muted); margin-top: 4px;">
                                    Qty: ${i.quantity} × ${formatPrice(i.price)}
                                </div>
                            </div>
                            <div style="display: flex; flex-direction: column; gap: 8px;">
                                <a href="/product.html?id=${i.productId}" class="btn-secondary" style="font-size: 12px; padding: 6px 14px; text-align: center;">Write a review</a>
                                <button class="btn-secondary" style="font-size: 12px; padding: 6px 14px;" onclick="quickAddToCart(${i.productId})">Buy it again</button>
                            </div>
                        </div>
                    `).join('')}
                </div>

                ${canCancel ? `
                    <div style="margin-top: 20px; padding-top: 16px; border-top: 1px solid rgba(210, 220, 235, 0.6); text-align: right;">
                        <button class="btn-danger" onclick="cancelOrder(${o.id})">Cancel Order</button>
                    </div>
                ` : ''}
            </div>
        </div>
    `;
}

async function cancelOrder(orderId) {
    if (!confirm('Are you sure you want to cancel this order? Any deducted inventory will be restored.')) {
        return;
    }

    try {
        const res = await apiClient.put(`/orders/${orderId}/cancel?reason=Cancelled by buyer`);
        if (res && res.success) {
            showToast('Order cancelled successfully', 'info');
            loadOrders();
        }
    } catch (err) {
        showToast(err.message || 'Failed to cancel order', 'error');
    }
}
