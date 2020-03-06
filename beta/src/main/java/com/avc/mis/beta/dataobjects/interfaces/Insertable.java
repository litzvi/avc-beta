/**
 * 
 */
package com.avc.mis.beta.dataobjects.interfaces;

import java.util.Arrays;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author Zvi
 *
 */
public interface Insertable extends Legible{
	
	
	public void setReference(Object referenced);

//	public String getValue();
	
	static <S, T extends Insertable> Set<T> filterAndSetReference(T[] tArray, UnaryOperator<T> p) {
		return Arrays.stream(tArray)
			.filter(t -> t.isLegal())
			.map(t -> p.apply(t))
			.collect(Collectors.toSet());
	}
	
}
