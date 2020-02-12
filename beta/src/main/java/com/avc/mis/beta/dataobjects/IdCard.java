/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class IdCard {
	private String idNumber;
	private Date dateOfIssue;
	private String placeOfIssue;
	private Country nationality;
}
