/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEMS_GROUP")
public class UsedItemsGroup extends ProcessInfoEntity {

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "group", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<UsedItem> usedItems = new HashSet<>();
	
	@Column(nullable = false)
	@NotBlank(message = "Group name is mandetory")
	private String groupName;

	public void setGroupName(String groupName) {
		this.groupName = Optional.ofNullable(groupName).map(s -> s.trim()).orElse(null);
	}
		
//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	@NotNull(message = "Group category is mandatory")
//	private GroupCategory category;
	
	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore
	@Column(nullable = false)
	private boolean tableView = false;
	
	public UsedItem[] getUsedItems() {
		return this.usedItems.toArray(new UsedItem[this.usedItems.size()]);
	}

	public void setUsedItems(UsedItem[] usedItems) {
		this.usedItems = Insertable.setReferences(usedItems, (t) -> {t.setReference(this);	return t;});
	}

	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
}
