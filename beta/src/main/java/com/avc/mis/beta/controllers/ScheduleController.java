package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.service.Orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public List<PoItemRow> getGeneralOrdersOpen() {
		return ordersDao.findAllGeneralOrderItems();
	}
}
