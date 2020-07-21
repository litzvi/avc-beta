package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
public interface ProcessRepository<T extends ProductionProcess> extends BaseRepository<T> {


	/**
	 * Gets the join of process item, process and storage information for the given process.
	 * @param processId id of the process
	 * @return List of ProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks) "
		+ "from ProcessItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId ")
	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

	/**
	 * Gets all processes done for given PoCode
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	@Query("select new com.avc.mis.beta.dto.values.ProcessBasic( "
			+ "p.id, t.processName) "
		+ "from ProductionProcess p "
			+ "join p.poCode c "
			+ "join p.processType t "
		+ "where c.code = :poCodeId ")
	List<ProcessBasic> findAllProcessesByPo(Integer poCodeId);

}
