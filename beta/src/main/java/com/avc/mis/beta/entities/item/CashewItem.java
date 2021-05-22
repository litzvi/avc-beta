/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.CashewGrade;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SaltLevel;
import com.avc.mis.beta.validation.groups.PositiveAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@DiscriminatorValue("cashew")
public class CashewItem extends Item {
	
	@Column(columnDefinition = "int default 1")
	@Positive(message = "Number of bags has to be positive", groups = PositiveAmount.class)
	private int numBags = 1;
	
	@Enumerated(EnumType.STRING)
//	@Column(nullable = false, updatable = false) - TODO TEMPERARY COMMENTED
//	@NotNull(message = "Cashew grade/type is mandatory")
	private CashewGrade grade;

//	@Column(nullable = false, updatable = false) - TODO TEMPERARY COMMENTED
//	@NotNull(message = "Is whole is mandatory")
	private Boolean isWhole = false;

//	@Column(nullable = false, updatable = false) - TODO TEMPERARY COMMENTED
//	@NotNull(message = "Is roast is mandatory")
	private Boolean isRoast = false;

//	@Column(nullable = false, updatable = false) - TODO TEMPERARY COMMENTED
//	@NotNull(message = "Is toffee is mandatory")
	private Boolean isToffee = false;

	@Enumerated(EnumType.STRING)
//	@Column(nullable = false, updatable = false) - TODO TEMPERARY COMMENTED
//	@NotNull(message = "Salt level is mandatory")
	private SaltLevel saltLevel = SaltLevel.NS;

}
