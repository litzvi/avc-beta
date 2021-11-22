/**
 * 
 */
package com.avc.mis.beta.entities.process.storages;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Used to differentiate extra added(bonus) items 
 * versus the amounts received as part of the order.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "EXTRA_ADDED")
@PrimaryKeyJoinColumn(name = "storageId")
public class ExtraAdded extends StorageWithSample {

	{
		setDtype("ExtraAdded");
	}

}
