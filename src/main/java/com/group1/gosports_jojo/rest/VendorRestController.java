package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.dto.VendorProfileUpdateRequest;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.service.VendorService;
import com.group1.gosports_jojo.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/vendors")
public class VendorRestController {
    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private VendorService vendorServiceImpl;


    @PostMapping("/create")
    public ResponseEntity<String> addVendor(@RequestBody Vendor vendor) {
        vendorServiceImpl.addVendor(vendor);
        return ResponseEntity.ok("Vendor added successfully!");
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable Integer id) {
        Vendor vendor = vendorServiceImpl.findVendorById(id);
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendor);
    }

    // Retrieve all vendors
    @GetMapping("/getAll")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorServiceImpl.getAllVendors();
        if (vendors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vendors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
        vendorServiceImpl.deleteVendor(id);
            return ResponseEntity.ok("Vendor deleted successfully!");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateVendor(@Valid @ModelAttribute VendorProfileUpdateRequest vendorProfileUpdateRequest,
                                          BindingResult vendorResult, HttpSession session) {
        Map<String, String> errors = new HashMap<>();
        if (vendorResult.hasErrors()) {
            vendorResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        }

        Vendor currentVendor = (Vendor) session.getAttribute("vendorAccount");
        Integer currentVendorId = currentVendor.getVendorId();

        // 檢查公司名稱重複，但排除當前記錄
        Vendor existingVendor = vendorServiceImpl.findByCompanyName(vendorProfileUpdateRequest.getCompanyName());
        if (existingVendor != null && !existingVendor.getVendorId().equals(currentVendorId)) {
            log.warn("該公司名稱: {} 已經註冊", vendorProfileUpdateRequest.getCompanyName());
            errors.put("companyName", "公司名稱已經被註冊");
        }

        // 檢查商店名稱重複，但排除當前記錄
        existingVendor = vendorServiceImpl.findByShopName(vendorProfileUpdateRequest.getShopName());
        if (existingVendor != null && !existingVendor.getVendorId().equals(currentVendorId)) {
            log.warn("該商店名稱: {} 已經註冊", vendorProfileUpdateRequest.getShopName());
            errors.put("shopName", "商店名稱已經被註冊");
        }

        // 檢查公司電話重複，但排除當前記錄
        existingVendor = vendorServiceImpl.findByCompanyPhone(vendorProfileUpdateRequest.getCompanyPhone());
        if (existingVendor != null && !existingVendor.getVendorId().equals(currentVendorId)) {
            log.warn("該公司電話: {} 已經被使用", vendorProfileUpdateRequest.getCompanyPhone());
            errors.put("companyPhone", "公司電話已經被使用");
        }

        // 檢查統一編號重複，但排除當前記錄
        existingVendor = vendorServiceImpl.findByUnifiedBusinessNumber(vendorProfileUpdateRequest.getUnifiedBusinessNumber());
        if (existingVendor != null && !existingVendor.getVendorId().equals(currentVendorId)) {
            log.warn("該統一編號: {} 已經註冊", vendorProfileUpdateRequest.getUnifiedBusinessNumber());
            errors.put("unifiedBusinessNumber", "統一編號已經被註冊");
        }

        // 檢查公司 EMAIL 重複，但排除當前記錄
        existingVendor = vendorServiceImpl.findByCompanyEmail(vendorProfileUpdateRequest.getCompanyEmail());
        if (existingVendor != null && !existingVendor.getVendorId().equals(currentVendorId)) {
            log.warn("該公司 EMAIL: {} 已經註冊", vendorProfileUpdateRequest.getCompanyEmail());
            errors.put("companyEmail", "公司 EMAIL 已經被註冊");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        vendorServiceImpl.updateVendorProfile(vendorProfileUpdateRequest, currentVendor,session);
        log.info("廠商資料更新成功!");

        return ResponseEntity.ok("廠商資料更新成功!");
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println("廠商用戶已登出");
        return ResponseEntity.status(HttpStatus.OK).body("廠商用戶已成功登出");
    }

}


