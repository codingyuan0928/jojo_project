package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.dao.impl.OrderDAO;
import com.group1.gosports_jojo.model.OrderVO;
import com.group1.gosports_jojo.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
public class SellerController {


    @Autowired
    OrderService orderSvc;


@GetMapping("/add_product")
public String addProduct() {
    return "add_product";
}
@GetMapping("/order_finish")
public String orderFinish(HttpServletRequest req, Model model, HttpServletResponse res) {


    List<OrderVO> list = orderSvc.getAll(1); // 呼叫無參數版本的 getAll 方法
    req.setAttribute("list", list);

    return "order_finish";
}
@GetMapping("order_finish_search")
public String orderFinishSearch() {
    return "order_finish_search";
}
@GetMapping("order_pending")
public String orderPending(HttpServletRequest req, Model model, HttpServletResponse res) {

    List<OrderVO> list = orderSvc.getAll(0); // 呼叫無參數版本的 getAll 方法
    req.setAttribute("list", list);

    return "order_pending";


}
@GetMapping("order_pending_search")
public String orderPendingSearch() {

    return "order_pending_search";
}
@GetMapping("product_menu")
public String productMenu() {

    return "product_menu";
}






@PostMapping("/searchFinishOrderTime")
public String searchOrderTime(HttpServletRequest req, Model model, HttpServletResponse res) {

    String time1 = req.getParameter("time1");
    String time2 = req.getParameter("time2");


    // 檢查 time1 和 time2 是否為 null 或空字串
    if (time1 == null || time1.isEmpty() || time2 == null || time2.isEmpty()) {
        // 如果 time1 或 time2 為 null 或空，處理錯誤情況，例如設置默認值或返回錯誤訊息
        req.setAttribute("errorMsg", "日期區間無效，請重新選擇時間範圍。");

        return "order_finish_search"; // 結束這個請求處理
    }

    System.out.println(time1);
    System.out.println(time2);
    DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;
    LocalDate timeOne = LocalDate.parse(time1);
    LocalDate timeTwo = LocalDate.parse(time2);

    Timestamp beginTime;
    Timestamp endTime;
    Integer orderStatus = 0 ;

    if(timeOne.isBefore(timeTwo)) {
        beginTime = Timestamp.valueOf(timeOne.atStartOfDay());
        endTime = Timestamp.valueOf(timeTwo.atStartOfDay());
    }else {
        beginTime = Timestamp.valueOf(timeTwo.atStartOfDay());
        endTime = Timestamp.valueOf(timeOne.atStartOfDay());
    }




    Set<OrderVO> orderList = orderSvc.getOrdersByTime(beginTime, endTime);//, orderStatus);

    req.setAttribute("OrderList", orderList); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req

    orderList.forEach(e->{
        System.out.println(e);
    });



    return "order_finish_search";
}
    @PostMapping("/searchPendingOrderTime")
    public String searchPendingOrderTime(HttpServletRequest req, Model model, HttpServletResponse res) {

        String time1 = req.getParameter("time1");
        String time2 = req.getParameter("time2");


        // 檢查 time1 和 time2 是否為 null 或空字串
        if (time1 == null || time1.isEmpty() || time2 == null || time2.isEmpty()) {
            // 如果 time1 或 time2 為 null 或空，處理錯誤情況，例如設置默認值或返回錯誤訊息
            req.setAttribute("errorMsg", "日期區間無效，請重新選擇時間範圍。");

            return "order_pending_search"; // 結束這個請求處理
        }

        System.out.println(time1);
        System.out.println(time2);
        DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate timeOne = LocalDate.parse(time1);
        LocalDate timeTwo = LocalDate.parse(time2);

        Timestamp beginTime;
        Timestamp endTime;
        Integer orderStatus = 0 ;

        if(timeOne.isBefore(timeTwo)) {
            beginTime = Timestamp.valueOf(timeOne.atStartOfDay());
            endTime = Timestamp.valueOf(timeTwo.atStartOfDay());
        }else {
            beginTime = Timestamp.valueOf(timeTwo.atStartOfDay());
            endTime = Timestamp.valueOf(timeOne.atStartOfDay());
        }




        Set<OrderVO> orderList = orderSvc.getOrdersByTime(beginTime, endTime);//, orderStatus);

        req.setAttribute("OrderList", orderList); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req

        orderList.forEach(e->{
            System.out.println(e);
        });



        return "order_pending_search";
    }










}

