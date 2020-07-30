/**
 * 
 */
package com.avc.mis.beta;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avc.mis.beta.entities.enums.MeasureUnit;
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
