/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="BANKS")
@NamedQuery(name = "Bank.findAll", query = "select b from Bank b")
public class Bank {
	
	@Id @GeneratedValue
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
//	@JsonBackReference(value = "branch_bank")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	@JsonIgnore
	private Set<BankBranch> branches = new HashSet<>();
}
