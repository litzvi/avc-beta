/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.service.Orders;
import com.avc.mis.beta.service.ProcessInfoReader;
import com.avc.mis.beta.service.ProcessInfoWriter;
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
	@Autowired private ProcessInfoReader processDisplay;
	@Autowired private ProcessInfoWriter processInfoWriter;

	
//	@Disabled
	@Test
	void ordersTest() {
		
		//insert an order 
		PO po = service.addBasicCashewOrder();
		PoDTO expected = null;
		expected = new PoDTO(po);
		PoDTO actual = orders.getOrder(po.getPoCode().getCode());
		assertEquals(expected, actual, "failed test adding po");

		//edit order status
		po.setOrderStatus(OrderStatus.OPEN_APPROVED);
		expected = new PoDTO(po);
		orders.editOrder(po);
		actual = orders.getOrder(po.getPoCode().getCode());
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
		tasks = processDisplay.getAllRequiredApprovals();

		tasks.forEach(t -> {
			ProductionProcessDTO p = processDisplay.getProcess(t.getProcessId(), t.getProcessName().name());
			String processSnapshot = null;
			try {
				processSnapshot = objMapper.writeValueAsString(p);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processInfoWriter.setProcessDecision(t.getId(), 
					DecisionType.APPROVED.name(), processSnapshot, null);
			
		});
		
		//cleanup
		service.cleanup(po);

	}	
}
