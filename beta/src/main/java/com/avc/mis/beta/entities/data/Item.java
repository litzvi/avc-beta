/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;

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
@Table(name = "ITEMS")
@NamedQuery(name = "Item.findAll", query = "select i from Item i")
public class Item extends BaseEntity {

//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

	@Column(name = "name", unique = true, nullable = false)
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	private MeasureUnit measureUnit;

	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue()) && measureUnit != null; 
	}

	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException("Item can't be blank and has to have a measure unit");
		
	}
}
