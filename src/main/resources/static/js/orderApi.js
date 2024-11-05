// 範例
export async function fetchOrderItem(orderItemId){
    const url = `${port}/api/order?`
    try {
        const response = await fetch(url);
        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
    }
        // 解析回應的 JSON 資料
        const orderData = await response.json();

        // 在前端顯示資料
        displayOrderData(orderData);

    } catch (error) {
     console.error('Fetch error:', error);
       throw error;  // 可以根據需求決定是否需要 rethrow error
     }
}

