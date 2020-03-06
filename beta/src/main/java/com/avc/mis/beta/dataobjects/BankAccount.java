/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.dataobjects.interfaces.KeyIdentifiable;
import com.avc.mis.beta.dataobjects.interfaces.Legible;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "BANK_ACCOUNTS"/*
								 * , uniqueConstraints = {@UniqueConstraint(columnNames = {"accountNo",
								 * "branchId"})}
								 */)
public class BankAccount implements Legible, KeyIdentifiable{
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String accountNo;
	
	@Column(nullable = false)
	private String ownerName;
		
	@JoinColumn(name="branchId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private BankBranch branch;
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName.trim();
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo.trim();
	}
	
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}
	

	/**
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getAccountNo()) && 
				StringUtils.isNotBlank(getOwnerName()) && getBranch() != null;
	}

}
