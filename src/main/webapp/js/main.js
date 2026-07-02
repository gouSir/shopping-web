/**
 * 简易购物网站 - 全局 JavaScript
 */

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {

    // ========== 搜索框：回车提交 ==========
    var searchForm = document.querySelector('.search-form');
    var searchInput = document.querySelector('.search-form input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchForm.submit();
            }
        });
    }

    // ========== 消息提示自动消失 ==========
    var msg = document.querySelector('.msg-success, .msg-error');
    if (msg) {
        setTimeout(function() {
            msg.style.transition = 'opacity 0.5s';
            msg.style.opacity = '0';
            setTimeout(function() { msg.style.display = 'none'; }, 500);
        }, 3000);
    }

    // ========== 购物车数量加减 ==========
    var qtyInputs = document.querySelectorAll('.qty-form input[type="number"]');
    qtyInputs.forEach(function(input) {
        input.addEventListener('change', function() {
            var val = parseInt(this.value);
            if (isNaN(val) || val < 1) this.value = 1;
            if (val > 99) this.value = 99;
        });
    });
});

/**
 * AJAX 添加到购物车（可选的增强功能）
 * @param {number} productId - 商品ID
 */
function addToCartAjax(productId) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/ShoppingWeb/cart', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            try {
                var data = JSON.parse(xhr.responseText);
                if (data.success) {
                    alert('已加入购物车！');
                    // 更新购物车角标
                    var badge = document.querySelector('.cart-badge');
                    if (badge) {
                        badge.textContent = data.count;
                        badge.style.display = data.count > 0 ? 'flex' : 'none';
                    } else if (data.count > 0) {
                        var cartLink = document.querySelector('.cart-link');
                        if (cartLink) {
                            var span = document.createElement('span');
                            span.className = 'cart-badge';
                            span.textContent = data.count;
                            cartLink.appendChild(span);
                        }
                    }
                }
            } catch (e) {
                console.error('解析响应失败', e);
            }
        }
    };
    xhr.send('action=add&productId=' + productId + '&quantity=1&ajax=true');
}
