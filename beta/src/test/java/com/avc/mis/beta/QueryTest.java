/**
 * 
 */
package com.avc.mis.beta;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.values.ReceiptRow;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.OrderReceipts;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.Users;
import com.avc.mis.beta.service.ValueTablesReader;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class QueryTest {
	
	@Autowired ObjectTablesReader objectTablesReader;
	@Autowired ValueTablesReader valueTablesReader;
	@Autowired Users users;
	@Autowired Orders orders;
	@Autowired OrderReceipts receipts;
	
	@Test
	void queryTest() {
		//get list of cashew orders
//		List<PoCodeDTO> openCashewOrdersBasic =  objectTablesReader.findOpenCashewOrdersPoCode();
//		for(PoCodeDTO row: openCashewOrdersBasic)
//			System.out.println(row);
		
		//get list of cashew orders and receipts
//		List<PoCodeDTO> activeCashewBasic =  objectTablesReader.findActiveCashewPoCode();
//		for(PoCodeDTO row: activeCashewBasic)
//			System.out.println(row);
		
		//list of bank branches
//		List<BankBranchDTO> branchList = valueTablesReader.getAllBankBranchesDTO();
//		branchList.forEach((i)->System.out.println(i));
		
		//get list of cities
//		List<CityDTO> cityList =  valueTablesReader.getAllCitiesDTO();
//		for(CityDTO city: cityList)
//			System.out.println(city);
		
		//get list of persons basic
//		List<PersonBasic> personsBasic = users.getPersonsBasic();
//		personsBasic.forEach(m -> System.out.println(m));
		
		//get order by po code
//		List<PoCodeDTO> activeCashewBasic =  objectTablesReader.findActiveCashewPoCode();
//		if(activeCashewBasic.isEmpty()) {
//			fail("Couldn't test fetching purchase order by po code, no active orders");
//		}
//		System.out.println(orders.getOrder(activeCashewBasic.get(0).getId()));
		
		//find table of open cashew orders
//		orders.findOpenCashewOrders().forEach(i -> System.out.println(i));
		
		//get order by process id
//		List<PoRow> poRows =  orders.findOpenCashewOrders();
//		if(poRows.isEmpty()) {
//			fail("Couldn't test fetching purchase order by process id, no open orders");
//		}
//		System.out.println(orders.getOrderByProcessId(poRows.get(0).getId()));
		
		//get list of cashew items
//		valueTablesReader.getCashewitemsBasic().forEach(i -> System.out.println(i));
		
		//print received orders
		List<ReceiptRow> receiptRows = receipts.findCashewReceipts();
		receiptRows.forEach(r -> System.out.println(r));
				
		
	}
}
