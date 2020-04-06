/**
 * 
 */
package com.avc.mis.beta.entities.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.ProcessDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@BatchSize(size = DAO.BATCH_SIZE)
@Table(name = "USER_MESSAGES")
public class UserMessage extends ProcessDataEntity {
			
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && this.getUser() != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Message needs to belong to process and user";
	}

}
