package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.embeddable.RawDamage;
import com.avc.mis.beta.entities.embeddable.RawDefects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CASHEW_STANDARDS", uniqueConstraints = 
{ @UniqueConstraint(columnNames = { "itemId", "standardOrganization" }) })
public class CashewStandard extends ValueEntity {
	
	@NotBlank(message = "Standard organization is mandatory")
	private String standardOrganization;	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	@NotNull(message = "Cashew standard has to reference an item")
	private Item item;
	
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
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal testa;
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal scorched;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal deepCut;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal offColour;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal shrivel;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal desert;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal deepSpot;
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal mold;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal dirty;
//
//	@Column(precision = 19, scale = 3)
//	private BigDecimal lightDirty;
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal decay;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal insectDamage;
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal nutCount;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal smallKernels;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal defectsAfterRoasting;
//	
	@Column(precision = 19, scale = 3)
	private BigDecimal roastingWeightLoss;
	
//	@Enumerated(EnumType.STRING)
//	private CheckStatus colour; 
//	
//	@Enumerated(EnumType.STRING)
//	private CheckStatus flavour;

	@Override
	public String getValue() {
		return String.format("%s-%s", this.item.getValue(), this.standardOrganization);
	}

}
