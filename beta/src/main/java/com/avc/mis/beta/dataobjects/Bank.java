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
import com.avc.mis.beta.dataobjects.interfaces.KeyIdentifiable;
import com.avc.mis.beta.dataobjects.interfaces.Legible;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="BANKS")
@NamedQuery(name = "Bank.findAll", query = "select b from Bank b")
public class Bank implements Legible, KeyIdentifiable{
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String value;
	
//	@JsonBackReference(value = "branch_bank")
	@ToString.Exclude
	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	@JsonIgnore
	private Set<BankBranch> branches = new HashSet<>();
	
	public void setValue(String value) {
		this.value = value.trim();
	}
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
}
