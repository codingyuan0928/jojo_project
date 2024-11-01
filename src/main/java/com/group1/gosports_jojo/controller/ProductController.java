package com.group1.gosports_jojo.controller;
import lulu.com.demo_productdatatable.request.ProductStatusUpdateRequest;
import lulu.com.demo_productdatatable.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     *
     * @param draw 表格中的查詢次數，用來追蹤該次查詢（例如前端 DataTable 的 AJAX 請求）
     * @param start 分頁的起始索引，表示查詢從哪個數據開始（例如第幾筆數據開始）
     * @param length 每頁的數據長度，表示一次查詢最多返回多少條數據。
     * @param searchValue 可選參數，用來處理前端的查詢過濾（例如依據產品名稱進行模糊搜索）
     * @param orderColumn 用來指定排序的列（對應表格的哪一列），例如可以按價格、產品名稱等排序 (欄位從 0 開始)
     * @param orderDirection 指定排序方向，可能是 asc（升序）或 desc（降序）
     * @return
     * draw：前端數據表格的同步查詢次數。
     * recordsTotal：總記錄數（總產品數）。
     * recordsFiltered：經過篩選後的記錄數。
     * data：實際的產品數據列表（通常是 DTO 格式的數據）。
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getData(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(name = "search[value]", required = false) String searchValue,
            @RequestParam(name = "order[0][column]", required = false) Integer orderColumn,
            @RequestParam(name = "order[0][dir]", required = false) String orderDirection,
            @RequestParam(name = "status", required = true) Integer status // 接收上架
    ){
        Map<String, Object> result = productService.getData(draw, start, length, searchValue, orderColumn, orderDirection, status);
        // 返回帶有結果的 ResponseEntity
        return ResponseEntity.ok(result);
    }

    /**
     * 上架/下架產品
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity<Map<String, String>> batchChangeStatus(@RequestBody ProductStatusUpdateRequest request) {
        List<Integer> productIds = request.getProductIds();
        Integer status = request.getStatus();

        productService.updateProductsStatus(productIds,status);

        // 返回 JSON 格式的消息
        Map<String, String> response = new HashMap<>();
        response.put("message", "下架成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 刪除產品
     * @param productIds
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteProducts(@RequestBody List<Integer> productIds) {
        productService.deleteProducts(productIds);
        // 返回 JSON 格式的消息
        Map<String, String> response = new HashMap<>();
        response.put("message", "刪除成功");
        return ResponseEntity.ok(response);
    }
}