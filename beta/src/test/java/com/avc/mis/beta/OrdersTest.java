/**
 * 
 */
package com.avc.mis.beta;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dao.Orders;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.process.ContractType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessType;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class OrdersTest {

	@Autowired
	Orders orders;
	
	private ProductionProcess basicOrder() {
		ProductionProcess process = new ProductionProcess();
		PO po = new PO();
		ContractType contractType = new ContractType();
		contractType.setId(1);
		po.setContractType(contractType);
		Supplier supplier = new Supplier();
		supplier.setId(45);
		po.setSupplier(supplier);
		process.setPo(po);
		ProcessType processType = new ProcessType();
		processType.setId(1);
		process.setProcessType(processType);
		process.setDate(new Date(System.currentTimeMillis()));
		process.setTime(new Date(System.currentTimeMillis()));
				
		return process;
	}
	
	@Test
	void ordersTest() {
		//insert an order 
		ProductionProcess process = basicOrder();
		try {
			orders.addOrder(process);
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
}
