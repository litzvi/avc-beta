/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.BaseEntity;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="PRODUCTION_LINES")
public class ProductionLine extends BaseEntity {

//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

	@Column(name = "name", unique = true, nullable = false)
	private String value;

	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Production line value can't be blank");		
	}
}
