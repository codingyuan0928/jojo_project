package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.dao.impl.AddProductDAO;
import com.group1.gosports_jojo.model.AddProductVO;
import com.group1.gosports_jojo.model.OrderVO;
import com.group1.gosports_jojo.service.impl.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/vendors")
public class SellerController {


    @Autowired
    OrderService orderSvc;
    @Autowired
    AddProductDAO adao;



@GetMapping("/order_finish")
public String orderFinish(HttpServletRequest req, Model model, HttpServletResponse res) {


    List<OrderVO> list = orderSvc.getAll(1); // 呼叫無參數版本的 getAll 方法
    req.setAttribute("list", list);

    return "order_finish";
}

@PostMapping("/order_views")
public String orderPending(Model model, @RequestParam("id") Integer id
,@RequestParam("status") Integer status) {
    orderSvc.UpdateOrder(id, status);
    List<OrderVO> list = orderSvc.getAll(0); // 呼叫無參數版本的 getAll 方法
    model.addAttribute("list", list);
    model.addAttribute("port",8083);
    return "order_pending";
}

@GetMapping("/order_pending")
public String orderviews(Model model) {
    List<OrderVO> list = orderSvc.getAll(0); // 呼叫無參數版本的 getAll 方法
    model.addAttribute("list", list);
    return "order_pending";
}

@GetMapping("/product_menu")
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

        return "order_finish_searcdddh"; // 結束這個請求處理
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

    req.setAttribute("list", orderList);

    orderList.forEach(e->{
        System.out.println(e);
    });



    return "order_finish";
}

@PostMapping("/searchPendingOrderTime")
public String searchPendingOrderTime(HttpServletRequest req, Model model, HttpServletResponse res) {

        String time1 = req.getParameter("time1");
        String time2 = req.getParameter("time2");


        // 檢查 time1 和 time2 是否為 null 或空字串
        if (time1 == null || time1.isEmpty() || time2 == null || time2.isEmpty()) {
            // 如果 time1 或 time2 為 null 或空，處理錯誤情況，例如設置默認值或返回錯誤訊息
            req.setAttribute("errorMsg", "日期區間無效，請重新選擇時間範圍。");
            return "order_pending";
            //return "order_pending_search"; // 結束這個請求處理
        }

        System.out.println(time1);
        System.out.println(time2);

        System.out.println("123");
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

    /* req.setAttribute("OrderList", orderList); */
        model.addAttribute("list", orderList);

//    model.addAttribute("list", orderSvc.getAll(0));

        return "order_pending";
        //return "order_pending_search";
    }


@GetMapping("/add_product")
    public String addProduct(HttpServletRequest req, Model model, HttpServletResponse res){
        return "add_product";

    }


@PostMapping("/insert_product")
public String insertProduct(HttpServletRequest req, Model model, HttpServletResponse res) throws
        IOException, ServletException {

    String productName = req.getParameter("productName");
    String productSpec = req.getParameter("productSpec");
    String price = req.getParameter("price");
    String stock = req.getParameter("stock");
    String productContent = req.getParameter("productContent");
    String action = req.getParameter("actions");

    AddProductVO addVO = new AddProductVO();


    int price1 = Integer.parseInt(price);
    int stock1 = Integer.parseInt(stock);
    int action1 = Integer.parseInt(action);


    addVO.setProductName(productName);
    addVO.setProductSpec(productSpec);
    addVO.setPrice(price1);
    addVO.setStock(stock1);
    addVO.setProductContent(productContent);
    //addVO.setAddress(address);



    String errm = adao.insert(addVO, action1);

    int maxId = adao.maxId();

    Part pic1 = req.getPart("pic1");
    Part pic2 = req.getPart("pic2");
    Part pic3 = req.getPart("pic3");
    Part pic4 = req.getPart("pic4");
    Part pic5 = req.getPart("pic5");

    InputStream pic = null;

    if(pic1 != null && pic1.getSize()>0) {

        pic = pic1.getInputStream();
        adao.insert2(pic, maxId);
    }

    if(pic2 != null && pic2.getSize()>0) {

        pic = pic2.getInputStream();
        adao.insert2(pic, maxId);
    }

    if(pic3 != null && pic3.getSize()>0) {

        pic = pic3.getInputStream();
        adao.insert2(pic, maxId);
    }
    if(pic4 != null && pic4.getSize()>0) {

        pic = pic4.getInputStream();
        adao.insert2(pic, maxId);
    }
    if(pic5 != null && pic5.getSize()>0) {

        pic = pic5.getInputStream();
        adao.insert2(pic, maxId);
    };


    //res.sendRedirect(url);

    req.setAttribute("ERRM", errm); //
    //檢查錯誤

        return "add_product";
    }
}

