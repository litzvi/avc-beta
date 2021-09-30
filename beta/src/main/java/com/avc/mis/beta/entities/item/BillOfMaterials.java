/**
 * 
 */
package com.avc.mis.beta.entities.item;

import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ProcessGroup;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.validation.groups.PositiveAmount;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "BOM")
public class BillOfMaterials extends LinkEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false, unique = true)
	@NotNull(message = "Product is mandatory")
	private Item product;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="batchAmount",  
                           	precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(name="batchMeasureUnit"))
    })
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit defaultBatch;
	
	@Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "bom", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<BomLine> bomList;
	
	public void setBomList(Set<BomLine> bomList) {
		this.bomList = Insertable.setReferences(bomList, (t) -> {t.setReference(this);	return t;});
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof Item) {
			this.setProduct((Item)referenced);
		}
		else {
			throw new ClassCastException("Bill of materials needs to have a product, product not set");
		}		
	}
}
