/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Interface for common database join of objects and their nested Collections.
 * Has helper functions for converting from database join structure to Object Oriented (OO).
 * 
 * @author zvi
 *
 */
public interface CollectionItemWithGroup {
	
	public static <E> Collection<E> safeCollection(Collection<E> collection) {
		return collection == null ? Collections.emptyList() : collection;
	}
	
	/**
	 * Used for building OO structure when fetching objects and their collections with one join query.
	 * Building List of groups, each filled with list of 'data items' (OO), when receiving a list of join operation between groups and their collection of 'items'.
	 * @param <R> dataWithGroups class
	 * @param <G> group class
	 * @param <I> item(in group) class
	 * @param dataWithGroups join of group with each of it's items.
	 * @param groupSupplier function to get the group from dataWithGroup object
	 * @param itemSupplier function to get the item from dataWithGroup object
	 * @param groupSetter function to set the group's items list.
	 * @return List of groups with lists of items set(filled).
	 */
	public static <R, G, I> List<G> getFilledGroups(
			List<R> dataWithGroups, 
			Function<R, G> groupSupplier, 
			Function<R, I> itemSupplier,
			BiConsumer<G, List<I>> groupSetter) {
		if(dataWithGroups == null || dataWithGroups.isEmpty()) {
			return null;
		}
		Map<G, List<I>> map = dataWithGroups.stream()
				.collect(Collectors.groupingBy(groupSupplier, 
						LinkedHashMap::new, 
						Collectors.mapping(itemSupplier, Collectors.toList())));
		List<G> groups = new ArrayList<>();
		map.forEach((k, v) -> {
			groupSetter.accept(k, v);
			groups.add(k);
		});
		return groups;
	}
		
	/**
	 * Fills given groups with lists of items.
	 * Used for building OO structure when fetching objects and their collections with one join query.
	 * Building List of groups, each filled with list of 'data items' (OO), when receiving a list of join operation between groups and their collection of 'items'.
	 * @param <K> key of group class
	 * @param <R> dataWithGroups class
	 * @param <G> group class
	 * @param <I> item(in group) class
	 * @param groups list of groups to fill with items
	 * @param dataWithGroups join of group with each of it's items.
	 * @param groupKeySupplier
	 * @param dataKeySupplier
	 * @param itemSupplier function to get the item from dataWithGroup object
	 * @param groupSetter function to set the group's items list.
	 */
	public static <K, R, G, I> void fillGroups(
			List<G> groups,
			List<R> dataWithGroups, 
			Function<G, K> groupKeySupplier, 
			Function<R, K> dataKeySupplier, 
			Function<R, I> itemSupplier,
			BiConsumer<G, List<I>> groupSetter) {
		if(dataWithGroups == null || dataWithGroups.isEmpty()) {
			return;
		}
		else {
			Map<K, List<I>> map = dataWithGroups.stream()
					.collect(Collectors.groupingBy(dataKeySupplier, 
							LinkedHashMap::new, 
							Collectors.mapping(itemSupplier, Collectors.toList())));
			groups.forEach(g -> {
				groupSetter.accept(g, map.get(groupKeySupplier.apply(g)));
			});
		}
	}
}
