/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zvi
 *
 */
//@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public interface CollectionItemWithGroup<S, G extends ListGroup<S>> {
	
//	public SubjectDataWithGroup(@NonNull Integer id) {
//		super(id);
//	}
	
	public default Integer getGroupId() {
		return getGroup().getId();
	}
	
	public abstract S getItem();
	
	public abstract G getGroup();
	
	
	public static <S, G extends ListGroup<S>> List<G> getFilledGroups(List<? extends CollectionItemWithGroup<S, G>> dataWithGroups) {
		Map<Integer, List<CollectionItemWithGroup<S, G>>> map = dataWithGroups.stream()
				.collect(Collectors.groupingBy(CollectionItemWithGroup<S, G>::getGroupId, LinkedHashMap::new, Collectors.toList()));
		List<G> processGroups = new ArrayList<>();
		for(List<CollectionItemWithGroup<S, G>> list: map.values()) {
			G processGroup = list.get(0).getGroup();
			processGroup.setList(list.stream()
					.map(i -> i.getItem())
					.collect(Collectors.toList()));

			processGroups.add(processGroup);
		}
		return processGroups;
	}
	
	/**
	 * Same as getFilledGroups static method but probably less efficient because needs to compare full ProcessGroups for groupingby
	 */
	public static <S, G extends ListGroup<S>> List<G> getFilledGroupsByComparing(List<? extends CollectionItemWithGroup<S, G>> dataWithGroups) {
		Map<G, List<S>> map = dataWithGroups.stream()
				.collect(Collectors.groupingBy(CollectionItemWithGroup<S, G>::getGroup, 
						LinkedHashMap::new, 
						Collectors.mapping(CollectionItemWithGroup<S, G>::getItem, Collectors.toList())));
		List<G> processGroups = new ArrayList<>();
		map.forEach((k, v) -> {
			k.setList(v);
			processGroups.add(k);
		});
		return processGroups;
	}
}
