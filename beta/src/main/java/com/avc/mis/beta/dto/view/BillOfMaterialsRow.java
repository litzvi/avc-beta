/**
 * 
 */
package com.avc.mis.beta.dto.view;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class BillOfMaterialsRow extends BasicDTO {

	private BasicValueEntity<Item> product;
	private AmountWithUnit defaultBatch;
	
	public BillOfMaterialsRow(Integer id, Integer productId, String productValue, AmountWithUnit defaultBatch) {
		super(id);
		this.product = new BasicValueEntity<Item>(productId, productValue);
		this.defaultBatch = defaultBatch;
	} 

}
