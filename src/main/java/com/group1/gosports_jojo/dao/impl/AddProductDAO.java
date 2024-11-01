package com.group1.gosports_jojo.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;

import com.group1.gosports_jojo.dao.AddProductDAO_interface;
import com.group1.gosports_jojo.model.AddProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddProductDAO implements AddProductDAO_interface {

	private final DataSource ds;
    // Constructor - 傳入資料庫連接


	@Autowired
	public AddProductDAO(DataSource dataSource){
		this.ds = dataSource;
	}

    // 插入產品資料
    public String insert(AddProductVO product, int action) {
    	
    	String errm = "err:";//檢查錯誤
    	
    	Connection con = null;
    	PreparedStatement statement = null;
    	try{
    	con = ds.getConnection();
    	
    	if(con ==null) {
    		
    		errm = errm + "no connection";
    	}
    	
        String sql = "INSERT INTO products (product_content, product_name, price, product_spec, stock, created_datetime,  product_status, product_updated_datetime, vendor_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 114)"; //vendor_id不能為null,先寫死
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        statement = con.prepareStatement(sql);

            
            statement.setString(1, product.getProductContent());
            statement.setString(2, product.getProductName());
            statement.setInt(3, product.getPrice());
            statement.setString(4, product.getProductSpec());
            statement.setInt(5, product.getStock());
            if(action == 1){
            statement.setTimestamp(6, now);
            }else {
            statement.setTimestamp(6, null);
            };
            statement.setInt(7, action);
            statement.setTimestamp(8, now);
            int rowsAffected = statement.executeUpdate();//受影響行數
            
            //檢查錯誤
            if (rowsAffected == 0) {
            	errm = errm + "nothing change";
            }else if(rowsAffected ==1) {
            	
            	errm = errm+"插入一行但你看不到";
            }
          //檢查錯誤
        }
    	catch (SQLException se) {
			throw new RuntimeException(
					errm = errm + "A database error occured. "+ se.getMessage());//檢查錯誤
			// Clean up JDBC resources
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
    	
    	return errm;
    }
    	 //insert圖片的方法
   	 public void insert2(InputStream pic, int product_id ) {
     	Connection con = null;
   		PreparedStatement statement = null;
   		try{
   		con = ds.getConnection();
   		
   		String insert = "INSERT INTO pictures(pic_created_datetime, product_id, picture) "
                + "VALUES (?, ?, ?)";
   		
        Timestamp now = new Timestamp(System.currentTimeMillis());
   		
   		statement = con.prepareStatement(insert);
   		
        statement.setTimestamp(1, now);
        statement.setInt(2, product_id);
        statement.setBlob(3, pic);
        
        statement.executeUpdate();
   		 
   	 }catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
   	 }

   public int maxId() {
	   
	    int maxId = 0;
 	    Connection con = null;
 	    Statement stmt = null;
		try{
		con = ds.getConnection();
		
		String max = "SELECT MAX(product_id) AS ID FROM products";
		
		stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(max);
        
        
        
        while (resultSet.next()) {
        	maxId = resultSet.getInt("ID");
        	
        }
	 }catch (SQLException se) {
		throw new RuntimeException("A database error occured. "
				+ se.getMessage());
		// Clean up JDBC resources
	} finally {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException se) {
				se.printStackTrace(System.err);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		
	 }
		
		return maxId;
	 }
   
}

