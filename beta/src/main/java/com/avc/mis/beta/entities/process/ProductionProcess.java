/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.avc.mis.beta.entities.data.Staff;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PROCESSES")
public class ProductionProcess {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private final Date insertTime;
	
	@ManyToOne 
	@JoinColumn(name = "staffId")
	private Staff staffRecording;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "POid", updatable = false)
	private PO po;
	
	@ManyToOne 
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	private ProcessType processType;
	
	@ManyToOne
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	private Long duration;//milliseconds
	private Integer numOfWorkers;
	
	@ManyToOne 
	@JoinColumn(name = "statusId"/*, nullable = false*/)
	private ProcessStatus status;
	private String remarks;
	
	public ProductionProcess() {
		this.insertTime = new Date(System.currentTimeMillis());
	}
	
}
