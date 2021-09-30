/**
 * 
 */
package com.avc.mis.beta.entities.plan;

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

import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
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
@Table(name = "USED_ITEM_PLANS")
public class UsedItemPlan extends RankedAuditedEntity {

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "planId", updatable = false, nullable = false)
	private ProductionPlanRow plan;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processItemId", nullable = false, updatable = false)
	@NotNull(message = "Storage has to reference a process item")
	private ProcessItem processItem;

	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="numberUnits", nullable = false, 
                           	precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	@NotNull(message = "number of units is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit numberUnits;
}
