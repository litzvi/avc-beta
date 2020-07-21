/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="CONTRACT_TYPES", uniqueConstraints = 
	{ @UniqueConstraint(columnNames = { "code", "currency" }), 
			@UniqueConstraint(columnNames = { "code", "suffix" }) })
public class ContractType extends ValueEntity {

	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Contract type name is mandatory")
	private String value;
	
	@Column(name = "code", nullable = false)
	@NotBlank(message = "Contract type has to have a code")
	private String code;
	
	@Setter(value = AccessLevel.NONE)
	@Column(nullable = false)
	@NotNull(message = "Contract type has to have a currency")
	private Currency currency; 
	
	@Getter(value = AccessLevel.NONE)
	private String suffix;
	
	public void setCurrency(String currencyCode) {
		this.currency = Currency.getInstance(currencyCode);
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public String getSuffix() {
		return suffix != null ? suffix : "";
	}
	
//	public String getValue() {
//		return name;
////		return String.format("%s-%s", this.code, this.currency);
////		return code.name();
//	}

}
