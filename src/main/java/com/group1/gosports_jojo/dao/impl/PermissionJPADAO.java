package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.PermissionDAO;
import com.group1.gosports_jojo.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@Repository
public class PermissionJPADAO implements PermissionDAO {

    private static final String GET_ALL_STMT = "SELECT * FROM PERMISSIONS ORDER BY PERMISSION_ID ASC";
    private EntityManager entityManager;

    @Autowired
    public PermissionJPADAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Permission insert(Permission permission) {
       entityManager.persist(permission);
       return permission;
    }

    @Override
    public Permission update(Permission permission) {
        return entityManager.merge(permission);
    }

    @Override
    public void delete(Permission permission) {
        entityManager.remove(permission);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Permission> getAll() {
        return entityManager.createNativeQuery(GET_ALL_STMT, Permission.class).getResultList();
    }
    @Transactional(readOnly = true)
    @Override
    public Permission getById(Integer permissionId) {
        return  entityManager.find(Permission.class, permissionId);
    }
}
