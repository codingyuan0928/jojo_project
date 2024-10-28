package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.entity.Permission;

import java.util.List;

public interface PermissionDAO {
    Permission insert (Permission permission);
    Permission update (Permission permission);
    void delete (Permission permission);
    List<Permission> getAll();
    Permission getById(Integer permissionId);
}
