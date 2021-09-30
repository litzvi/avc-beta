/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.LinkEntity;
import com.avc.mis.beta.entities.SubjectLinkEntity;
import com.avc.mis.beta.entities.data.ContactDetails;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;
import com.avc.mis.beta.validation.groups.PositiveAmount;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "BOM_LINES")
public class BomLine extends SubjectLinkEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bomId", updatable = false)
	private BillOfMaterials bom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "materialId", nullable = false)
	@NotNull(message = "Material is mandatory")
	private Item material;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(precision = 19, scale = MeasureUnit.SCALE))
//        ,
//        @AttributeOverride(name="measureUnit",
//                           column=@Column(nullable = false))
    })
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit defaultAmount;
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof BillOfMaterials) {
			this.setBom((BillOfMaterials)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a bill of materials");
		}		
	}
}
