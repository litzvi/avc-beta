/**
 * 
 */
package com.avc.mis.beta;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.collection.ApprovalTaskDTO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
import com.avc.mis.beta.service.ProcessReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Zvi
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WithUserDetails("eli")
public class OrdersTest {
	
	public static final int NUM_ITEMS = 3;
	
	public int PROCESS_NO = LocalDateTime.now().hashCode();

	@Autowired TestService service;
	@Autowired Orders orders;
	@Autowired private ProcessReader processReader;
	@Autowired private ProcessInfoReader processInfoReader;
	@Autowired private ProcessInfoWriter processInfoWriter;

	
//	@Disabled
	@Test
	void ordersTest() {
		
		//insert an order 
		PO po = service.addBasicCashewOrder();
		PoDTO expected = null;
		expected = new PoDTO(po);
		PoDTO actual = orders.getOrder(po.getPoCode().getId());
		assertEquals(expected, actual, "failed test adding po");

		//edit order status
		po.setDuration(Duration.ofHours(15));
		expected = new PoDTO(po);
		try {
			orders.editOrder(po);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw e2;
		}
		actual = orders.getOrder(po.getPoCode().getId());
		assertEquals(expected, actual, "failed test editing po order status");		
		
		//cleanup
		service.cleanup(po);

		//remove a line/item from order
		po = service.addBasicCashewOrder();
		OrderItem[] oldItems = po.getOrderItems();
		OrderItem[] items = new OrderItem[oldItems.length - 1];
		IntStream.range(0, items.length).forEach(i -> items[i] = oldItems[i]);
		po.setOrderItems(items);;
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrderByProcessId(po.getId());	
		assertEquals(expected, actual, "failed test editing po order status");
				
		//get list approval tasks for user
		List<ApprovalTaskDTO> tasks;
		ObjectMapper objMapper = new ObjectMapper();
		tasks = processInfoReader.getAllRequiredApprovals(null, null);

		try {
			processInfoWriter.setProcessStatus(ProcessStatus.FINAL, po.getId());
			fail("Should not be able to change to final before approved");
		} catch (Exception e1) {}		
		try {
			tasks.forEach(t -> {
				PoProcessDTO p = processReader.getProcess(t.getProcessId(), t.getProcessName());
				String processSnapshot = null;
				try {
					processSnapshot = objMapper.writeValueAsString(p);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				processInfoWriter.setApprovalDecision(t.getId(), 
						DecisionType.APPROVED, processSnapshot, null);
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		//cleanup
		service.cleanup(po);

	}	
	
	@Test
	void generalOrdersTest() {
		PO po;
		try {
			po = service.addBasicGeneralOrder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		PoDTO expected = new PoDTO(po);
		PoDTO actual = orders.getOrder(po.getPoCode().getId());
		assertEquals(expected, actual, "failed test adding po");
	}
}
