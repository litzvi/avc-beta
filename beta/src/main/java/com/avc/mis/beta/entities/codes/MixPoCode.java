/**
 * 
 */
package com.avc.mis.beta.entities.codes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;

import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.values.ContractType;

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
	private Set<BasePoCode> origionPoCodes = new HashSet<>();
	
	@Null(message = "mixed po code doesn't have a code")
	@Override
	public String getCode() {
		return super.getCode();
	}
	
	@Null(message = "mixed po code doesn't have a supplier")
	@Override
	public Supplier getSupplier() {
		return super.getSupplier();
	}
	
	@Null(message = "mixed po code doesn't have a contract type")
	@Override
	public ContractType getContractType() {
		return super.getContractType();
	}
	
	@NotNull(message = "Display is mandatory for mixed po code")
	@Override
	public String getDisplay() {
		return super.getDisplay();
	}

}
