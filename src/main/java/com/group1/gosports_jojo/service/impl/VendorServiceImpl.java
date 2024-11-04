package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.UserDAO;
import com.group1.gosports_jojo.dao.VendorDAO;
import com.group1.gosports_jojo.dto.VendorRegisterRequest;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.security.PasswordUtil;
import com.group1.gosports_jojo.service.EmailService;
import com.group1.gosports_jojo.service.RedisService;
import com.group1.gosports_jojo.service.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final VendorDAO dao;
    private final PasswordUtil passwordUtil;
    private final AuthCode authCodeService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Autowired
    public VendorServiceImpl(VendorDAO dao, PasswordUtil passwordUtil, AuthCode authCodeService, RedisService redisService, EmailService emailService) {
        this.dao = dao;
        this.passwordUtil = passwordUtil;
        this.authCodeService = authCodeService;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    @Override
    public void addVendor(Vendor vendor) {
        dao.insert(vendor);
    }

    @Override
    public void updateVendor(Vendor vendor) {
        dao.update(vendor);
    }

    @Override
    public void deleteVendor(Integer vendorId) {
        dao.delete(vendorId);
    }

    @Override
    public Vendor findVendorById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return dao.getAll();
    }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public Vendor findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public Vendor findByCompanyName(String companyName) {return dao.findByCompanyName(companyName);}

    @Override
    public Vendor findByShopName(String shopName) {return dao.findByShopName(shopName);}

    @Override
    public Vendor findByCompanyPhone(String companyPhone){return dao.findByCompanyPhone(companyPhone);}

    @Override
    public Vendor findByUnifiedBusinessNumber(String unifiedBusinessNumber){return dao.findByUnifiedBusinessNumber(unifiedBusinessNumber);}

    @Override
    public Vendor findByCompanyEmail(String companyEmail){return dao.findByCompanyEmail(companyEmail);}

    @Override
    public void updateVendorProfile(){}

    @Override
    public Integer register(VendorRegisterRequest vendorRegisterRequest) {
        String hashedPassword = passwordUtil.encode(vendorRegisterRequest.getPassword());
        // 創建一個 Vendor 實體並填充數據
        Vendor vendor = new Vendor();
        vendor.setUsername(vendorRegisterRequest.getUsername());
        vendor.setPassword(hashedPassword);
        vendor.setEmail(vendorRegisterRequest.getEmail());
        vendor.setCompanyName(vendorRegisterRequest.getCompanyName());
        vendor.setCompanyAddress(vendorRegisterRequest.getCompanyAddress());
        vendor.setCompanyPhone(vendorRegisterRequest.getCompanyPhone());
        vendor.setCompanyEmail(vendorRegisterRequest.getCompanyEmail());
        vendor.setShopName(vendorRegisterRequest.getShopName());
        vendor.setUnifiedBusinessNumber(vendorRegisterRequest.getUnifiedBusinessNumber());

        // 處理 avatar 文件轉換
        MultipartFile avatarFile = vendorRegisterRequest.getAvatar();
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                vendor.setAvatar(avatarFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error processing avatar file", e);
            }
        }

        // 處理 registrationDocument 文件轉換
        MultipartFile registrationDocument = vendorRegisterRequest.getRegistrationDocument();
        if (registrationDocument != null && !registrationDocument.isEmpty()) {
            try {
                vendor.setRegistrationDocument(registrationDocument.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error processing registration document", e);
            }
        }

        // 使用 VendorDAO 插入數據
       dao.insert(vendor);

        // 返回新插入的 Vendor ID
        return vendor.getVendorId();
    }



}
