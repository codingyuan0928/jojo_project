export async function fetchShoppingCartList(userId) {
    const url = `${port}/api/cart/view?userId=${userId}`;
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;  // 可以根據需求決定是否需要 rethrow error
    }
}


export function addProduct(userId,productId,quantity){
    const url = `/api/cart/add?userId=${userId}&productId=${productId}&quantity=${quantity}`;
    return fetch(url, {
        method: 'POST',
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        });
}

export function deleteProduct(userId, productId){
    const url = `${port}/api/cart/remove?userId=${userId}&productId=${productId}`;
    return fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        });
}

export function deleteProducts(userId, productIds) {
    // 建立查詢參數
    const queryParams = new URLSearchParams();
    queryParams.append('userId', userId);
    productIds.forEach(id => queryParams.append('productIds', id));

    // 完整 URL
    const url = `${port}/api/cart/remove/batchproducts?${queryParams.toString()}`;

    return fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        });
}


export function updateProductQuantity(userId, productId, quantity) {
    const url = `${port}/api/cart/update?userId=${userId}&productId=${productId}&quantity=${quantity}`;
    return fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(message => { // 獲取後端錯誤訊息
                    throw new Error(message); // 將訊息丟出至 catch 區塊
                });
            }
            return response.text();
        })
        .catch((error) => {
            // 顯示後端返回的錯誤訊息
            if (error.message === "商品已下架，無法添加到購物車") {
                alert("商品已下架，無法添加到購物車");
            } else if (error.message === "庫存不足，無法添加到購物車") {
                //  TODO:重新取得購物清單
                alert("庫存不足，無法添加到購物車");

            } else {
                alert("發生錯誤，請稍後再試");
            }
        })
}

// 結帳
export function checkout(fakUserId,checkoutItems,address) {
    const url = `${port}/order`;
    // 構建發送的資料
    const data = {
        userId: fakUserId,
        items: checkoutItems,
        address: address
    };
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)  // 將數據轉換為 JSON 字符串發送
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .catch(error => {
            console.error('Error updating product quantity:', error);
            throw error;
        });
}







