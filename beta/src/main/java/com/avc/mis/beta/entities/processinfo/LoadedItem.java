/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.validation.groups.PositiveAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Item line of container loading. 
 * Contains official declared amount - usually the ordered amount, besides for actual weighed amount.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "LOADED_ITEMS")
public class LoadedItem extends ProcessInfoEntity {
	
	
//	@ToString.Exclude
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "loadingId", nullable = false, updatable = false)
//	@NotNull
//	private ContainerLoading loading;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", nullable = false)
	@NotNull(message = "Item is mandatory")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private PoCode poCode;	
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name = "declaredAmount", nullable = false, 
                           	precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	@NotNull(message = "Declared amount is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit declaredAmount;	
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ContainerLoading) {
			this.setProcess((ContainerLoading)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a container loading process");
		}		
	}
}
