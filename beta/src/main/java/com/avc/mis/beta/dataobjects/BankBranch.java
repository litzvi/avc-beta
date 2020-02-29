/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="BANK_BRANCHES")
@NamedQuery(name = "BankBranch.findAll", query = "select bb from BankBranch bb")
public class BankBranch {
	
	@Id @GeneratedValue
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
//	@JsonManagedReference(value = "branch_bank")
	@ManyToOne(optional = false)
	@JoinColumn(name = "bankId", nullable = false)
	private Bank bank;
}
