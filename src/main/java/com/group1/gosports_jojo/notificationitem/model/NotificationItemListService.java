package com.group1.gosports_jojo.notificationitem.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NotificationItemListService")
public class NotificationItemListService {

	private NotificationItemListDAO_interface dao;

	
	@Autowired
	public NotificationItemListService(NotificationItemListDAO_interface dao) {
//		dao = new NotificationItemListDAO();
		this.dao = dao;
	}

	public List<NotificationItemListVO> getAll() {
		return dao.getAll();
	}

}
