/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.BookedContainerDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ShipmentBooking;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ShipmentBookingDTO extends GeneralProcessDTO {

	private Set<BookedContainerDTO> bookedContainers; //can use a SortedSet like ContactDetails to maintain order
	
	private String personInCharge;
	

	/**
	 * All arguments (besides for booked containers) Constructor ,
	 * used to project directly from database without nested fetching.
	 */
	public ShipmentBookingDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
//			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName,  
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, OffsetDateTime startTime, OffsetDateTime endTime, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals,
			String personInCharge) {
		super(id, version, createdDate, staffRecording, 
//				poCodeId, contractTypeCode, contractTypeSuffix,
//				supplierId, supplierVersion, supplierName, 
				processName, productionLine, 
				recordedTime, startTime, endTime, duration, 
				numOfWorkers, processStatus, editStatus, remarks, approvals);
		this.personInCharge = personInCharge;

	}
	
	/**
	 * Constructor from ShipmentBooking object, used for testing.
	 * @param booking the ShipmentBooking object
	 */
	public ShipmentBookingDTO(@NonNull ShipmentBooking booking) {
		super(booking);
		this.personInCharge = booking.getPersonInCharge();
		this.bookedContainers = Arrays.stream(booking.getBookedContainers()).map(i->{return new BookedContainerDTO(i);}).collect(Collectors.toSet());

	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Container Booking";
	}

}
