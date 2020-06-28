package com.avc.mis.beta.entities.values;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CASHEW_STANDARDS")
public class CashewStandard extends ValueEntity {
	
	private String standardOrganization;	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal totalDefects;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal totalDamage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal breakage;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal foreignMaterial;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal humidity;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal testa;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal scorched;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal deepCut;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal offColour;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal shrivel;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal desert;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal deepSpot;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal mold;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal dirty;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal decay;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal insectDamage;
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal nutCount;
//	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal smallKernels;
//	
	@Column(precision = 19, scale = 3)
	private BigDecimal defectsAfterRoasting;
	
	@Column(precision = 19, scale = 3)
	private BigDecimal weightLoss;
	
//	@Enumerated(EnumType.STRING)
//	private CheckStatus colour; 
//	
//	@Enumerated(EnumType.STRING)
//	private CheckStatus flavour;

	@Override
	public boolean isLegal() {
		return item != null && StringUtils.isNotBlank(standardOrganization);
	}

	@Override
	public String getValue() {
		return standardOrganization;
	}

	@Override
	public String getIllegalMessage() {
		return "Cashew standard has to specify a standard organization and reference an item";
	} 

}
