/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Entity
@Table(name="BANKS")
public class Bank extends ValueEntity {
	
	@Column(name = "name", nullable = false, unique = true)
	private String value;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
//	@BatchSize(size = BaseEntity.BATCH_SIZE)
	@JsonIgnore
	private Set<BankBranch> branches = new HashSet<>();
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);		
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Bank name can't be blank";
	}
	
}
