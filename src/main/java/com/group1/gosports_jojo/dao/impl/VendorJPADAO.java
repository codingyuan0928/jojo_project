package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.VendorDAO;
import com.group1.gosports_jojo.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@Repository
public class VendorJPADAO implements VendorDAO {
    private static final String GET_BY_SHOP_NAME_PSTMT="SELECT * FROM VENDORS WHERE shop_name = ?";
    private static final String GET_BY_COMPANY_PHONE_PSTMT="SELECT * FROM VENDORS WHERE company_phone = ?";
    private static final String GET_BY_COMPANY_EMAIL_PSTMT="SELECT * FROM VENDORS WHERE company_email = ?";
    private static final String GET_BY_UNIFIED_BUSINESS_NUMBER_PSTMT="SELECT * FROM VENDORS WHERE unified_business_number = ?";
    private static final String GET_BY_COMPANY_NAME_PSTMT="SELECT * FROM VENDORS WHERE company_name = ?";
    private static final String GET_ALL_PSTMT = "SELECT * FROM VENDORS ORDER BY VENDOR_ID ASC";
    private static final String SOFT_DELETE_PSTMT="UPDATE vendors SET enabled = 0 WHERE vendor_id = ?";
    private static final String GET_BY_EMAIL_PSTMT="SELECT * FROM VENDORS WHERE email = ?";
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

    //軟刪除方法
    @Override
    public void delete(Integer vendorId) {
        Query query = entityManager.createNativeQuery(SOFT_DELETE_PSTMT);
        query.setParameter(1, vendorId);
        query.executeUpdate();
    }

    @Transactional(readOnly = true)
    @Override
    public Vendor findById(int id) {
        return entityManager.find(Vendor.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Vendor> getAll() {
        return entityManager.createNativeQuery(GET_ALL_PSTMT, Vendor.class).getResultList();
    }

    @Override
    public Vendor findByEmail(String email) {
        List<Vendor> result = entityManager.createNativeQuery(GET_BY_EMAIL_PSTMT, Vendor.class)
                .setParameter(1, email)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public Vendor findByCompanyName(String companyName) {
    List<Vendor> result = entityManager.createNativeQuery(GET_BY_COMPANY_NAME_PSTMT, Vendor.class)
            .setParameter(1,companyName)
            .getResultList();
    return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public Vendor findByShopName(String shopName){
    List<Vendor> result = entityManager.createNativeQuery(GET_BY_SHOP_NAME_PSTMT,Vendor.class)
            .setParameter(1,shopName)
            .getResultList();
    return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public Vendor findByCompanyPhone(String companyPhone){
        List<Vendor> result = entityManager.createNativeQuery(GET_BY_COMPANY_PHONE_PSTMT,Vendor.class)
                .setParameter(1,companyPhone)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public Vendor findByUnifiedBusinessNumber(String unifiedBusinessNumber){
        List<Vendor> result = entityManager.createNativeQuery(GET_BY_UNIFIED_BUSINESS_NUMBER_PSTMT,Vendor.class)
                .setParameter(1,unifiedBusinessNumber)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public Vendor findByCompanyEmail(String companyEmail){
        List<Vendor> result = entityManager.createNativeQuery(GET_BY_COMPANY_EMAIL_PSTMT,Vendor.class)
                .setParameter(1,companyEmail)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}
