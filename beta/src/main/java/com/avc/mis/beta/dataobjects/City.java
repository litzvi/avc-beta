/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="CITIES", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "countryId"})})
@NamedQuery(name = "City.findAll", query = "select c from City c")
public class City {
	
	@Id @GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private String name;
	
//	@JsonManagedReference(value = "city_country")
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "countryId", nullable = false)
	private Country country;
}
