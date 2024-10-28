package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.impl.PermissionJPADAO;
import com.group1.gosports_jojo.entity.Permission;
import com.group1.gosports_jojo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionJPADAO permissionDAO;

    @Autowired
    public PermissionServiceImpl(PermissionJPADAO permissionDAO) {
        this.permissionDAO = permissionDAO;
    }

    @Override
    public Permission addPermission(Permission permission) {
        permissionDAO.insert(permission);
        return permission;
    }

    @Override
    public Permission updatePermission(Permission permission) {
        return permissionDAO.update(permission);
    }

    @Override
    public void deletePermission(Integer permissionId) {
        permissionDAO.delete( permissionDAO.getById((permissionId)));
    }

    @Override
    public Permission getById(Integer permissionId) {
        return permissionDAO.getById(permissionId);
    }

    @Override
    public List<Permission> getAllPermission() {
        return permissionDAO.getAll();
    }
}
