package com.group1.gosports_jojo;

import com.group1.gosports_jojo.security.PasswordUtil;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@EnableScheduling
@SpringBootApplication
public class GosportsJojoApplication implements CommandLineRunner {

    @Autowired
    private PasswordUtil passwordUtil; // 注入加密工具

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(GosportsJojoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        updatePasswordsForTable("users", "user_id");
        updatePasswordsForTable("vendors", "vendor_id");
        updatePasswordsForTable("administrators", "administrator_id");
    }

    public void updatePasswordsForTable(String tableName, String idColumn) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String url = "jdbc:mysql://localhost:3306/go_sports?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String updatePassword = "UPDATE " + tableName + " SET password = ? WHERE " + idColumn + " = ?";

        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(updatePassword);
            int id = 1; // 假設從 1 開始遍歷每個表的 ID
            while (true) {
                // 使用 PasswordUtil 加密密碼
                String hashedPassword = passwordUtil.encode("123456");
                pstmt.setString(1, hashedPassword);
                pstmt.setInt(2, id);

                int updatedRows = pstmt.executeUpdate();
                if (updatedRows == 0) break; // 無行數更新，表示已遍歷完畢
                System.out.println("已更新 " + tableName + " 中 ID 為 " + id + " 的密碼");
                id++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
