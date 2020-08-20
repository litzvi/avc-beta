package com.avc.mis.beta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.dto.view.PoRow;
import com.avc.mis.beta.service.Orders;

@RestController
@RequestMapping(path = "/api/schedule")
public class ScheduleController {

	
	@Autowired
	private Orders ordersDao;
	
	@RequestMapping("/getCashewOrdersOpen")
	public List<PoItemRow> getCashewOrdersOpen() {
		return ordersDao.findAllCashewOrderItems();
	}
	
	@RequestMapping("/getGeneralOrdersOpen")
	public List<PoRow> getGeneralOrdersOpen() {
		return ordersDao.findAllGeneralOrders();
	}
}
