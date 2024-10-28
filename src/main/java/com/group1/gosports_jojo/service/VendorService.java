package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.entity.Vendor;

import java.util.List;

public interface VendorService {
    void addVendor(Vendor vendor);
    void updateVendor(Vendor vendor);
    void deleteVendor(Vendor vendor);
    Vendor findVendorById(int id);
    List<Vendor> getAllVendors();
}
