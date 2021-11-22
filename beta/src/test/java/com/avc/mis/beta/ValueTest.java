/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.dto.values.CountryDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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
		CountryDTO country = new CountryDTO();
		country.setValue("Imaginary country" + LocalDateTime.now().hashCode());
		Integer countryId = valueWriter.addCountry(country);
		List<CountryDTO> countries = valueTablesReader.getAllCountries();
		CountryDTO persistedCountry = countries.stream().filter(i -> i.getId() == countryId).findAny().get();
		assertTrue(countries.stream().filter(i -> i.getId() == countryId).findAny().isPresent());
		country.setId(countryId);
		valueWriter.remove(persistedCountry.getClass(), countryId);
		countries = valueTablesReader.getAllCountries();
		assertFalse(countries.stream().filter(i -> i.getId() == countryId).findAny().isPresent());
	}
	
	@Test
	void settingsUOMTest() {
		BigDecimal origion = BigDecimal.ONE;
		
		//test uom conversion
		BigDecimal dest = MeasureUnit.convert(origion, MeasureUnit.GRAM, MeasureUnit.KG);
		dest = MeasureUnit.convert(dest, MeasureUnit.KG, MeasureUnit.LBS);
		dest = MeasureUnit.convert(dest, MeasureUnit.LBS, MeasureUnit.LOT);
		dest = MeasureUnit.convert(dest, MeasureUnit.LOT, MeasureUnit.OZ);
		dest = MeasureUnit.convert(dest, MeasureUnit.OZ, MeasureUnit.GRAM);
		System.out.println("origion: " + origion + ", dest: " + dest);
		assertTrue(origion.compareTo(dest.setScale(MeasureUnit.CALCULATION_SCALE - 1, RoundingMode.HALF_EVEN)) == 0);
		
		//test uom conversion
		dest = MeasureUnit.convert(origion, MeasureUnit.GRAM, MeasureUnit.OZ);
		dest = MeasureUnit.convert(dest, MeasureUnit.OZ, MeasureUnit.LOT);
		dest = MeasureUnit.convert(dest, MeasureUnit.LOT, MeasureUnit.LBS);
		dest = MeasureUnit.convert(dest, MeasureUnit.LBS, MeasureUnit.KG);
		dest = MeasureUnit.convert(dest, MeasureUnit.KG, MeasureUnit.GRAM);
		System.out.println("origion: " + origion + ", dest: " + dest);
		assertTrue(origion.compareTo(dest.setScale(MeasureUnit.CALCULATION_SCALE - 1, RoundingMode.HALF_EVEN)) == 0);
		
		//test uom conversion
		dest = MeasureUnit.convert(origion, MeasureUnit.GRAM, MeasureUnit.LBS);
		dest = MeasureUnit.convert(dest, MeasureUnit.LBS, MeasureUnit.OZ);
		dest = MeasureUnit.convert(dest, MeasureUnit.OZ, MeasureUnit.KG);
		dest = MeasureUnit.convert(dest, MeasureUnit.KG, MeasureUnit.LOT);
		dest = MeasureUnit.convert(dest, MeasureUnit.LOT, MeasureUnit.GRAM);
		System.out.println("origion: " + origion + ", dest: " + dest);
		assertTrue(origion.compareTo(dest.setScale(MeasureUnit.CALCULATION_SCALE - 1, RoundingMode.HALF_EVEN)) == 0);

		//test uom conversion
		dest = MeasureUnit.convert(origion, MeasureUnit.GRAM, MeasureUnit.LOT);
		dest = MeasureUnit.convert(dest, MeasureUnit.LOT, MeasureUnit.KG);
		dest = MeasureUnit.convert(dest, MeasureUnit.KG, MeasureUnit.OZ);
		dest = MeasureUnit.convert(dest, MeasureUnit.OZ, MeasureUnit.LBS);
		dest = MeasureUnit.convert(dest, MeasureUnit.LBS, MeasureUnit.GRAM);
		System.out.println("origion: " + origion + ", dest: " + dest);
		assertTrue(origion.compareTo(dest.setScale(MeasureUnit.CALCULATION_SCALE - 1, RoundingMode.HALF_EVEN)) == 0);

		
	}
}
