package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.entity.Vendor;

import java.util.List;

public interface VendorDAO {
    void insert(Vendor vendor);
    void update(Vendor vendor);
    void delete(Vendor vendor);
    Vendor findById(int id);
    List<Vendor> getAll();
}
