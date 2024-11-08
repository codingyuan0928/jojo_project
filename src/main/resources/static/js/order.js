// 初始化 DataTable1
function initDataTable1() {
    new DataTable('#myTable1', {
        ajax: {
          url: '/products',
            data: {

                status: 1 // 0: 下架 1: 上架
            }
        },
        processing: true,
        serverSide: true,
        columns: [
            {
                data: null,
                title: '<input type="checkbox" id="select-all-table1" onclick="checkAll(this)">',
                render: function (data, type, row) {
                    return '<input class="form-check-input" type="checkbox" value="' + row.productId + '" id="flexCheckDefault' + row.productId + '" onchange="handleCheckboxChange(this)">';
                }
            },
            { data: 'productId', title: '編號' },
            { data: 'productName', title: '商品名稱' },
            { data: 'price', title: '價錢' },
            { data: 'stock', title: '剩餘庫存' },
            {
                data: null, title: '操作',
                render: function (data, type, row) {
                    return '<button type="button" class="btn btn-success btn-sm me-3" onclick="changeProductStatus(this,' + row.productId + ')">下架</button>' +
                        // '<a class="btn btn-sm btn-primary me-3" href="edit.html?id=' + row.productId + '" role="button" data-bs-target="#popup">編輯</a>' +
                        '<button type="button" class="btn btn-danger btn-sm me-3" onclick="deleteItem(' + row.productId + ')">刪除</button>';
                }
            }
        ],
        paging: true,
        columnDefs: [{ orderable: false, targets: [0, 2, 4, 5] }],
        order: [[1, 'asc']]
    });
}

// 初始化 DataTable2
function initDataTable2() {
    new DataTable('#myTable2', {
        ajax: {
            url: '/products',
            data: {

                status: 0 // 0: 下架 1: 上架
            }
        },
        processing: true,
        serverSide: true,
        columns: [
            {
                data: null,
                title: '<input type="checkbox" id="select-all-table2" onclick="checkAll(this)">',
                render: function (data, type, row) {
                    return '<input class="form-check-input" type="checkbox" value="' + row.productId + '" id="flexCheckDefault' + row.productId + '" onchange="handleCheckboxChange(this)">';
                }
            },
            { data: 'productId', title: '編號' },
            { data: 'productName', title: '商品名稱' },
            { data: 'price', title: '價錢' },
            { data: 'stock', title: '剩餘庫存' },
            {
                data: null, title: '操作',
                render: function (data, type, row) {
                    return '<button type="button" class="btn btn-success btn-sm me-3" onclick="changeProductStatus(this,' + row.productId + ')">上架</button>' +
                        // '<a class="btn btn-sm btn-primary me-3" href="@{/vendors/add_product(productId={product.productId})}' + row.productId + '" role="button" data-bs-target="#popup">編輯</a>' +
                        '<button type="button" class="btn btn-danger btn-sm me-3" onclick="deleteItem(' + row.productId + ')">刪除</button>';
                }
            }
        ],
        paging: true,
        columnDefs: [{ orderable: false, targets: [0, 2, 4, 5] }],
        order: [[1, 'asc']]
    });
}

/**
 * 全部勾選/取消全部勾選
 */

let checkedList = []
function checkAll(element){
    // 動態選擇表格
    let tableId = element.id === 'select-all-table1' ? '#myTable1' : '#myTable2';

    var checkboxes = document.querySelectorAll(`${tableId} input[type="checkbox"]`);
    // 檢查全選框的狀態
    if (element.checked) {
        // 如果全選框被勾選，將所有的 checkbox 勾選並加入 checkedList
        checkedList = []; // 先清空 checkedList
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = true;
            if (checkbox.value !== 'on') { // 避免將非目標值加入
                checkedList.push(checkbox.value);
            }
        });
    } else {
        // 如果全選框未勾選，取消所有的 checkbox 並清空 checkedList
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = false;
        });
        checkedList = []; // 清空已勾選的列表
    }

    console.log('已勾選項目:', checkedList);  // 輸出已勾選的項目
}

/**
 * 單一勾選
 */
function handleCheckboxChange(element) {
    const value = element.value;

    if (element.checked) {
        // 如果勾選，且 checkedList 中尚未存在該值，則加入
        if (!checkedList.includes(value)) {
            checkedList.push(value);
        }
    } else {
        // 如果取消勾選，從 checkedList 中移除該值
        checkedList = checkedList.filter(item => item !== value);
    }

    console.log('已勾選項目:', checkedList);  // 列出所有被勾選的項目

    // 檢查是否所有 checkbox 都已勾選，如果是，則全選框也應被勾選
    const allCheckboxes = document.querySelectorAll('#myTable1 input[type="checkbox"]');
    const allChecked = Array.from(allCheckboxes).every(checkbox => checkbox.checked);
    document.getElementById('select-all-table1').checked = allChecked;
    document.getElementById('select-all-table2').checked = allChecked;
}


/**
 * 批次更新(上架/下架)狀態
 */
const confirmRemoveModal = new bootstrap.Modal(document.getElementById('confirmRemoveModal'));
const confirmUploadModal = new bootstrap.Modal(document.getElementById('confirmUploadModal'));
function batchChangeStatus(element){
    const text = element.innerHTML
    if(text === '批次下架'){
        // 顯示提醒視窗
        confirmRemoveModal.show();
        console.log('打開下架提醒視窗');
    }
    if(text === '批次上架'){
        // 顯示提醒視窗
        confirmUploadModal.show();
        console.log('打開上架提醒視窗');
    }
}

async function confirmBatchChangStatus(element){
    console.log('顯示提醒視窗確認');
    // 取得被勾選的項目
    console.log(checkedList);
    console.log('呼叫 API')

    if(element.dataset.status === 'unshelve'){
        await changeProductStatusAPI(checkedList,1) // 目前商品狀態上架:1

        // 全選 checkbox 取消勾選
        const selectAllTable1 = document.getElementById('select-all-table1');
        if (selectAllTable1 && selectAllTable1.checked) {
            selectAllTable1.checked = false;
            console.log('已上架全選 checkbox 已取消勾選');
        }

        console.log('關閉提醒視窗');
        confirmRemoveModal.hide();
    }

    if(element.dataset.status === 'shelve'){
        await changeProductStatusAPI(checkedList,0) // 目前商品狀態下架:0

        // 全選 checkbox 取消勾選
        const selectAllTable2 = document.getElementById('select-all-table2');
        if (selectAllTable2 && selectAllTable2.checked) {
            selectAllTable2.checked = false;
            console.log('已下架全選 checkbox 已取消勾選');
        }

        console.log('關閉提醒視窗');
        confirmUploadModal.hide();


    }

    // 重新載入兩張 table
    $('#myTable1').DataTable().ajax.reload();
    $('#myTable2').DataTable().ajax.reload();

}

/**
 * 單一商品上/下架
 */
function changeProductStatus(item,id){
    const text = item.innerHTML
    if(text === '下架'){
        console.log(item + '準備被下架');
        // 將被勾選的商品加到 checkedList
        checkedList = [];
        checkedList.push(id);
        confirmRemoveModal.show();
    }
    if(text === '上架'){
        console.log(item + '準備被上架');
        // 將被勾選的商品加到 checkedList
        checkedList = [];
        checkedList.push(id);
        confirmUploadModal.show();
    }

}


/**
 * 單一商品刪除
 */
const confirmDeleteModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
async function deleteItem(id){
    console.log(id + '準備刪除');

        // 將被勾選的商品加到 checkedList
        checkedList = [];
        checkedList.push(id);
        confirmDeleteModal.show();
}

async function deleteProduct(){
    await changeProductStatusAPI(checkedList,1,true) // 刪除狀態:2
    confirmDeleteModal.hide();
    // 重新載入兩張 table
    $('#myTable1').DataTable().ajax.reload();
    $('#myTable2').DataTable().ajax.reload();
}
// let deleteList = []
// const confirmDeleteModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
// function deleteItem(item){
//     console.log(item + '準備被刪除');
//     // 將要被刪除的項目加到 checkedList
//     deleteList = [];
//     deleteList.push(item);
//     confirmDeleteModal.show();
//     console.log('打開提醒視窗');
// }
//
//
// async function deleteProduct(){
//     console.log('顯示提醒視窗確認');
//     // 取得被勾選的項目
//     console.log(deleteList);
//     console.log('呼叫 API')
//
//     // 刪除商品 API
//     await deleteProductAPI(deleteList);
//
//     console.log('關閉提醒視窗');
//     confirmDeleteModal.hide();
//
//     // 重新載入兩張 table
//     $('#myTable1').DataTable().ajax.reload();
//     $('#myTable2').DataTable().ajax.reload();
// }

/**
 * 更改商品狀態 API
 * @param productIds
 * @param status
 * @returns {Promise<any>}
 */
async function changeProductStatusAPI(productIds, status, isDelete){
    const url = '/products';

    const requestBody = {
        productIds: productIds,
        status: status,
        isDelete: isDelete
    };
    return fetch(url, {
        method: 'PUT', // 指定 HTTP 方法為 PUT
        headers: {
            'Content-Type': 'application/json' // 設置內容類型為 JSON
        },
        body: JSON.stringify(requestBody) // 將資料轉換為 JSON 格式發送
    })
        .then(response => {
            console.log(response)
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
    });
}

async function deleteProductAPI(productIds) {
    // 確保 productIds 是一個陣列
    if (!Array.isArray(productIds)) {
        productIds = [productIds];
    }

    const url = '/products';

    try {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productIds)
        });

        // 確認 API 回應
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }

        // 解析回應數據
        const data = await response.json();
        console.log('產品刪除成功', data);
        return data;
    } catch (error) {
        console.error('刪除產品時發生錯誤:', error);
        throw error;
    }
}


document.addEventListener('DOMContentLoaded', function () {
    initDataTable1();
    initDataTable2();
});


// 監聽 Bootstrap 的 Tab 切換事件
document.querySelectorAll('button[data-bs-toggle="tab"]').forEach(tab => {
    tab.addEventListener('shown.bs.tab', function (event) {
        // 每次切換 Tab 清空 checkedList
        checkedList = [];
        // 每次切換 Tab 清空 checkedList
        deleteList = [];
        console.log('Tab 切換，已清空 checkedList 和 deleteList');
    });
});
