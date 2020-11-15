package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;
import com.avc.mis.beta.entities.item.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cashew standard entity - records standards for raw kernel of cashew nuts of organizations.
 * e.g. Vina control standard, AVC standard etc.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CASHEW_STANDARDS")
public class CashewStandard extends ValueEntity {
	
	@NotBlank(message = "Standard organization is mandatory")
	private String standardOrganization;	

	@JoinTable(name = "STANDARD_ITEMS",
			joinColumns = @JoinColumn(name = "standardId", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "itemId", referencedColumnName = "id"))
	@ManyToMany(fetch = FetchType.EAGER)
	@NotEmpty(message = "Cashew standard has to reference at least one item")
	private Set<Item> items;
	
	@NotNull
	@Embedded
	private RawDefects defects;

	@NotNull
	@Embedded
	private RawDamage damage;
		
	@Column(precision = 19, scale = 3)
	private BigDecimal totalDefects;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal totalDamage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal totalDefectsAndDamage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal wholeCountPerLb;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal smallSize;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal ws;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal lp;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal breakage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal foreignMaterial;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal humidity;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal roastingWeightLoss;

}
