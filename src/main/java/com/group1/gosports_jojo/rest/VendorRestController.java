package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/vendors")
public class VendorRestController {

    @Autowired
    private VendorService vendorService;


    @PostMapping("/create")
    public ResponseEntity<String> addVendor(@RequestBody Vendor vendor) {
        vendorService.addVendor(vendor);
        return ResponseEntity.ok("Vendor added successfully!");
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable Integer id) {
        Vendor vendor = vendorService.findVendorById(id);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendor);
    }

    // Retrieve all vendors
    @GetMapping("/getAll")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();
        if (vendors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vendors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
         vendorService.deleteVendor(id);
            return ResponseEntity.ok("Vendor deleted successfully!");
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVendor(@PathVariable Integer id, @RequestBody Vendor updatedVendor) throws Exception {
        Vendor existingVendor = vendorService.findVendorById(id);

        if (existingVendor == null) {
            return ResponseEntity.notFound().build();
        }

        // 需要排除的欄位集合
        Set<String> excludeFields = new HashSet<>();
        excludeFields.add("vendorId");
        excludeFields.add("password");
        excludeFields.add("enabled");
        excludeFields.add("createdAt");
        excludeFields.add("updatedAt");
        excludeFields.add("status");

        // 獲取所有欄位
        Field[] fields = Vendor.class.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();

            // 跳過需要排除的欄位
            if (excludeFields.contains(fieldName)) {
                continue;
            }

            // 獲取 getter 和 setter 方法名稱
            String getterName = "get" + capitalize(fieldName);
            String setterName = "set" + capitalize(fieldName);

            // 呼叫 getter 和 setter
            Method getter = Vendor.class.getMethod(getterName);
            Method setter = Vendor.class.getMethod(setterName, field.getType());

            // 從 updatedVendor 取得值
            Object updatedValue = getter.invoke(updatedVendor);

            // 如果值不為 null，則設定到 existingVendor
            if (updatedValue != null) {
                setter.invoke(existingVendor, updatedValue);
            }
        }

        // 保存更新後的 Vendor
        vendorService.updateVendor(existingVendor);

        return ResponseEntity.ok("Vendor updated successfully!");
    }

    private String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println("廠商用戶已登出");
        return ResponseEntity.status(HttpStatus.OK).body("廠商已成功登出");
    }

}


