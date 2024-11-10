package com.group1.gosports_jojo.group.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PhotoWriteAvatarForVendor {
    public static void main(String argv[]) {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3307/go_sports?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photos = "src/main/resources/static/images/vendor"; //測試用圖片已置於【專案錄徑】底下的【resources/DB_photos1】目錄內
        String update = "update vendors set avatar =? where vendor_id=?";

        int count = 1;
        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(update);
            File[] photoFiles = new File(photos).listFiles();
            for (File f : photoFiles) {
                fin = new FileInputStream(f);
                pstmt = con.prepareStatement(update);
                pstmt.setInt(2, count);
                pstmt.setBinaryStream(1, fin);
                pstmt.executeUpdate();
                count++;
                System.out.print(" update the database...");
                System.out.println(f.toString());
            }

            fin.close();
            pstmt.close();
            System.out.println("加入圖片-更新成功.........");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
