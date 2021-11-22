/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.group.ProductionPlanRow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCTION_PLANS")
@PrimaryKeyJoinColumn(name = "processId")
public class ProductionPlan extends GeneralProcess {

	
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProductionPlanRow> productionPlanRows = new HashSet<>();
	

	public void setProductionPlanRows(Set<ProductionPlanRow> productionPlanRows) {
		this.productionPlanRows = Insertable.setReferences(productionPlanRows, (t) -> {t.setReference(this);	return t;});
	}
}
