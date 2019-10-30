package com.rcm.app.dbConnector.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.annotations.ApiModelProperty;

public class DBDetails {

	@ApiModelProperty(notes="Schema name (if applicable for the DB)")
	String schemaName;
	
	@ApiModelProperty(notes="Map object with key as table name and value and list of associated columns (with their data type and table association) ")
	Map<String, List<String>> tableColMap;
	
	@ApiModelProperty(notes="Map with Key as table name and value table entries (records)")
	Map<String, Map<Integer, List<String>>> tableDataMap;
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public Map<String, List<String>> getTableColMap() {
		return tableColMap;
	}
	public void setTableColMap(Map<String, List<String>> tableColMap) {
		this.tableColMap = tableColMap;
	}
	public Map<String, Map<Integer, List<String>>> getTableDataMap() {
		return tableDataMap;
	}
	public void setTableDataMap(Map<String, Map<Integer, List<String>>> tableDataMap) {
		this.tableDataMap = tableDataMap;
	}
	
	
}
