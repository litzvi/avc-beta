/**
 * 
 */
package com.avc.mis.beta.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.Data;

import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dataobjects.SupplyCategory;
import com.google.gson.Gson;
import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;
import com.avc.mis.beta.dataobjects.Company;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional
public class Suppliers {
	
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	/**
	 * 
	 * @return
	 */
	public String getSuppliersList() {
		
		String sql ="select JSON_ARRAYAGG(JSON_OBJECT('id', CO.id, 'name', CO.name, 'phones', P.phones, 'emails', E.emails, 'categories', C.categories)) as suppliers\r\n" + 
				"from SUPPLIERS as S\r\n" + 
				"left join COMPANIES as CO \r\n" + 
				"	on S.companyId=CO.id\r\n" + 
				"left join CONTACT_DETAILS as CD\r\n" + 
				"	on CD.companyId=CO.id\r\n" + 
				"join (select contactId, JSON_ARRAYAGG(phone) as phones from PHONES group by contactId) as P \r\n" + 
				"	on CD.id = P.contactId\r\n" + 
				"join (select contactId, JSON_ARRAYAGG(email) as emails from EMAILS group by contactId) \r\n" + 
				"	as E on CD.id = E.contactId\r\n" + 
				"join (select companyId, JSON_ARRAYAGG(name) as categories from category_suppliers join supply_categories on categoryId = id group by companyId) \r\n" + 
				"	as C on CD.id = C.companyId\r\n" + 
				"group by CO.id\r\n";
		

		return jdbcTemplateObject.queryForObject(sql, String.class); 
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSupplierDetails() {
		
		String sql = "select JSON_OBJECT(\r\n" + 
				"	'id', CO.id, 'name', CO.name, 'legal vietnamese name', CO.localName, \r\n" + 
				"    'legal english name', CO.englishName, 'license number', CO.license, \r\n" + 
				"    'tax code', CO.taxCode, 'registration location', CO.registrationLocation, 'supply categories', C.categories, 'company contacts', CC.contacts,\r\n" + 
				"	'contact details', JSON_OBJECT('id', CD.id, \r\n" + 
				"		'phones', phones, \r\n" + 
				"        'faxes', faxes, \r\n" + 
				"        'emails', emails, \r\n" + 
				"        'addresses', addresses, \r\n" + 
				"        'payment accounts', paymentAccounts\r\n" + 
				"        )) as supplier\r\n" + 
				"from SUPPLIERS as S\r\n" + 
				"join COMPANIES as CO\r\n" + 
				"	on S.companyId = CO.id\r\n" + 
				"left join CONTACT_DETAILS_VIEW as CD\r\n" + 
				"	on CO.id = CD.companyId\r\n" + 
				"left join (\r\n" + 
				"	select companyId, JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as categories \r\n" + 
				"    from SUPPLIERS_CATEGORIES_VIEW\r\n" + 
				"    group by companyId\r\n" + 
				"    ) as C \r\n" + 
				"    on CD.id = C.companyId\r\n" + 
				"left join (\r\n" + 
				"	select companyId, \r\n" + 
				"		JSON_ARRAYAGG(JSON_OBJECT(\r\n" + 
				"			'id', personId, \r\n" + 
				"            'name', personName, \r\n" + 
				"            'position', JSON_OBJECT('id', positionId, 'name', positionName), \r\n" + 
				"            'date of birth', dob, \r\n" + 
				"            'id information', JSON_OBJECT(\r\n" + 
				"				'id number', IdNumber, 'date of issue', dateOfIssue, 'place of issue', placeOfIssue,\r\n" + 
				"                'country', JSON_OBJECT('id', countryId, 'name', countryName)),\r\n" + 
				"			'contact details', JSON_OBJECT(\r\n" + 
				"				'id', contactId,\r\n" + 
				"				'phones', phones, \r\n" + 
				"				'faxes', faxes, \r\n" + 
				"				'emails', emails, \r\n" + 
				"				'addresses', addresses, \r\n" + 
				"				'payment accounts', paymentAccounts\r\n" + 
				"				)\r\n" + 
				"			)) as contacts\r\n" + 
				"    from COMPANY_CONTACTS_VIEW\r\n" + 
				"    group by companyId\r\n" + 
				"    ) as CC\r\n" + 
				"    on CO.id=CC.companyId\r\n" + 
				"where CO.id=1\r\n";
		
		return jdbcTemplateObject.queryForObject(sql, String.class);
	}

	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public void addSupplier(Supplier supplier) {

		Supplier.insertSupplier(getJdbcTemplateObject(), supplier);
		
	}

	/**
	 * @return the jdbcTemplateObject
	 */
	private JdbcTemplate getJdbcTemplateObject() {
		return jdbcTemplateObject;
	}
	
	
}
