/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.validation.groups.OnPersist;
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
@Table(name = "PO_CODES")
public class PoCode extends BaseEntity {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@Id
//	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "com.avc.mis.beta.utilities.UseExistingIdOtherwiseGenerateUsingIdentity")
//	@GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
	@Column(nullable = false, updatable = false)
//	@EqualsAndHashCode.Include
	private String code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplierId", updatable = false, nullable = false)
	private Supplier supplier; 
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contractTypeId", updatable = false, nullable = false)
	@NotNull(message = "PO code is required to have a contract type", groups = OnPersist.class)
	private ContractType contractType;
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "poCode", fetch = FetchType.LAZY)
	private Set<PoProcess> processes = new HashSet<>();
	
	
	/**
	 * @return a string representing full PO code. e.g. VAT-900001, PO-900001V
	 */
	public String getValue() {
		return String.format("%s-%s%s", this.contractType.getCode(), this.code, this.contractType.getSuffix());
	}

//	@Override
//	public Integer getId() {
//		return code;
//	}
//
//	@Override
//	public void setId(Integer id) {
//		this.code = id;		
//	}

}
