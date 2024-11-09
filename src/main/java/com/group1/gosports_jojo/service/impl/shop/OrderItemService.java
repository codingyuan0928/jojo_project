package com.group1.gosports_jojo.service.impl.shop;

import com.group1.gosports_jojo.dto.shopping.CheckoutItem;
import com.group1.gosports_jojo.dto.shopping.CheckoutRequest;
import com.group1.gosports_jojo.dto.shopping.MyOrderItemResponse;
import com.group1.gosports_jojo.entity.Order;
import com.group1.gosports_jojo.entity.Orderitem;
import com.group1.gosports_jojo.entity.User;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.repository.OrderItemRepository;
import com.group1.gosports_jojo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping
    public void saveOrder(CheckoutRequest checkoutRequest) {
        Integer userId = checkoutRequest.getUserId();
        List<CheckoutItem> checkoutRequestItems = checkoutRequest.getItems();

        //Key: 廠商 ID, value: 使用者在該廠商下單的所有產品金額
        HashMap<Integer, Integer> map = new HashMap<>();

        // 新增訂單
        for (CheckoutItem item : checkoutRequestItems) {
            // 如果該 vendorId 不存在於 map 中，則初始化金額，否則累加金額
            if (!map.containsKey(item.getVendorId())) {
                map.put(item.getVendorId(), item.getPrice()*item.getQuantity());
            } else {
                map.put(item.getVendorId(), map.get(item.getVendorId()) + item.getPrice()* item.getQuantity());
            }
        }

        // 用來儲存成功新增的訂單，Key: 廠商ID , Value: 訂單ID
        Map<Integer, Integer> orderMap = new HashMap<>();

        // map Key: 廠商 ID, value: 使用者在該廠商下單的所有產品金額
        map.forEach((vendorId, amount) -> {
            Order order = new Order();

            User user = new User();
            user.setUserId(userId);
            order.setUser(user);

            order.setOrderStatus(0);

            Vendor vendor = new Vendor();
            vendor.setVendorId(vendorId);
            order.setVendor(vendor);
            order.setCreatedDatetime(new Timestamp(System.currentTimeMillis()));
            order.setUpdatedDatetime(new Timestamp(System.currentTimeMillis()));

            order.setTotalAmount(amount);

            // 訂單 存到資料庫
            Order savedOrder  = orderRepository.save(order);
            // 使用 Order 物件作為 Key，Order 的 ID 作為 Value，加入到 Map 中
            orderMap.put(vendorId,savedOrder.getId());

        });


        // 新增購買商品
        for (CheckoutItem item : checkoutRequestItems) {
            // 創建購買的商品物件
            Orderitem orderitem = new Orderitem();

            Integer orderID = orderMap.get(item.getVendorId());
            orderitem.setOrderId(orderID);
            orderitem.setProductId(item.getProductId());
            orderitem.setQuantity(item.getQuantity());
            orderitem.setPrice(item.getPrice());
            orderitem.setPickAddress(checkoutRequest.getAddress()); //TODO



            // 購買的商品 存到資料庫
            orderItemRepository.save(orderitem);
        }

    }

    public List<Orderitem> getAll(Integer orderitemId){
        return orderItemRepository.findAll();
    }


    public List<Order> getMyOrder(Integer userId){
        // 購物車成功下單的商品
        return orderRepository.findByUser_UserId(userId);
    }
    public List<MyOrderItemResponse> getmyOrderitems(Integer orderId) {
        List<Object[]> orderDetailsByOrderId = orderItemRepository.findOrderDetailsByOrderId(orderId);

        List<MyOrderItemResponse> myOrderItems = orderDetailsByOrderId.stream().map(item -> {
            Integer orderid = (Integer) item[0];
            Integer userId = (Integer) item[1];
            Integer totalAmount = (Integer) item[2];
            Timestamp createDatetime = (Timestamp) item[3];
            Integer vendorId = (Integer) item[4];
            Timestamp pickupDatetime = (Timestamp) item[5];
            Integer productId = (Integer) item[6];
            Integer quantity = (Integer) item[7];
            Integer price = (Integer) item[8];
            String pickaddress = (String) item[9];
            String productName = (String) item[10];
            String shopName = (String) item[11];
            String userName = (String) item[12];
            String email = (String) item[13];


            MyOrderItemResponse orderItem = new MyOrderItemResponse(
                    orderid, userId, totalAmount, createDatetime, vendorId,
                    pickupDatetime, productId, quantity, price,pickaddress,
                    productName, shopName, userName, email
            );

            // 打印每個 MyOrderItemResponse 的內容
            System.out.println("Order Item: " + orderItem);

            return orderItem;
        }).collect(Collectors.toList());

        return myOrderItems;
    }

}
