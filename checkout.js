/**
 * RAJ MART - Checkout Flow Logic
 */

let cartData = null;

document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuthStatus();
    if (!user) {
        showToast('Please sign in to complete checkout.', 'error');
        setTimeout(() => location.href = '/login.html', 1000);
        return;
    }
    if (user.role !== 'BUYER') {
        showToast('Only buyer accounts can check out.', 'error');
        location.href = '/';
        return;
    }

    // Auto-fill buyer name
    document.getElementById('ship-name').value = user.name || '';

    loadCheckoutCart();

    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', handleCheckoutSubmit);
    }
});

async function loadCheckoutCart() {
    try {
        const res = await apiClient.get('/cart');
        if (res && res.success && res.data) {
            cartData = res.data;
            if (!cartData.items || cartData.items.length === 0) {
                showToast('Your cart is empty. Add items before checking out.', 'error');
                setTimeout(() => location.href = '/cart.html', 1200);
                return;
            }

            renderCheckoutSummary(cartData);
        }
    } catch (err) {
        showToast('Failed to load cart summary: ' + err.message, 'error');
    }
}

function renderCheckoutSummary(cart) {
    const preview = document.getElementById('checkout-items-preview');
    preview.innerHTML = cart.items.map(item => `
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; font-size: 13px;">
            <div style="display: flex; align-items: center; gap: 8px;">
                <img src="${escapeHtml(item.productImageUrl)}" style="width: 32px; height: 32px; object-fit: contain; border-radius: 4px;" />
                <div>
                    <strong>${escapeHtml(item.productName)}</strong>
                    <div style="color: #666;">Qty: ${item.quantity} × ${formatPrice(item.productPrice)}</div>
                </div>
            </div>
            <strong>${formatPrice(item.subtotal)}</strong>
        </div>
    `).join('');

    document.getElementById('co-subtotal').textContent = formatPrice(cart.subtotal);
    document.getElementById('co-tax').textContent = formatPrice(cart.tax);
    document.getElementById('co-shipping').textContent = cart.shipping > 0 ? formatPrice(cart.shipping) : 'FREE';
    document.getElementById('co-total').textContent = formatPrice(cart.total);
}

async function handleCheckoutSubmit(e) {
    e.preventDefault();

    const name = document.getElementById('ship-name').value.trim();
    const phone = document.getElementById('ship-phone').value.trim();
    const street = document.getElementById('ship-street').value.trim();
    const city = document.getElementById('ship-city').value.trim();
    const state = document.getElementById('ship-state').value.trim();
    const zip = document.getElementById('ship-zip').value.trim();
    const paymentMethod = document.querySelector('input[name="payment-method"]:checked')?.value || 'Mock UPI';

    const fullAddress = `${name}, ${street}, ${city}, ${state} - ${zip}, Phone: ${phone}`;

    // Open mock payment confirmation modal
    const modal = document.getElementById('payment-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');
    const spinner = document.getElementById('modal-spinner');

    modal.classList.add('active');
    modalTitle.textContent = `Processing Mock ${paymentMethod}...`;
    modalMessage.textContent = 'Simulating secure payment gateway confirmation...';
    spinner.textContent = '⏳';

    try {
        // 1 second realistic gateway simulation delay
        await new Promise(r => setTimeout(r, 1000));

        const res = await apiClient.post('/orders', {
            shippingAddress: fullAddress,
            paymentMethod: paymentMethod,
            paymentDetails: 'SIMULATED-SUCCESS-TXN'
        });

        if (res && res.success && res.data) {
            spinner.textContent = '✅';
            modalTitle.textContent = 'Payment Authorized & Order Confirmed!';
            modalMessage.textContent = `Order #${res.data.id} has been placed successfully. Redirecting to your orders...`;

            setTimeout(() => {
                location.href = '/orders.html';
            }, 1500);
        }
    } catch (err) {
        spinner.textContent = '❌';
        modalTitle.textContent = 'Checkout Failed';
        modalMessage.textContent = err.message || 'Payment simulation failed. Please try again.';
        setTimeout(() => {
            modal.classList.remove('active');
            showToast(err.message || 'Checkout failed', 'error');
        }, 2000);
    }
}
