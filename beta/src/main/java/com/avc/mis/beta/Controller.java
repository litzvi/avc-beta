/**
 * 
 */
package com.avc.mis.beta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dao.Suppliers;

/**
 * @author Zvi
 *
 */
@RestController
public class Controller {
	
	@Autowired
	private Suppliers suppliersDao;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@RequestMapping("/suppliers")
	public List<String> suppliers() {
		System.out.println(suppliersDao.getSuppliersList());
		return suppliersDao.getSuppliersList();
	}

	@RequestMapping("/supplier-details")
	public String supplierDetails() {
		System.out.println(suppliersDao.getSupplierDetails());
		return suppliersDao.getSupplierDetails();
	}
	
}
