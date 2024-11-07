package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.model.*;
import com.group1.gosports_jojo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller

public class PostController {

    @Autowired
    PostService postSvc;
    @Autowired
    ReplyService replySvc;
    @Autowired
    ResponseService responseSvc;

    @GetMapping("/forum_add_post")
    public String forumAddPost( Model model){
        return "forum_add_post";
    }

    @GetMapping("/forum_list_all_post")
    public String forumListAllPost(HttpServletRequest req, Model model, HttpServletResponse res){

        List<PostVO> list = postSvc.getAll();
        req.setAttribute("list", list);

        List<PostVO> list2 = postSvc.getAll2();
        req.setAttribute("list2", list2);

        return "forum_list_all_post";
    }

    @GetMapping("/getOne")
    public String forumListOnePost(HttpServletRequest req, Model model, HttpServletResponse res){

        /*************************** 1.接收請求參數 ****************************************/
        Integer postId = Integer.valueOf(req.getParameter("post_id"));
        Integer userId = Integer.valueOf(req.getParameter("user_id"));

        /*************************** 2.開始查詢資料 ****************************************/
        PostVO postVO = postSvc.getOnePost(postId);
        req.setAttribute("postVO", postVO);

        List<ReplyVO> list = replySvc.getOnePostReply(postId);
        req.setAttribute("list", list);
        //確認用戶是否在某篇文章讚過讚
        ResponseVO responseVO = responseSvc.getPostAllResponse(postId, userId);
        if (responseVO == null) { // 檢查 responseVO 是否為 null
            responseVO = new ResponseVO(); // 建立新的 ResponseVO
            responseVO.setResponse_status(0); // 設定默認狀態為 0
        } else if (responseVO.getResponse_status() == null) {
            responseVO.setResponse_status(0); // 如果狀態為 null，也設定為 0
        }
        req.setAttribute("responseVO", responseVO);

        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
        return "forum_list_one_post";
    }

    @GetMapping("/forum_update_post")
    public String forumUpdatePost(){
        return "forum_update_post";
    }

    @PostMapping("/search_post_keyword")
    public String searchPostKeyword(HttpServletRequest req, Model model, HttpServletResponse res){

        Map<String,String> errorMsgs = new LinkedHashMap<String,String>();  //把錯誤資訊存在ermaps，並且用map存取
        req.setAttribute("errorMsgs", errorMsgs);

        if (!errorMsgs.isEmpty()) {

            List<PostVO> list = postSvc.getAll();
            req.setAttribute("list",list);

            List<PostVO> list2 = postSvc.getAll2();
            req.setAttribute("list2", list2);

            return "forum_list_all_post";//程式中斷
        }

        /***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
        String keyword = req.getParameter("keyword");

        if (keyword.isEmpty()) {
            errorMsgs.put("empty2","查無資料");
        }

        if (!errorMsgs.isEmpty()) {
            List<PostVO> list = postSvc.getAll();
            req.setAttribute("list",list);

            List<PostVO> list2 = postSvc.getAll2();
            req.setAttribute("list2", list2);

            return "forum_list_all_post";//程式中斷
        }
        /***************************2.開始查詢資料*****************************************/
        List<PostVO> search_result1 =   postSvc.SEARCH_POST(keyword, keyword);
        List<PostVO> search_result2 =   postSvc.SEARCH_POST_BY_POP(keyword, keyword);

        /***************************3.查詢完成,準備轉交(Send the Success view)*************/
        req.setAttribute("list", search_result1);   // 資料庫取出的empVO物件,存入req
        req.setAttribute("list2", search_result2);   // 資料庫取出的empVO物件,存入req

        return "forum_list_all_post";
    }


    @PostMapping("/GET_ONE_POST_REPLY")
    public String getOnePostReply (HttpServletRequest req, Model model, HttpServletResponse res){


        Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
        req.setAttribute("errorMsgs", errorMsgs);

        /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/


        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();
        System.out.println("============" + req.getParameter("post_id"));
        Integer post_id = Integer.valueOf(req.getParameter("post_id"));
        System.out.println("============" + post_id);

        String reply_content = req.getParameter("reply_content");

        if (reply_content == null || reply_content.trim().length() == 0) {
            errorMsgs.put("reply_content", "留言請勿空白");
        }
        // 若有錯誤，返回原頁面
        if (!errorMsgs.isEmpty()) {
            /*************************** 2.開始查詢資料 ****************************************/
            PostVO postVO = postSvc.getOnePost(post_id);
            req.setAttribute("postVO", postVO);

            List<ReplyVO> list = replySvc.getOnePostReply(post_id);
            req.setAttribute("list", list);
            //確認用戶是否在某篇文章讚過讚
            ResponseVO responseVO = responseSvc.getPostAllResponse(post_id, user_id);
            if (responseVO == null) { // 檢查 responseVO 是否為 null
                responseVO = new ResponseVO(); // 建立新的 ResponseVO
                responseVO.setResponse_status(0); // 設定默認狀態為 0
            } else if (responseVO.getResponse_status() == null) {
                responseVO.setResponse_status(0); // 如果狀態為 null，也設定為 0
            }
            req.setAttribute("responseVO", responseVO);

            /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
            return "forum_list_one_post";
        }
        //有值
        /*************************** 2.開始新增資料 ***************************************/

        ReplyVO replyVO = replySvc.addReply(user_id, post_id, reply_content);
        req.setAttribute("replyVO", replyVO);

        PostVO postVO = postSvc.getOnePost(post_id);
        req.setAttribute("postVO", postVO);

        List<ReplyVO> list = replySvc.getOnePostReply(post_id);
        req.setAttribute("list", list);

        ResponseVO responseVO = responseSvc.getPostAllResponse(post_id, user_id);
        if (responseVO == null) { // 檢查 responseVO 是否為 null
            responseVO = new ResponseVO(); // 建立新的 ResponseVO
            responseVO.setResponse_status(0); // 設定默認狀態為 0
        } else if (responseVO.getResponse_status() == null) {
            responseVO.setResponse_status(0); // 如果狀態為 null，也設定為 0
        }
        req.setAttribute("responseVO", responseVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) ***********/

        return "forum_list_one_post";
    }


    @PostMapping("/insert_post")
    public String insertPost (HttpServletRequest req, Model model, HttpServletResponse res){

        Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
        req.setAttribute("errorMsgs", errorMsgs);

        /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        String post_category = req.getParameter("post_category").trim();
        if (post_category == null || post_category.trim().length() == 0) {
            errorMsgs.put("post_category", "請選擇文章類別");
        }

        String post_title = req.getParameter("post_title").trim();
        if (post_title == null || post_title.trim().length() == 0) {
            errorMsgs.put("post_title", "請填寫文章標題");
        }

        String post_content = req.getParameter("post_content").trim();
        if (post_content == null || post_content.trim().length() == 0) {
            errorMsgs.put("post_content", "請填寫文章內容");
        }

        // 若有錯誤，返回原頁面
        if (!errorMsgs.isEmpty()) {
            return "forum_add_post";
        }

        postSvc.addPost(user_id, post_category, post_title, post_content);

        List<PostVO> list = postSvc.getAll();
        req.setAttribute("list", list);

        List<PostVO> list2 = postSvc.getAll2();
        req.setAttribute("list2", list2);

        return "forum_list_all_post";
    }



    @PostMapping("/getOne_For_Update")
    public String getOneForUpdate(HttpServletRequest req, Model model, HttpServletResponse res){


        Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
        req.setAttribute("errorMsgs", errorMsgs);


        /*************************** 1.接收請求參數 ****************************************/
        Integer postId = Integer.valueOf(req.getParameter("post_id"));

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        /*************************** 2.開始查詢資料 ****************************************/
        PostVO postVO = postSvc.getOnePost(postId);
        req.setAttribute("postVO", postVO);


        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
        return "forum_update_post";
    }





    @PostMapping("/forum_update")
    public String forumUpdate(HttpServletRequest req, Model model, HttpServletResponse res){

        /*************************** 1.接收請求參數 ****************************************/

        Integer postId = Integer.valueOf(req.getParameter("post_id"));

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
        req.setAttribute("errorMsgs", errorMsgs);

        /*************************** 2.開始查詢資料 ****************************************/

        String post_category = req.getParameter("post_category");
//        if (post_category == null || post_category.trim().length() == 0) {
//            errorMsgs.put("post_category", "請選擇文章類別");
//        }

        String post_title = req.getParameter("post_title");
        if (post_title == null || post_title.trim().length() == 0) {


            errorMsgs.put("post_title", "請填寫文章標題");

        }

        String post_content = req.getParameter("post_content");
        if (post_content == null || post_content.trim().length() == 0) {


            errorMsgs.put("post_content", "請填寫文章內容");

        }

        // 若有錯誤，返回原頁面
        if (!errorMsgs.isEmpty()) {

            PostVO postVO = postSvc.getOnePost(postId);
            req.setAttribute("postVO", postVO);

            return "forum_update_post";
        }

        PostVO listPostVO = postSvc.updatePost2(post_title, post_category, post_content, postId);


        List<PostVO> list = postSvc.getAll();
        req.setAttribute("list", list);

        List<PostVO> list2 = postSvc.getAll2();
        req.setAttribute("list2", list2);
        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/



        return "forum_list_all_post";
    }





    @PostMapping("/post_delete")
    public String postDelete(HttpServletRequest req, Model model, HttpServletResponse res){



        /*************************** 1.接收請求參數 ****************************************/

        Integer postId = Integer.valueOf(req.getParameter("post_id"));

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        /*************************** 2.開始查詢資料 ****************************************/

        postSvc.deletePost(postId);

        List<PostVO> list = postSvc.getAll();
        req.setAttribute("list", list);

        List<PostVO> list2 = postSvc.getAll2();
        req.setAttribute("list2", list2);
        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/



        return "forum_list_all_post";
    }







    @PostMapping("/cancel_response")
    public String cancelResponse(HttpServletRequest req, Model model, HttpServletResponse res){



        /*************************** 1.接收請求參數 ****************************************/

        Integer postId = Integer.valueOf(req.getParameter("post_id"));

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        /*************************** 2.開始查詢資料 ****************************************/

        Integer response_id = Integer.valueOf(req.getParameter("response_id"));

        responseSvc.updateResponse(response_id);
        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/



        PostVO postVO = postSvc.getOnePost(postId);
        req.setAttribute("postVO", postVO);

        List<ReplyVO> list = replySvc.getOnePostReply(postId);
        req.setAttribute("list", list);
        //確認用戶是否在某篇文章讚過讚
        ResponseVO responseVO = responseSvc.getPostAllResponse(postId, user_id);
        if (responseVO == null) { // 檢查 responseVO 是否為 null
            responseVO = new ResponseVO(); // 建立新的 ResponseVO
            responseVO.setResponse_status(0); // 設定默認狀態為 0
        } else if (responseVO.getResponse_status() == null) {
            responseVO.setResponse_status(0); // 如果狀態為 null，也設定為 0
        }
        req.setAttribute("responseVO", responseVO);

        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
        return "forum_list_one_post";


    }




    @PostMapping("/response_good")
    public String responseGood(HttpServletRequest req, Model model, HttpServletResponse res){



        /*************************** 1.接收請求參數 ****************************************/

        Integer postId = Integer.valueOf(req.getParameter("post_id"));

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer user_id = userVO.getUserId();

        /*************************** 2.開始查詢資料 ****************************************/


        responseSvc.insert(postId,user_id );
        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/


        PostVO postVO = postSvc.getOnePost(postId);
        req.setAttribute("postVO", postVO);

        List<ReplyVO> list = replySvc.getOnePostReply(postId);
        req.setAttribute("list", list);
        //確認用戶是否在某篇文章讚過讚
        ResponseVO responseVO = responseSvc.getPostAllResponse(postId, user_id);
        if (responseVO == null) { // 檢查 responseVO 是否為 null
            responseVO = new ResponseVO(); // 建立新的 ResponseVO
            responseVO.setResponse_status(0); // 設定默認狀態為 0
        } else if (responseVO.getResponse_status() == null) {
            responseVO.setResponse_status(0); // 如果狀態為 null，也設定為 0
        }
        req.setAttribute("responseVO", responseVO);

        /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
        return "forum_list_one_post";


    }







}
