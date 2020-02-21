/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dataobjects.Supplier;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional
public class Suppliers extends DAO {
	
	
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
				"left join (select contactId, JSON_ARRAYAGG(phone) as phones from PHONES group by contactId) as P \r\n" + 
				"	on CD.id = P.contactId\r\n" + 
				"left join (select contactId, JSON_ARRAYAGG(email) as emails from EMAILS group by contactId) \r\n" + 
				"	as E on CD.id = E.contactId\r\n" + 
				"left join (select companyId, JSON_ARRAYAGG(name) as categories from category_suppliers join supply_categories on categoryId = id group by companyId) \r\n" + 
				"	as C on CD.id = C.companyId\r\n";
		

		return getJdbcTemplateObject().queryForObject(sql, String.class); 
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSupplierDetails(int supplierId) {
		
		String sql = "select JSON_OBJECT(\r\n" + 
				"	'id', CO.id, 'name', CO.name, 'localName', CO.localName, 'englishName' , CO.englishName, 'license', CO.license, \r\n" + 
				"    'taxCode', CO.taxCode, 'registrationLocation', CO.registrationLocation, 'supplyCategories', C.supplyCategories, 'companyContacts', CC.companyContacts,\r\n" + 
				"	'contactDetails', JSON_OBJECT('id', CD.id, \r\n" + 
				"		'phones', phones, \r\n" + 
				"        'faxes', faxes, \r\n" + 
				"        'emails', emails, \r\n" + 
				"        'addresses', addresses, \r\n" + 
				"        'paymentAccounts', paymentAccounts\r\n" + 
				"        )) as supplier\r\n" + 
				"from SUPPLIERS as S\r\n" + 
				"join COMPANIES as CO\r\n" + 
				"	on S.companyId = CO.id\r\n" + 
				"left join CONTACT_DETAILS_VIEW as CD\r\n" + 
				"	on CO.id = CD.companyId\r\n" + 
				"left join (\r\n" + 
				"	select companyId, JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as supplyCategories \r\n" + 
				"    from SUPPLIERS_CATEGORIES_VIEW\r\n" + 
				"    group by companyId\r\n" + 
				"    ) as C \r\n" + 
				"    on CD.id = C.companyId\r\n" + 
				"left join (\r\n" + 
				"	select companyId, \r\n" + 
				"		JSON_ARRAYAGG(JSON_OBJECT(\r\n" + 
				"			'person', JSON_OBJECT('id', personId, 'name', personName, 'date of birth', dob, \r\n" + 
				"                'idCard', JSON_OBJECT('idNumber', IdNumber, 'dateOfIssue', dateOfIssue, 'placeOfIssue', placeOfIssue,\r\n" + 
				"					'nationality', JSON_OBJECT('id', countryId, 'name', countryName)), \r\n" + 
				"				'contactDetails', JSON_OBJECT('contactId', contactId, 'phones', phones, 'faxes', faxes, \r\n" + 
				"					'emails', emails, 'addresses', addresses, 'paymentAccounts', paymentAccounts)), \r\n" + 
				"            'position', JSON_OBJECT('id', positionId, 'name', positionName))) as companyContacts\r\n" + 
				"    from COMPANY_CONTACTS_VIEW\r\n" + 
				"    group by companyId\r\n" + 
				"    ) as CC\r\n" + 
				"    on CO.id=CC.companyId\r\n" + 
				"where CO.id= ? \r\n";
		
		return getJdbcTemplateObject().queryForObject(sql, new Object[] {supplierId}, String.class);
	}

	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public void addSupplier(Supplier supplier) {
		getEntityManager().persist(supplier);	
	}
	
	public Supplier getSupplier(int id) {
		Supplier supplier = getEntityManager().find(Supplier.class, id);
		if(supplier == null) {
			throw new IllegalArgumentException("No supplier with given ID");
		}
		return getEntityManager().find(Supplier.class, id);
	}
	
	public void editSupplierInformation(Supplier supplier) {
//		getEntityManager().find(Supplier.class, supplier.getId()).setName("findName");
		getEntityManager().merge(supplier);
		getEntityManager().flush();
	}

		
	
}
