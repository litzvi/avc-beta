/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.SampleItemWithWeight;
import com.avc.mis.beta.entities.process.SampleReceipt;

/**
 * @author Zvi
 *
 */
public interface SampleRepository extends PoProcessRepository<SampleReceipt> {
	
	@Query("select new com.avc.mis.beta.dto.query.SampleItemWithWeight( "
			+ " i.id, i.version, i.ordinal, item.id, item.value, "
			+ "i.measureUnit, i.sampleContainerWeight, "
			+ "w.id, w.version, w.ordinal, w.unitAmount, w.numberUnits, w.numberOfSamples, w.avgTestedWeight) "
		+ "from SampleItem i "
			+ "join i.item item "
			+ "join i.process p "
			+ "join i.itemWeights w "
//			+ "join i.amountWeighed weighed "
		+ "where p.id = :processId "
//		+ "order by i.ordinal, w.ordinal " //sorted in dto
		+ "")
	List<SampleItemWithWeight> findSampleItemsWithWeight(int processId);
}
