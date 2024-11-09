package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.UserDAO;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserJDBCDAO implements UserDAO {

    private final DataSource dataSource;

    @Autowired
    public UserJDBCDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String INSERT_PSTMT = "INSERT INTO users (username,password,avatar,email,enabled,provider_name,access_token,refresh_token,access_token_expiry,refresh_token_expiry,newsletter_subscription_consent_field,group_points,interests_tag ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String GET_ALL_PSTMT = "SELECT user_id,username,password,avatar,email,enabled,provider_name,access_token,refresh_token,access_token_expiry,refresh_token_expiry,newsletter_subscription_consent_field,created_at,updated_at,group_points,interests_tag FROM users order by user_id";
    private static final String GET_ONE_PSTMT = "SELECT user_id,username,password,avatar,email,enabled,provider_name,access_token,refresh_token,access_token_expiry,refresh_token_expiry,newsletter_subscription_consent_field,created_at,updated_at,group_points,interests_tag FROM users WHERE user_id = ?";
    private static final String DELETE = "UPDATE users SET enabled=? WHERE user_id = ?";
    private static final String UPDATE = "UPDATE users SET username=?,password=?,avatar=?,email=?,enabled=?,provider_name=?,access_token=?,refresh_token=?,access_token_expiry=?,refresh_token_expiry=?,newsletter_subscription_consent_field=?,group_points=?,interests_tag=? WHERE user_id = ?";
    private static final String GET_BY_EMAIL_PSTMT = "SELECT user_id,username,password,avatar,email,enabled,provider_name,access_token,refresh_token,access_token_expiry,refresh_token_expiry,newsletter_subscription_consent_field,created_at,updated_at,group_points,interests_tag FROM users WHERE email = ?";

    @Override
    public void insert(UserVO userVO) {


        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(INSERT_PSTMT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, userVO.getUsername());
            pstmt.setString(2, userVO.getPassword());
            pstmt.setBytes(3, userVO.getAvatar());
            pstmt.setString(4, userVO.getEmail());
            pstmt.setInt(5, userVO.getEnabled());
            pstmt.setString(6, userVO.getProviderName());
            pstmt.setString(7, userVO.getAccessToken());
            pstmt.setString(8, userVO.getRefreshToken());
            pstmt.setTimestamp(9, userVO.getAccessTokenExpiry());
            pstmt.setTimestamp(10, userVO.getRefreshTokenExpiry());
            pstmt.setInt(11, userVO.getNewsletterSubscriptionConsentField());
            pstmt.setInt(12, userVO.getGroupPoints());
            pstmt.setInt(13, userVO.getInterestsTag());

            int affectedRows= pstmt.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("新增失敗!");
            }
           try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
               if(generatedKeys.next()){
                   userVO.setUserId(generatedKeys.getInt(1));
               }else{
                    throw new SQLException("新增失敗，無法獲取userId");
               }
           }

        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
    }

    @Override
    public boolean update(UserVO userVO) {


        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE)) {


            pstmt.setString(1, userVO.getUsername());
            pstmt.setString(2, userVO.getPassword());
            pstmt.setBytes(3, userVO.getAvatar());
            pstmt.setString(4, userVO.getEmail());
            pstmt.setInt(5, userVO.getEnabled());
            pstmt.setString(6, userVO.getProviderName());
            pstmt.setString(7, userVO.getAccessToken());
            pstmt.setString(8, userVO.getRefreshToken());
            pstmt.setTimestamp(9, userVO.getAccessTokenExpiry());
            pstmt.setTimestamp(10, userVO.getRefreshTokenExpiry());
            pstmt.setInt(11, userVO.getNewsletterSubscriptionConsentField());
            pstmt.setInt(12, userVO.getGroupPoints());
            pstmt.setInt(13, userVO.getInterestsTag());
            pstmt.setInt(14, userVO.getUserId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

    }

    @Override
    public void delete(Integer userId) {


        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {


            pstmt.setInt(1, 0);
            pstmt.setInt(2,userId);

            pstmt.executeUpdate();

        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }

    }

    @Override
    public UserVO findById(Integer userId) {

        UserVO userVO = null;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_ONE_PSTMT)) {


            pstmt.setInt(1, userId);


            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userVO = new UserVO();
                    userVO.setUserId(rs.getInt("user_id"));
                    userVO.setUsername(rs.getString("username"));
                    userVO.setPassword(rs.getString("password"));
                    userVO.setAvatar(rs.getBytes("avatar"));
                    userVO.setEmail(rs.getString("email"));
                    userVO.setEnabled(rs.getInt("enabled"));
                    userVO.setProviderName(rs.getString("provider_name"));
                    userVO.setAccessToken(rs.getString("access_token"));
                    userVO.setRefreshToken(rs.getString("refresh_token"));
                    userVO.setAccessTokenExpiry(rs.getTimestamp("access_token_expiry"));
                    userVO.setRefreshTokenExpiry(rs.getTimestamp("refresh_token_expiry"));
                    userVO.setNewsletterSubscriptionConsentField(rs.getInt("newsletter_subscription_consent_field"));
                    userVO.setCreatedAt(rs.getTimestamp("created_at"));
                    userVO.setUpdatedAt(rs.getTimestamp("updated_at"));
                    userVO.setGroupPoints(rs.getInt("group_points"));
                    userVO.setInterestsTag(rs.getInt("interests_tag"));
                }
            }
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        return userVO;
    }

    @Override
    public List<UserVO> getAll() {
        List<UserVO> list = new ArrayList<UserVO>();
        UserVO userVO = null;

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_ALL_PSTMT)) {

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    userVO = new UserVO();
                    userVO.setUserId(rs.getInt("user_id"));
                    userVO.setUsername(rs.getString("username"));
                    userVO.setPassword(rs.getString("password"));
                    userVO.setAvatar(rs.getBytes("avatar"));
                    userVO.setEmail(rs.getString("email"));
                    userVO.setEnabled(rs.getInt("enabled"));
                    userVO.setProviderName(rs.getString("provider_name"));
                    userVO.setAccessToken(rs.getString("access_token"));
                    userVO.setRefreshToken(rs.getString("refresh_token"));
                    userVO.setAccessTokenExpiry(rs.getTimestamp("access_token_expiry"));
                    userVO.setRefreshTokenExpiry(rs.getTimestamp("refresh_token_expiry"));
                    userVO.setNewsletterSubscriptionConsentField(rs.getInt("newsletter_subscription_consent_field"));
                    userVO.setCreatedAt(rs.getTimestamp("created_at"));
                    userVO.setUpdatedAt(rs.getTimestamp("updated_at"));
                    userVO.setGroupPoints(rs.getInt("group_points"));
                    userVO.setInterestsTag(rs.getInt("interests_tag"));
                    list.add(userVO); // Store the row in the list
                }
            }
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());

        }
        return list;
    }

    @Override
    public UserVO findByEmail(String email) {
        UserVO userVO = null;

        try(Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_BY_EMAIL_PSTMT)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userVO = new UserVO();
                    userVO.setUserId(rs.getInt("user_id"));
                    userVO.setUsername(rs.getString("username"));
                    userVO.setPassword(rs.getString("password"));
                    userVO.setAvatar(rs.getBytes("avatar"));
                    userVO.setEmail(rs.getString("email"));
                    userVO.setEnabled(rs.getInt("enabled"));
                    userVO.setProviderName(rs.getString("provider_name"));
                    userVO.setAccessToken(rs.getString("access_token"));
                    userVO.setRefreshToken(rs.getString("refresh_token"));
                    userVO.setAccessTokenExpiry(rs.getTimestamp("access_token_expiry"));
                    userVO.setRefreshTokenExpiry(rs.getTimestamp("refresh_token_expiry"));
                    userVO.setNewsletterSubscriptionConsentField(rs.getInt("newsletter_subscription_consent_field"));
                    userVO.setCreatedAt(rs.getTimestamp("created_at"));
                    userVO.setUpdatedAt(rs.getTimestamp("updated_at"));
                    userVO.setGroupPoints(rs.getInt("group_points"));
                    userVO.setInterestsTag(rs.getInt("interests_tag"));
                }
            }
        }catch (SQLException se){
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        return userVO;
    }
}
