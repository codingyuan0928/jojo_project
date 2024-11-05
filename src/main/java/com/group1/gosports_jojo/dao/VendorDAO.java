package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.entity.Vendor;

import java.util.List;

public interface VendorDAO {
    void insert(Vendor vendor);
    void update(Vendor vendor);
    void delete(Integer vendorId);
    Vendor findById(int id);
    List<Vendor> getAll();
    Vendor findByEmail(String email);
    Vendor findByCompanyName(String companyName);
    Vendor findByShopName(String shopName);
    Vendor findByCompanyPhone(String companyPhone);
    Vendor findByUnifiedBusinessNumber(String unifiedBusinessNumber);
    Vendor findByCompanyEmail(String companyEmail);
}
