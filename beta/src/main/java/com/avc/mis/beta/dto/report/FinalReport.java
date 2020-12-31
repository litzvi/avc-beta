/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.entities.enums.ProcessName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
public class FinalReport {
	
	private ReceiptReportLine receipt;
	private List<QcReportLine> receiptQC;
	private ProductionReportLine cleaning;
	private ProductionReportLine roasting;
	private List<QcReportLine> roastQC;
	private ProductionReportLine packing;
	private List<LoadingReportLine> loadings;
	

}
