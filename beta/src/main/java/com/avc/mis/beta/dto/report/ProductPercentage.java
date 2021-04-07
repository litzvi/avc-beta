/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ProductPercentage {

	String process;
	BigDecimal receivedOrderUnitsLoss;
	BigDecimal receivedCountLoss;
}
