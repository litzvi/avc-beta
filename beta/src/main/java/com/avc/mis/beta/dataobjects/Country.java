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
@Table(name="COUNTRIES")
@NamedQuery(name = "Country.findAll", query = "select c from Country c")
public class Country implements legible, KeyIdentifiable {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String value;
	
//	@JsonBackReference(value = "city_country")
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private Set<City> cities = new HashSet<>();
	
	public void setValue(String value) {
		this.value = value.trim();
	}
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}

	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
}
