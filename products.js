/**
 * RAJ MART - Product Catalog Logic
 */

let currentPage = 0;
const pageSize = 9;

document.addEventListener('DOMContentLoaded', () => {
    // Parse URL params
    const params = new URLSearchParams(window.location.search);
    const cat = params.get('category');
    const keyword = params.get('keyword');

    if (cat) {
        const radio = document.querySelector(`input[name="cat-filter"][value="${cat}"]`);
        if (radio) radio.checked = true;
    }
    if (keyword) {
        const input = document.getElementById('global-search-input');
        if (input) input.value = keyword;
    }

    loadProducts();
});

function applyFilters() {
    currentPage = 0;
    loadProducts();
}

async function loadProducts() {
    const grid = document.getElementById('catalog-products-grid');
    const countText = document.getElementById('results-count-text');
    if (!grid) return;

    const selectedCat = document.querySelector('input[name="cat-filter"]:checked')?.value || 'all';
    const sort = document.getElementById('sort-select')?.value || 'newest';
    const params = new URLSearchParams(window.location.search);
    const keyword = params.get('keyword') || document.getElementById('global-search-input')?.value.trim() || '';

    grid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #666;">Loading products...</div>`;

    let url = `/products/search?page=${currentPage}&size=${pageSize}&sort=${sort}`;
    if (selectedCat && selectedCat !== 'all') {
        url += `&category=${encodeURIComponent(selectedCat)}`;
    }
    if (keyword) {
        url += `&keyword=${encodeURIComponent(keyword)}`;
    }

    try {
        const res = await apiClient.get(url);
        if (res && res.success && res.data) {
            let products = res.data.products || [];
            const includeOutOfStock = document.getElementById('filter-in-stock')?.checked;

            if (!includeOutOfStock) {
                products = products.filter(p => p.stockQuantity > 0);
            }

            const total = res.data.total || 0;
            const totalPages = res.data.totalPages || 1;

            if (countText) {
                countText.textContent = `Showing ${products.length} of ${total} products`;
            }

            if (products.length === 0) {
                grid.innerHTML = `
                    <div style="grid-column: 1/-1; text-align: center; padding: 60px 20px; background: #fff; border-radius: 8px;">
                        <h3 style="margin-bottom: 8px;">No products found</h3>
                        <p style="color: #666;">Try adjusting your search terms or filters.</p>
                    </div>
                `;
                renderPagination(0, 0);
                return;
            }

            grid.innerHTML = products.map(p => createProductCardHtml(p)).join('');
            renderPagination(currentPage, totalPages);
        }
    } catch (err) {
        grid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #b12704;">Error loading products: ${escapeHtml(err.message)}</div>`;
    }
}

function renderPagination(current, totalPages) {
    const container = document.getElementById('pagination-controls');
    if (!container) return;

    if (totalPages <= 1) {
        container.innerHTML = '';
        return;
    }

    let html = '';
    if (current > 0) {
        html += `<button class="btn-secondary" onclick="changePage(${current - 1})">« Previous</button>`;
    }
    html += `<span style="padding: 10px 14px; font-weight: 700; font-size: 14px;">Page ${current + 1} of ${totalPages}</span>`;
    if (current < totalPages - 1) {
        html += `<button class="btn-secondary" onclick="changePage(${current + 1})">Next »</button>`;
    }

    container.innerHTML = html;
}

function changePage(page) {
    currentPage = page;
    loadProducts();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}
