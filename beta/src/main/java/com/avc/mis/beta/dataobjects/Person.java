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
public class Person {
	private int id;
	private String name;
	private Date dob;
	private IdCard idCard;
	private ContactDetails contactDetails;
}
