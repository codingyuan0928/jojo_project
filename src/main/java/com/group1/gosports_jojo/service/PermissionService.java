package com.group1.gosports_jojo.service;

import com.group1.gosports_jojo.entity.Permission;

import java.util.List;

public interface PermissionService {
    Permission addPermission(Permission permission);
    Permission updatePermission(Permission permission);
    void deletePermission(Integer permissionId);
    Permission getById(Integer permissionId);
    List<Permission> getAllPermission();
}
