/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.util.List;

import lombok.Data;

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
