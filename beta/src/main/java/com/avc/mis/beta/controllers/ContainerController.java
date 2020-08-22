package com.avc.mis.beta.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.DataObjectWithName;
import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.view.ItemInventoryRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.RawQcRow;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.service.Loading;
import com.avc.mis.beta.service.ObjectTablesReader;
import com.avc.mis.beta.service.QualityChecks;
import com.avc.mis.beta.service.ValueTablesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(path = "/api/container")
public class ContainerController {

	@Autowired
	private Loading loading;
	
	
	
	
	@PostMapping("/addLoading")
	public ContainerLoadingDTO addLoading(@RequestBody ContainerLoading load) {
		loading.addLoading(load);
		return loading.getLoading(load.getId());
	}
	
	@PutMapping("/editLoading")
	public ContainerLoadingDTO editLoading(@RequestBody ContainerLoading load) {
		loading.editLoading(load);
		return loading.getLoading(load.getId());
	}
	
	@RequestMapping("/getLoading/{id}")
	public ContainerLoadingDTO getLoading(@PathVariable("id") int processId) {
		return loading.getLoading(processId);
	}
	
	@RequestMapping("/getAllLoadings")
	public List<LoadingRow> getAllLoadings() {
		return loading.getLoadings();
	}
	
}
