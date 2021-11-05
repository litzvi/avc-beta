/**
 * 
 */
package com.avc.mis.beta.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.collection.ItemCountDTO;
import com.avc.mis.beta.dto.process.collection.WeightedPoDTO;
import com.avc.mis.beta.dto.processInfo.PoProcessInfo;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.collection.ItemCount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying PoProcess entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class PoProcessDTO extends GeneralProcessDTO {
	
	private PoCodeBasic poCode;	
	
	@EqualsAndHashCode.Exclude
	private List<WeightedPoDTO> weightedPos;
	
	private List<ItemCountDTO> itemCounts;
	
	public PoProcessDTO(@NonNull PoProcess process) {
		super(process);
		if(process.getPoCode() != null)
			this.poCode = new PoCodeBasic(process.getPoCode());
	}
	
	public void setPoProcessInfo(PoProcessInfo info) {
		if(info != null)
			this.poCode = info.getPoCode();
	}
	
	/**
	 * @return List<WeightedPoDTO> if poCode is null, otherwise null
	 * because no need to present weighted pos if it's only one po.
	 */
	public List<WeightedPoDTO> getWeightedPos() {
		if(poCode != null) {
			return null;
		}
		return this.weightedPos;
	}
	
	@Override
	public PoProcess fillEntity(Object entity) {
		PoProcess poProcess;
		if(entity instanceof PoProcess) {
			poProcess = (PoProcess) entity;
		}
		else {
			throw new IllegalStateException("Param has to be PoProcess class");
		}
		super.fillEntity(poProcess);
		if(getPoCode() != null)
			poProcess.setPoCode(getPoCode().fillEntity(new BasePoCode()));
		if(getItemCounts() != null) {
			Ordinal.setOrdinals(getItemCounts());
			poProcess.setItemCounts(getItemCounts().stream().map(i -> i.fillEntity(new ItemCount())).collect(Collectors.toSet()));
		}
		
		return poProcess;
	}

	
}
