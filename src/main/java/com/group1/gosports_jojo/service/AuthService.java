package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.dto.VendorRegisterRequest;

public interface AuthService {
    Object login(AuthLoginRequest authLoginRequest);
     boolean accountExists(String email);
    Integer registerUser(UserRegisterRequest userRegisterRequest);
    Integer registerVendor(VendorRegisterRequest vendorRegisterRequest);
    void generateAndSendAuthCode(String email);
    boolean validateAuthCode(String email, String authCode);

}
