/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Table(name = "MIXED_PO_CODES")
//@PrimaryKeyJoinColumn(name = "poCodeId")
//@DiscriminatorValue("mix_code")
public class MixPoCode extends BasePoCode {

	@ToString.Exclude 
	@JoinTable(name = "MIX_POS",
			joinColumns = @JoinColumn(name = "mixedId", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "poId", referencedColumnName = "id"))
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<BasePoCode> poCodes = new HashSet<>();
	
	@Override
	public void setDisplay(String display) {
		super.setDisplay(display);
	}

}
