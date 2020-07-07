package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.entities.process.ProductionProcess;

public interface ProcessRepository<T extends ProductionProcess> extends BaseRepository<T> {


	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
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

	@Query("select new com.avc.mis.beta.dto.values.ProcessBasic( "
			+ "p.id, t.processName) "
		+ "from ProductionProcess p "
			+ "join p.poCode c "
			+ "join p.processType t "
		+ "where c.code = :poId ")
	List<ProcessBasic> findAllProcessesByPo(Integer poId);

}
