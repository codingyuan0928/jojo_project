package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.entity.Permission;
import com.group1.gosports_jojo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionRestController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Permission>> getAll() {
    permissionService.getAllPermission();
    return ResponseEntity.ok(permissionService.getAllPermission());
    }
}
