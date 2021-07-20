package com.avc.mis.beta.controllers;

import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.report.OrderReports;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/schedule")
public class ScheduleController {

	
	@Autowired
	private OrderReports orderReports;
	
	@RequestMapping("/getAllCashewOrders")
	public List<PoItemRow> getAllCashewOrders(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return orderReports.findAllCashewOrderItems(begin != null? begin.toLocalDate() : null, end != null? end.toLocalDate() : null);
	}
	
	@RequestMapping("/getAllGeneralOrders")
	public List<PoItemRow> getAllGeneralOrders(@QueryParam("begin")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime begin, 
			@QueryParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return orderReports.findAllGeneralOrderItems(begin != null? begin.toLocalDate() : null, end != null? end.toLocalDate() : null);
	}
}
