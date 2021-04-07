/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.List;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class ListGroupImp<T> implements ListGroup<T> {
	
	private Integer id;
	
	private List<T> list;
	
}
