package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.processinfo.UsedItemDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.GeneralProcess;

/**
 * @author Zvi
 *
 */
public interface ProcessRepository<T extends GeneralProcess> extends BaseRepository<T> {

	@Query("select new com.avc.mis.beta.dto.view.ProcessRow("
			+ "p.id, po_code.code, t.code, t.suffix, "
			+ "p.recordedTime, p.duration) "
		+ "from PoProcess p "
			+ "join p.poCode po_code "
				+ "join po_code.contractType t "
			+ "join p.processType pt "
		+ "where pt.processName = :processName ")
	List<ProcessRow> findProcessByType(ProcessName processName);

	
	@Query("select new com.avc.mis.beta.dto.processinfo.UsedItemDTO( "
			+ "i.id, i.version, sf.ordinal, item.id, item.value, "
			+ "itemPo.id, ct.code, ct.suffix, s.name, "
			+ "unit.amount, unit.measureUnit, "
			+ "warehouseLocation.id, warehouseLocation.value, "
			+ "i.numberUnits, sf.containerWeight) "
		+ "from UsedItem i "
			+ "join i.storage sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
				+ "join sf.processItem pi "
					+ "join pi.item item "
			+ "join i.group grp "
				+ "join grp.process p "
					+ "join p.poCode itemPo "
						+ "left join itemPo.contractType ct "
						+ "left join itemPo.supplier s "
		+ "where p.id = :processId ")
	Set<UsedItemDTO> findUsedItems(int processId);
	
	@Query("select new com.avc.mis.beta.dto.query.UsedItemWithGroup( "
			+ "grp.id, grp.version, grp.groupName, grp.tableView, "
			+ "i.id, i.version, sf.ordinal, item.id, item.value, "
			+ "itemPo.id, ct.code, ct.suffix, s.name, "
			+ "unit.amount, unit.measureUnit, "
			+ "warehouseLocation.id, warehouseLocation.value, "
			+ "i.numberUnits, sf.containerWeight) "
		+ "from UsedItem i "
			+ "join i.storage sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
				+ "join sf.processItem pi "
					+ "join pi.item item "
			+ "join i.group grp "
				+ "join grp.process p "
					+ "join p.poCode itemPo "
						+ "left join itemPo.contractType ct "
						+ "left join itemPo.supplier s "
		+ "where p.id = :processId ")
	List<UsedItemWithGroup> findUsedItemsWithGroup(int processId);
	

	/**
	 * Gets the join of process item, process and storage information for the given process.
	 * @param processId id of the process
	 * @return List of ProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
			+ " i.id, i.version, "
			+ "item.id, item.value, item.category, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, sf.containerWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.groupName, i.description, i.remarks, i.tableView) "
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
		+ "from PoProcess p "
			+ "join p.poCode c "
			+ "join p.processType t "
		+ "where c.code = :poCodeId ")
	List<ProcessBasic> findAllProcessesByPo(Integer poCodeId);

}
