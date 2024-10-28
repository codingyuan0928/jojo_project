package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.VendorDAO;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorDAO vendorDAO;

    @Autowired
    public VendorServiceImpl(VendorDAO vendorDAO) {
        this.vendorDAO = vendorDAO;
    }

    @Override
    public void addVendor(Vendor vendor) {
        vendorDAO.insert(vendor);
    }

    @Override
    public void updateVendor(Vendor vendor) {
        vendorDAO.update(vendor);
    }

    @Override
    public void deleteVendor(Vendor vendor) {
        vendorDAO.delete(vendor);
    }

    @Override
    public Vendor findVendorById(int id) {
        return vendorDAO.findById(id);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorDAO.getAll();
    }
}
