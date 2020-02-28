/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.Person;
import com.avc.mis.beta.dataobjects.Supplier;
import com.avc.mis.beta.dto.SupplierDTO;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Suppliers extends DAO {
	
//	@Autowired
//	private SupplierReposetory supplierReposetory;
	
	/**
	 * 
	 * @return
	 */
	public String getSuppliersList() {
		

		String sql ="select JSON_ARRAYAGG(JSON_OBJECT('id', CO.id, 'name', CO.name, 'phones', P.phones, 'emails', E.emails, 'categories', C.categories)) as suppliers\r\n" +
			"from SUPPLIERS as S\r\n" +
			"left join COMPANIES as CO \r\n" +
				" on S.company_id=CO.id\r\n" +
			"left join CONTACT_DETAILS as CD\r\n" +
				" on CD.company_id=CO.id\r\n" +
			"left join (select contact_id, JSON_ARRAYAGG(phone) as phones from PHONES group by contact_id) as P \r\n" +
				" on CD.id = P.contact_id\r\n" +
			"left join (select contact_id, JSON_ARRAYAGG(email) as emails from EMAILS group by contact_id) \r\n" +
				" as E on CD.id = E.contact_id\r\n" +
			"left join (select company_id, JSON_ARRAYAGG(name) as categories " + 
					"from suppliers_categories " + 
					"join supply_categories on category_id = id " + 
					"group by company_id) as C " + 
				"on CO.id = C.company_id\r\n";
		
		return (String)getEntityManager().createNativeQuery(sql).getSingleResult();

//		return getJdbcTemplateObject().queryForObject(sql, String.class); 
	}
	
	public List<Supplier> getSuppliers() {
		
		
		//TODO
		return null;
		
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getSupplierDetails(int supplierId) {
	
	String sql = "select JSON_OBJECT(\r\n" +
	"	'id', CO.id, 'name', CO.name, 'localName', CO.localName, 'englishName' , CO.englishName, 'license', CO.license, \r\n"
	+
	"    'taxCode', CO.taxCode, 'registrationLocation', CO.registrationLocation, 'supplyCategories', C.supplyCategories, 'companyContacts', CC.companyContacts,\r\n"
	+ "	'contactDetails', JSON_OBJECT('id', CD.id, \r\n" +
	"		'phones', phones, \r\n" + "        'faxes', faxes, \r\n" +
	"        'emails', emails, \r\n" + "        'addresses', addresses, \r\n" +
	"        'paymentAccounts', paymentAccounts\r\n" +
	"        )) as supplier\r\n" + "from SUPPLIERS as S\r\n" +
	"join COMPANIES as CO\r\n" + "	on S.companyId = CO.id\r\n" +
	"left join CONTACT_DETAILS_VIEW as CD\r\n" + "	on CO.id = CD.companyId\r\n"
	+ "left join (\r\n" +
	"	select companyId, JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as supplyCategories \r\n"
	+ "    from SUPPLIERS_CATEGORIES_VIEW\r\n" + "    group by companyId\r\n" +
	"    ) as C \r\n" + "    on CD.id = C.companyId\r\n" + "left join (\r\n" +
	"	select companyId, \r\n" + "		JSON_ARRAYAGG(JSON_OBJECT(\r\n" +
	"			'person', JSON_OBJECT('id', personId, 'name', personName, 'date of birth', dob, \r\n"
	+
	"                'idCard', JSON_OBJECT('idNumber', IdNumber, 'dateOfIssue', dateOfIssue, 'placeOfIssue', placeOfIssue,\r\n"
	+
	"					'nationality', JSON_OBJECT('id', countryId, 'name', countryName)), \r\n"
	+
	"				'contactDetails', JSON_OBJECT('contactId', contactId, 'phones', phones, 'faxes', faxes, \r\n"
	+
	"					'emails', emails, 'addresses', addresses, 'paymentAccounts', paymentAccounts)), \r\n"
	+
	"            'position', JSON_OBJECT('id', positionId, 'name', positionName))) as companyContacts\r\n"
	+ "    from COMPANY_CONTACTS_VIEW\r\n" + "    group by companyId\r\n" +
	"    ) as CC\r\n" + "    on CO.id=CC.companyId\r\n" + "where CO.id= ? \r\n";
	
	return getJdbcTemplateObject().queryForObject(sql, new Object[] {supplierId},
	String.class); }
	
	
	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public void addSupplier(Supplier supplier) {
		if(supplier == null || !supplier.isLegal()) {
			throw new IllegalArgumentException("Supplier missing required details");
		}
		getEntityManager().persist(supplier);
		for(CompanyContact contact: supplier.getCompanyContacts()) {
			Person person = contact.getPerson();
			if(person != null && person.isLegal()) {
				getEntityManager().persist(person);
				getEntityManager().persist(contact);
			}
			
		}
//		getEntityManager().flush();
	}
	
	public SupplierDTO getSupplier(int id) {
		
		Supplier supplier = getEntityManager().find(Supplier.class, id);
		if(supplier == null) {
			throw new IllegalArgumentException("No supplier with given ID");
		}
		
		return new SupplierDTO(supplier);
		
//		Hibernate.initialize(supplier.getContactDetails().getPhones());
//		Hibernate.initialize(supplier.getContactDetails().getFaxes());
//		Hibernate.initialize(supplier.getContactDetails().getEmails());
//		Hibernate.initialize(supplier.getContactDetails().getAddresses());
//		Hibernate.initialize(supplier.getContactDetails().getPaymentAccounts());
//		Hibernate.initialize(supplier.getCompanyContacts());
		
		
//		return supplier;

//		TypedQuery<Supplier> query = getEntityManager().createNamedQuery("Supplier.details", Supplier.class);
//		query.setParameter("sid", id);
//		Supplier supplier, supplierCopy = null;
//		try {
//			supplier = query.getSingleResult();
//		}
//		catch(NoResultException e) {
//			throw new IllegalArgumentException("No supplier with given ID");
//		}
//		return supplier;
		
//		Gson gson = new Gson();
//	    supplierCopy = gson.fromJson(gson.toJson(supplier), Supplier.class);
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.ANY);
//		objectMapper.setVisibility(PropertyAccessor.SETTER, Visibility.ANY);
//		objectMapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.ANY);
//		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
//		
//		try {
//			supplierCopy = objectMapper.readValue(objectMapper.writeValueAsString(supplier), Supplier.class);
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Hibernate.initialize(supplier.getContactDetails());
//		return supplierCopy;
			
//		String sql = "select s from supplier s "
//				+ "left join fetch s.supplyCategories sc, s.contactDetails, "
//				+ "s.companyContacts ";
//		Query query = getEntityManager().createQuery(sql);
//		
//		EntityGraph<?> entityGraph = getEntityManager().getEntityGraph("graph.supplier.all");
//		Map<String, Object> properties = new HashMap<>();
//		properties.put("javax.persistence.fetchgraph", entityGraph);
//		Supplier supplier = getEntityManager().find(Supplier.class, id, properties);
		
		/*
		 * return supplierReposetory.findSupplierById(id) .orElseThrow(() -> new
		 * IllegalArgumentException("No supplier with given ID"));
		 */
		
//		if(!supplierReposetory.findById(id).isPresent()) {
//			throw new IllegalArgumentException("No supplier with given ID");
//		}
//		return supplier;
	}
	
	/*
	 * public List getSuppliersBasic() {
	 * 
	 * Query query = getEntityManager().createNativeQuery( "Supplier.findAllBasic");
	 * return query.getResultList();
	 * 
	 * }
	 */
	
	public void editSupplierInformation(Supplier supplier) {
//		getEntityManager().find(Supplier.class, supplier.getId()).setName("findName");
		getEntityManager().merge(supplier);
		getEntityManager().flush();
	}
	
	
	
	public void editSupplier() {
		
	}
	
	public void editSupplierMainInfo() {
		
	}
	
	public void editSupplierContactInfo() {
		
	}
	
	public void editSupplierAccountsInfo() {
		
	}
	
	public void editSupplierContactPersonsInfo() {
		
	}

		
	
}
