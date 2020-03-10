/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dataobjects.Bank;
import com.avc.mis.beta.dataobjects.BankBranch;
import com.avc.mis.beta.dataobjects.City;
import com.avc.mis.beta.dataobjects.CompanyPosition;
import com.avc.mis.beta.dataobjects.Country;
import com.avc.mis.beta.dataobjects.SupplyCategory;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class ReferenceTables extends DAO {
	
	/**
	 * 
	 * @return
	 */
	public List<SupplyCategory> getAllSupplyCategories() {
		
		TypedQuery<SupplyCategory> query = getEntityManager().createNamedQuery(
				"SupplyCategory.findAll", SupplyCategory.class);
		return query.getResultList();
	}
	
	public List<City> getAllCities() {
		
		TypedQuery<City> query = getEntityManager().createNamedQuery(
				"City.findAll", City.class);
		return query.getResultList();
	}
	
	public List<Country> getAllCountries() {
		
		TypedQuery<Country> query = getEntityManager().createNamedQuery(
				"Country.findAll", Country.class);
		return query.getResultList();
	}
	
	public Map<Country, List<City>> getCitiesByCountry() {
		
		return getAllCities().stream().collect(Collectors.groupingBy(City::getCountry));
	}

	public List<CompanyPosition> getAllCompanyPositions() {
		
		TypedQuery<CompanyPosition> query = getEntityManager().createNamedQuery(
				"CompanyPosition.findAll", CompanyPosition.class);
		return query.getResultList();
	}
	
	public List<Bank> getAllBanks() {
		
		TypedQuery<Bank> query = getEntityManager().createNamedQuery(
				"Bank.findAll", Bank.class);
		return query.getResultList();
	}
	
	public List<BankBranch> getAllBankBranches() {
		
		TypedQuery<BankBranch> query = getEntityManager().createNamedQuery(
				"BankBranch.findAll", BankBranch.class);
		return query.getResultList();
	}
	
	
}
