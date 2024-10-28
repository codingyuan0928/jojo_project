package com.group1.gosports_jojo.service.impl;


import com.group1.gosports_jojo.dao.UserDAO;
import com.group1.gosports_jojo.dto.UserLoginRequest;
import com.group1.gosports_jojo.dto.UserProfileUpdateRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.security.PasswordUtil;
import com.group1.gosports_jojo.service.EmailService;
import com.group1.gosports_jojo.service.RedisService;
import com.group1.gosports_jojo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO dao;
    private final PasswordUtil passwordUtil;
    private final AuthCode authCodeService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserDAO dao, PasswordUtil passwordUtil, AuthCode authCodeService, RedisService redisService, EmailService emailService) {
        this.dao = dao;
        this.passwordUtil = passwordUtil;
        this.authCodeService = authCodeService;
        this.redisService = redisService;
        this.emailService = emailService;
    }


    public UserVO addUser(String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag) {

        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setAvatarBytes(avatar);
        userVO.setEmail(email);
        userVO.setEnabled(enabled);
        userVO.setProviderName(providerName);
        userVO.setAccessToken(accessToken);
        userVO.setRefreshToken(refreshToken);
        userVO.setAccessTokenExpiry(accessTokenExpiry);
        userVO.setRefreshTokenExpiry(refreshTokenExpiry);
        userVO.setNewsletterSubscriptionConsentField(newsletterSubscriptionConsentField);
        userVO.setGroupPoints(groupPoints);
        userVO.setInterestsTag(interestsTag);
        dao.insert(userVO);
        return userVO;
    }

    public UserVO updateUser(Integer userId, String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag) {
        UserVO userVO = new UserVO();

        userVO.setUserId(userId);
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setAvatarBytes(avatar);
        userVO.setEmail(email);
        userVO.setEnabled(enabled);
        userVO.setProviderName(providerName);
        userVO.setAccessToken(accessToken);
        userVO.setRefreshToken(refreshToken);
        userVO.setAccessTokenExpiry(accessTokenExpiry);
        userVO.setRefreshTokenExpiry(refreshTokenExpiry);
        userVO.setNewsletterSubscriptionConsentField(newsletterSubscriptionConsentField);
        userVO.setGroupPoints(groupPoints);
        userVO.setInterestsTag(interestsTag);
        dao.update(userVO);
        return userVO;
    }

    public void deleteUser(Integer userId) {
        dao.delete(userId);
    }

    public UserVO getOneUser(Integer userId) {
        return dao.findById(userId);
    }

    public List<UserVO> getAll() {
        return dao.getAll();
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查註冊的email
        UserVO userVO = dao.findByEmail(userRegisterRequest.getEmail());
        if (userVO != null) {
            log.warn("該email: {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("該 email: %s 已經被註冊", userRegisterRequest.getEmail()));
        }

        //使用bcrypt生成雜湊值
        String hashedPassword = passwordUtil.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        return dao.createUser(userRegisterRequest);
    }

    @Override
    public UserVO login(UserLoginRequest userLoginRequest) {
        UserVO user = dao.findByEmail(userLoginRequest.getEmail());

        //檢查user是否存在
        if (user == null) {
            log.warn("該email: {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //比較密碼(輸入密碼，資料庫內密碼)
        if (passwordUtil.matches(userLoginRequest.getPassword(),user.getPassword())) {
            return user;
        } else {
            log.warn("email: {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void generateAndSendAuthCode(String email) {
        String authCode = authCodeService.returnAuthCode();
        redisService.setAuthCode(email, authCode, 180);
        String messageText = "親愛的用戶您好:\n" +
                "您的email驗證碼為: " + authCode + "\n" +
                "請於三分鐘內完成驗證避免逾時，謝謝~";
        emailService.sendMail(email,"GoSports用戶信箱驗證",messageText);
    }

    @Override
    public boolean validateAuthCode(String email, String authCode) {
        String storedAuthCode = redisService.getAuthCode(email);
        return storedAuthCode != null && storedAuthCode.equals(authCode);
    }

    //會員頁面更新資料
    @Override
    public void updateUserProfile(Integer userId, UserProfileUpdateRequest userProfileUpdateRequest) {
        UserVO userVO = dao.findById(userId);

        if (userVO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userVO.setUsername(userProfileUpdateRequest.getUsername());
        userVO.setAvatarBytes(userProfileUpdateRequest.getAvatar());
        userVO.setInterestsTag(userProfileUpdateRequest.getInterestsTag());

        dao.update(userVO);
    }

}
