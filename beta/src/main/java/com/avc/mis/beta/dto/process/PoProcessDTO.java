/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.embedable.PoProcessInfo;
import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.processinfo.WeightedPoDTO;
import com.avc.mis.beta.entities.process.PoProcess;

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

	
}
