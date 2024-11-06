package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.entity.Administrator;

public interface AdminService {
    Administrator login(AuthLoginRequest authLoginRequest);
    boolean updateAdminName(String newUsername,Administrator administrator);
}
