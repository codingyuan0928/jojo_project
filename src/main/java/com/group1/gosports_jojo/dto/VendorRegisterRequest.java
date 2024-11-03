package com.group1.gosports_jojo.dto;


import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class VendorRegisterRequest {
 @NotBlank(message = "使用者名稱不能空白")
 @Size(max = 255, message = "使用者名稱不能超過85個中文字，請填寫正確中文名稱")
 private String username;

 @Pattern(regexp = "^[a-zA-Z0-9!@#\\$%\\^&\\*]{6,20}$", message = "密碼必須是6到20碼的英文字母、數字或特殊符號")
 @NotBlank(message = "密碼不能空白")
 private String password;

 private MultipartFile avatar;

 @Size(max = 50, message = "Email 長度不能超過 50 字")
 @NotBlank(message = "Email不能空白")
 @Email(message = "Email格式不正確")
 private String email;

 @Size(max = 50, message = "公司名稱長度不能超過 50 字")
 @NotBlank(message = "公司名稱不能空白")
 private String companyName;

 @NotBlank(message = "公司地址不能空白")
 @Size(max = 255, message = "公司地址不能超過85個中文字，請填寫正確中文名稱")
 private String companyAddress;

 @Pattern(regexp = "^(\\+?886\\-?)?\\d{10}$", message = "公司電話格式不正確，請填寫10位數的臺灣公司電話")
 private String companyPhone;

 @Size(max = 50, message = "公司信箱長度不能超過 50 字元")
 @NotBlank(message = "公司信箱不能空白")
 @Email(message = "公司信箱格式不正確")
 private String companyEmail;

 private MultipartFile registrationDocument;

 @Size(max = 50, message = "商城名稱長度不能超過 50 字")
 @NotBlank(message = "商城名稱不能空白")
 private String shopName;

 @Pattern(regexp = "^[0-9]{8}$", message = "統一編號格式不正確，請輸入8位數字的臺灣統一編號")
 private String unifiedBusinessNumber;

 public  String getUsername() {
  return username;
 }

 public void setUsername(String username) {
  this.username = username;
 }

 public  String getPassword() {
  return password;
 }

 public void setPassword( String password) {
  this.password = password;
 }

 public MultipartFile getAvatar() {
  return avatar;
 }

 public void setAvatar(MultipartFile avatar) {
  this.avatar = avatar;
 }

 public  String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getCompanyName() {
  return companyName;
 }

 public void setCompanyName(String companyName) {
  this.companyName = companyName;
 }

 public String getCompanyAddress() {
  return companyAddress;
 }

 public void setCompanyAddress(String companyAddress) {
  this.companyAddress = companyAddress;
 }

 public String getCompanyPhone() {
  return companyPhone;
 }

 public void setCompanyPhone(String companyPhone) {
  this.companyPhone = companyPhone;
 }

 public String getCompanyEmail() {
  return companyEmail;
 }

 public void setCompanyEmail(String companyEmail) {
  this.companyEmail = companyEmail;
 }

 public MultipartFile getRegistrationDocument() {
  return registrationDocument;
 }

 public void setRegistrationDocument(MultipartFile registrationDocument) {
  this.registrationDocument = registrationDocument;
 }



 public String getShopName() {
  return shopName;
 }

 public void setShopName(String shopName) {
  this.shopName = shopName;
 }

 public String getUnifiedBusinessNumber() {
  return unifiedBusinessNumber;
 }

 public void setUnifiedBusinessNumber(String unifiedBusinessNumber) {
  this.unifiedBusinessNumber = unifiedBusinessNumber;
 }
}

