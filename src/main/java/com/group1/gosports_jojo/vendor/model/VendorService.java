package com.group1.gosports_jojo.vendor.model;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("VendorService")
public class VendorService {

	private VendorDAO_interface dao;

	@Autowired
	public VendorService(VendorDAO_interface dao) {
//		dao = new VendorDAO();
		this.dao = dao;
		
	}
	
	//查詢單一廠商圖檔
	public VendorVO getOneVendorImage(Integer vendorId) {
		return dao.getOneVendorImage(vendorId);
	}
	
	// 查詢所有廠商
	public List<VendorVO> getAllVendor() {
		return dao.getAllVendor();
	}
	
	////查詢近30分鐘，新註冊的廠商名單
	public List<VendorVO> getNewVendor(){
		return dao.getNewVendor();
	};

	// 查詢審核狀態為0(未審核)或2(審核為通過)的廠商
	public List<VendorVO> getPendingVendor() {
		return dao.getPendingVendor();
	}

	// 更新審核狀態，審核通過(改為1)
	public void updatePass(Integer vendorId) {
		dao.updatePass(vendorId);
	}

	// 更新審核狀態，審核未通過(改為2)
	public void updateHidden(Integer vendorId) {
		dao.updateHidden(vendorId);
	}
	
	
	//查詢近30分鐘，廠商狀態更新為2的名單
	public List<VendorVO> getNotPassVendor(){
		return dao.getNotPassVendor();
	}
	
	// 更新帳號狀態，使廠商停權
	public void updateEnabled0(Integer vendorId) {
		dao.updateEnabled0(vendorId);
		
	}

}
