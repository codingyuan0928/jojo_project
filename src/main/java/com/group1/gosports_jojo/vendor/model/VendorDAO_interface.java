package com.group1.gosports_jojo.vendor.model;

import java.util.*;

public interface VendorDAO_interface {

	//查詢單一廠商圖檔
	public VendorVO getOneVendorImage(Integer vendorId);
	
	//查詢所有廠商(後臺管理員審查)
	public List<VendorVO> getAllVendor();
	
	//查詢近30分鐘，新註冊的廠商名單
	public List<VendorVO> getNewVendor();
	

	//查詢審核狀態為0(未審核)或2(審核為通過)的廠商(後臺管理員審查)
	public List<VendorVO> getPendingVendor();

	// 更新審核狀態，審核未通過(改為2) (審核未通過或被檢舉停權)
	public void updateHidden(Integer vendorId);

	// 更新審核狀態，審核通過(改為1)
	public void updatePass(Integer vendorId);

	//查詢近30分鐘，廠商狀態更新為2的名單
	public List<VendorVO> getNotPassVendor();
	
	// 設定廠商停權(使enabled=0)
	public void updateEnabled0(Integer vendorId);
	
}
