/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.system.WeightedPo;
import com.avc.mis.beta.entities.values.ContractType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Basic immutable information that serves as identification for material/s, product/s 
 * that where usually aggregated in one purchase order and followed during processing life as a separate unit.
 * 
 * code and id are synonymous in this class
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "PO_CODES", uniqueConstraints = 
	{ @UniqueConstraint(columnNames = { "code", "contractTypeId" }) })
@Inheritance(strategy=InheritanceType.JOINED)
public class BasePoCode extends BaseEntity implements ValueInterface {

	@NotNull(message = "code is mandatory")
	@Column(updatable = false, nullable = false)
	private String code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplierId", nullable = false)
	private Supplier supplier; 
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contractTypeId", nullable = false)
	private ContractType contractType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productCompanyId")
	private Supplier productCompany;
		
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "poCode", fetch = FetchType.LAZY)
	private Set<PoProcess> processes = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "poCode", fetch = FetchType.LAZY)
	private Set<WeightedPo> weightedPos = new HashSet<>();
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public String getValue() {
		return display(this.contractType.getCode(), this.code, this.contractType.getSuffix());
	}
	
	/**
	 * @param contractTypeCode
	 * @param poCode
	 * @param suffix
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public static String display(String contractTypeCode, String poCode, String suffix) {
		return String.format("%s-%s%s", contractTypeCode, poCode, suffix);		
	}

}
