package com.avc.mis.beta.dto.queryRows;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;

import lombok.Value;

@Value
public class PoProcessItemEntry {
	
	PoCodeDTO itemPo;
	ProcessItemDTO processItem;
	
}
