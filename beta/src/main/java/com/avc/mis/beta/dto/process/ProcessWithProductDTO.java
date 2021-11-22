/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;

import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Abstract DTO for processes that add/produce items to inventory.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class ProcessWithProductDTO<T extends ProcessItemDTO> extends PoProcessDTO {
	
	private List<T> processItems;
	
	@Override
	public ProcessWithProduct<? extends ProcessItem> fillEntity(Object entity) {
		ProcessWithProduct<? extends ProcessItem> processWithProduct;
		if(entity instanceof ProcessWithProduct) {
			processWithProduct = (ProcessWithProduct<? extends ProcessItem>) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessWithProduct class");
		}
		super.fillEntity(processWithProduct);
		
		return processWithProduct;
	}


}
