///**
// * 
// */
//package com.avc.mis.beta.entities.process;
//
//import javax.persistence.FetchType;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MappedSuperclass;
//import javax.validation.constraints.NotNull;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
///**
// * Process that also refers to a specific single PO#
// * 
// * @author Zvi
// * 
// */
//@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
//@ToString(callSuper = true)
////@Entity
////@Table(name = "PO_PROCESSES")
////@PrimaryKeyJoinColumn(name = "processId")
//@MappedSuperclass
//public abstract class ExportProcess extends GeneralProcess {	
//
//	@NotNull(message = "Shipment code is mandatory")
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(nullable = false, name = "shipment_code_code")
//	private ShipmentCode shipmentCode;
//	
//	
//}
