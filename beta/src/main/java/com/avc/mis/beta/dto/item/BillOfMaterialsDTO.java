/**
 * 
 */
package com.avc.mis.beta.dto.item;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BillOfMaterials;
import com.avc.mis.beta.entities.item.BomLine;
import com.avc.mis.beta.entities.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BillOfMaterialsDTO extends LinkDTO {

	private BasicValueEntity<Item> product;
	private AmountWithUnit defaultBatch;
	
	private List<BomLineDTO> bomList;

	public BillOfMaterialsDTO(Integer id, Integer productId, String productValue, AmountWithUnit defaultBatch) {
		super(id);
		this.product = new BasicValueEntity<Item>(productId, productValue);
		this.defaultBatch = defaultBatch;
	} 
	
	public BillOfMaterialsDTO(BillOfMaterials billOfMaterials) {
		super(billOfMaterials);
		if(billOfMaterials.getProduct() != null)
			this.product = new BasicValueEntity<Item>(billOfMaterials.getProduct());
		if(billOfMaterials.getDefaultBatch() != null)
			this.defaultBatch = billOfMaterials.getDefaultBatch().clone();
		if(billOfMaterials.getBomList() != null) {
			this.bomList = billOfMaterials.getBomList().stream()
					.map(i -> new BomLineDTO(i))
					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList());		
		}
	}
	
	@JsonIgnore
	public BillOfMaterials fillEntity(BillOfMaterials billOfMaterials) {		
		super.fillEntity(billOfMaterials);
		
		if(getProduct() != null) {
			Item product = new Item();
			product.setId(getProduct().getId());
			billOfMaterials.setProduct(product);
		}
		if (getDefaultBatch() != null) {
			billOfMaterials.setDefaultBatch(getDefaultBatch().clone());
		}
		if(getBomList() != null) {
			Ordinal.setOrdinals(getBomList());
			billOfMaterials.setBomList(getBomList().stream().map(i -> i.fillEntity(new BomLine())).collect(Collectors.toSet()));
		}
		
		return billOfMaterials;
	}

	
	
	
	
}
