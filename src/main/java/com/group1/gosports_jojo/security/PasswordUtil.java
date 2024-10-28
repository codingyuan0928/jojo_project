package com.group1.gosports_jojo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private  final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordUtil(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public  String encode(String rawpassword) {
        return passwordEncoder.encode(rawpassword);
    }

    public  boolean matches(String rawpassword, String encodedpassword) {
        return passwordEncoder.matches(rawpassword, encodedpassword);
    }
}
