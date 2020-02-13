/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class BankBranch {
	private Integer id;
	private String name;
	private Bank bank;
}
