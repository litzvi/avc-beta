/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Interface for common database join of objects and their nested Collections.
 * Has helper functions for converting from database join structure to Object Oriented (OO).
 * 
 * @author zvi
 *
 */
public interface CollectionItemWithGroup<I, G extends ListGroup<I>> {
		
	public default Integer getGroupId() {
		return getGroup().getId();
	}
	
	public abstract I getItem();
	
	public abstract G getGroup();
	
	
	/**
	 * Used for building OO structure when fetching objects and their collections with one join query.
	 * Building List of groups, each filled with list of 'data items' (OO), when receiving a list of join operation between groups and their collection of 'items'.
	 * @param <I> Class of 'item' within the group collection
	 * @param <G> Class of the 'group'
	 * @param dataWithGroups the inner join of the group data with it's collection of 'items'
	 * @return List of groups with nested list of each corresponding collection set in OO structure.
	 */
	public static <I, G extends ListGroup<I>> List<G> getFilledGroups(List<? extends CollectionItemWithGroup<I, G>> dataWithGroups) {
		if(dataWithGroups == null || dataWithGroups.isEmpty()) {
			return null;
		}
		Map<Integer, List<CollectionItemWithGroup<I, G>>> map = dataWithGroups.stream()
				.collect(Collectors.groupingBy(CollectionItemWithGroup<I, G>::getGroupId, 
						LinkedHashMap::new, 
						Collectors.toList()));
		List<G> groups = new ArrayList<>();
		for(List<CollectionItemWithGroup<I, G>> list: map.values()) {
			G group = list.get(0).getGroup();
			group.setList(list.stream()
					.map(i -> i.getItem())
					.collect(Collectors.toList()));

			groups.add(group);
		}
		return groups;
	}
	
	/**
	 * Same as getFilledGroups static method but probably less efficient because needs to compare full ProcessGroups for groupingby
	 */
	public static <I, G extends ListGroup<I>> List<G> getFilledGroupsByComparing(List<? extends CollectionItemWithGroup<I, G>> dataWithGroups) {
		if(dataWithGroups == null || dataWithGroups.isEmpty()) {
			return null;
		}
		Map<G, List<I>> map = dataWithGroups.stream()
				.collect(Collectors.groupingBy(CollectionItemWithGroup<I, G>::getGroup, 
						LinkedHashMap::new, 
						Collectors.mapping(CollectionItemWithGroup<I, G>::getItem, Collectors.toList())));
		List<G> groups = new ArrayList<>();
		map.forEach((k, v) -> {
			k.setList(v);
			groups.add(k);
		});
		return groups;
	}

	
	/**
	 * Used for building OO structure when fetching objects and their collections with 2 queries, 
	 * one for all data in nested collections and then another query for the groups. 
	 * Building List of groups, each filled with list of 'data items' (OO), when receiving a list of all 'data items' each containing a pointer to the group id.
	 * After grouping the items by group id, will use the given method to fetch the groups objects by their IDs 
	 * and then setting their corresponding collections of 'items'.
	 * @param <I> Class of 'item' within the group collection
	 * @param <G> Class of the 'group'
	 * @param dataWithGroupsId the inner join of the group id with it's collection of 'items'
	 * @param groupsFetchingFunction function to apply on set of group IDs to get the list of group objects
	 * @return List of groups with nested list of each corresponding collection set in OO structure.
	 */
	public static <I, G extends ListGroup<I>> List<G> getFilledGroups(List<? extends CollectionItemWithGroup<I, G>> dataWithGroupsId, 
			Function<Set<Integer>, List<G>> groupsFetchingFunction) {
		if(dataWithGroupsId.isEmpty()) {
			return null;
		}
		Map<Integer, List<I>> map = dataWithGroupsId.stream()
				.collect(Collectors.groupingBy(CollectionItemWithGroup<I, G>::getGroupId, 
						LinkedHashMap::new, 
						Collectors.mapping(CollectionItemWithGroup<I, G>::getItem, Collectors.toList())));
		List<G> processGroups = groupsFetchingFunction.apply(map.keySet());
		for(G g: processGroups) {
			g.setList(map.get(g.getId()));
		}
		return processGroups;
	}
	
	
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
}
