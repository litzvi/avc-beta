/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.collection.ReceiptItemDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying Receipt entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ReceiptDTO extends ProcessWithProductDTO<ReceiptItemDTO> {
	
	@EqualsAndHashCode.Exclude
	private Integer referencedOrder;
	
	public ReceiptDTO(@NonNull Receipt receipt) {
		super(receipt);
		setReceiptItems(receipt.getReceiptItems().stream()
				.map(i->{return new ReceiptItemDTO((ReceiptItem) i);}).collect(Collectors.toList()));

	}
	
	/**
	 * Always null for Receipt - use getReceiptItems instead
	 */
	@JsonIgnore
	@Override
	public List<ReceiptItemDTO> getProcessItems() {
		return null;
	}
	
	public List<ReceiptItemDTO> getReceiptItems() {
		return super.getProcessItems();
	}
	
	public void setReceiptItems(List<ReceiptItemDTO> receiptItems) {
		super.setProcessItems(receiptItems);
		this.referencedOrder = receiptItems.stream().findAny().map(i -> i.getReferencedOrder()).orElse(null);
	}

	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Receipt.class;
	}

	@Override
	public Receipt fillEntity(Object entity) {
		Receipt receipt;
		if(entity instanceof Receipt) {
			receipt = (Receipt) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Receipt class");
		}
		super.fillEntity(receipt);
		if(getReceiptItems() == null || getReceiptItems().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one received item");
		}
		else {
			Ordinal.setOrdinals(getReceiptItems());
			receipt.setReceiptItems(getReceiptItems().stream().map(i -> i.fillEntity(new ReceiptItem())).collect(Collectors.toSet()));
		}
		
		return receipt;
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}

	

}
