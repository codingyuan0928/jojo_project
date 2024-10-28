package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.VendorDAO;
import com.group1.gosports_jojo.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@Repository
public class VendorJPADAO implements VendorDAO {

    private static final String GET_ALL_STMT = "SELECT * FROM VENDORS ORDER BY VENDOR_ID ASC";


    private EntityManager entityManager;

    @Autowired
    public VendorJPADAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void insert(Vendor vendor) {
        entityManager.persist(vendor);
    }


    @Override
    public void update(Vendor vendor) {
        entityManager.merge(vendor);
    }


    @Override
    public void delete(Vendor vendor) {
        entityManager.remove(entityManager.contains(vendor) ? vendor : entityManager.merge(vendor));
    }

    @Transactional(readOnly = true)
    @Override
    public Vendor findById(int id) {
        return entityManager.find(Vendor.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Vendor> getAll() {
        return entityManager.createNativeQuery(GET_ALL_STMT, Vendor.class).getResultList();
    }
}
