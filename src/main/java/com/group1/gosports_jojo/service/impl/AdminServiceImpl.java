package com.group1.gosports_jojo.service.impl;
import com.group1.gosports_jojo.security.PasswordUtil;
import com.group1.gosports_jojo.dao.AdministratorDAO;
import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.entity.Administrator;
import com.group1.gosports_jojo.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminServiceImpl implements AdminService {

    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private AdministratorDAO administratorDAO;

    @Override
    public Administrator login(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.getEmail();
        String password = authLoginRequest.getPassword();
        Administrator administrator = administratorDAO.getByEmail(email);
        if (administrator == null) {
            log.warn("該 email: {} 尚未註冊", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該 email 尚未註冊");
        }
        if (passwordUtil.matches(password, administrator.getPassword())) {
            return administrator;
        } else {
            log.warn("email: {} 的密碼不正確", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼不正確");
        }
    }

    @Override
    public boolean updateAdminName(String newUsername, Administrator administrator) {
        administrator.setUsername(newUsername);
        return administratorDAO.update(administrator);
    }

    @Override
    public Administrator findByEmail(String email) {
        return administratorDAO.getByEmail(email);
    }
}
