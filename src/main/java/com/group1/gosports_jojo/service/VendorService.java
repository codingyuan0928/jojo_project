package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.dto.VendorProfileUpdateRequest;
import com.group1.gosports_jojo.dto.VendorRegisterRequest;
import com.group1.gosports_jojo.entity.Vendor;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface VendorService {
    void addVendor(Vendor vendor);
    void updateVendor(Vendor vendor);
    void deleteVendor(Integer vendorId);
    Vendor findVendorById(Integer vendorId);
    List<Vendor> getAllVendors();
    Vendor findByEmail(String email);
    Integer register(VendorRegisterRequest vendorRegisterRequest);
    Vendor findByCompanyName(String companyName);
    Vendor findByShopName(String shopName);
    Vendor findByCompanyPhone(String phoneNumber);
    Vendor findByUnifiedBusinessNumber(String phoneNumber);
    Vendor findByCompanyEmail(String companyEmail);
    void updateVendorProfile(VendorProfileUpdateRequest vendorProfileUpdateRequest, Vendor vendor,HttpSession session);
}
