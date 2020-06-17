/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.entities.values.Country;
import com.avc.mis.beta.service.ValueTablesReader;
import com.avc.mis.beta.service.ValueWriter;

/**
 * @author Zvi
 *
 */
@SpringBootTest
public class ValueTest {

	@Autowired ValueTablesReader valueTablesReader;
	@Autowired ValueWriter valueWriter;

	
	@Test
	void softDeleteTest() {
		//test softDelete
		Country country = new Country();
		country.setValue("Imaginary country" + LocalDateTime.now().hashCode());
		valueWriter.addCountry(country);
		List<Country> countries = valueTablesReader.getAllCountries();
		int index = countries.indexOf(country);
		Country persistedCountry = countries.get(index);
		assertTrue(persistedCountry.isActive());
		valueWriter.remove(country.getClass(), country.getId());
		countries = valueTablesReader.getAllCountries();
		index = countries.indexOf(country);
		assertTrue(index == -1);
	}
}
