package com.rcm.app.dbConnector.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBDetails {

	String schemaName;
	Map<String, Set<String>> tableColMap;
	Map<String, Map<Integer, List<String>>> tableDataMap;
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public Map<String, Set<String>> getTableColMap() {
		return tableColMap;
	}
	public void setTableColMap(Map<String, Set<String>> tableColMap) {
		this.tableColMap = tableColMap;
	}
	public Map<String, Map<Integer, List<String>>> getTableDataMap() {
		return tableDataMap;
	}
	public void setTableDataMap(Map<String, Map<Integer, List<String>>> tableDataMap) {
		this.tableDataMap = tableDataMap;
	}
	
	
}
