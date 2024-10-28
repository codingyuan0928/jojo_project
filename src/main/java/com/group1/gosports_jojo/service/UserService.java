package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.dto.UserLoginRequest;
import com.group1.gosports_jojo.dto.UserProfileUpdateRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.model.UserVO;

import java.sql.Timestamp;
import java.util.List;

public interface UserService {

    UserVO addUser(String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag);

    UserVO updateUser(Integer userId, String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag);

    void deleteUser(Integer userId);

    UserVO getOneUser(Integer userId);

    List<UserVO> getAll();

    Integer register(UserRegisterRequest userRegisterRequest);

    UserVO login(UserLoginRequest userLoginRequest);

    void generateAndSendAuthCode(String email);

    boolean validateAuthCode(String email, String authCode);

    void updateUserProfile(Integer userId, UserProfileUpdateRequest userProfileUpdateRequest);
}
