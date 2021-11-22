package com.avc.mis.beta.utilities;

import lombok.Value;

/**
 * Pairs of key and value for generically fetching pairs directly from database.
 * 
 * @author zvi
 *
 * @param <K> key class
 * @param <V> Value class
 */
@Value
public class KeyValueObject <K, V>{

	K key;
	V value;
}
