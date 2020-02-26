/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Entity
@Table(name="COUNTRIES")
@NamedQuery(name = "Country.findAll", query = "select c from Country c")
public class Country {
	
	@Id @GeneratedValue
	private int id;
	
	@Column(unique = true, nullable = false)
	private String Name;
	
//	@JsonBackReference(value = "city_country")
//	@EqualsAndHashCode.Exclude
	@ToString.Exclude 
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private transient Set<City> cities;
}
