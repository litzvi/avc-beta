/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.ShipmentCodeBasic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoadingRow extends ValueDTO {

	private ShipmentCodeBasic shipmentCode;
//	private PoCodeBasic poCode;
	private OffsetDateTime recordedTime;
	private Duration duration;
	 
	private List<ProductionProcessWithItemAmount> usedItems;
	private List<ProductionProcessWithItemAmount> loadedItems;
	
	
	public LoadingRow(@NonNull Integer id, 
			Integer shipmentCodeId, String portOfDischargeCode, String portOfDischargeValue, 
			OffsetDateTime recordedTime, Duration duration) {
		super(id);
		this.shipmentCode = new ShipmentCodeBasic(shipmentCodeId, portOfDischargeCode, portOfDischargeValue);
		this.recordedTime = recordedTime;
		this.duration = duration;
		
	}
	
}
