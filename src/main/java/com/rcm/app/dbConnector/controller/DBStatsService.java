package com.rcm.app.dbConnector.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rcm.app.dbConnector.model.ColumnsData;
import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.model.DBDetails;
import com.rcm.app.dbConnector.model.TablesData;
import com.rcm.app.dbConnector.service.ConnectorService;
import com.rcm.app.dbConnector.util.Constants;

import io.swagger.annotations.ApiOperation;

/**
 * Controller to handle all REST requests
 * 
 * @author rcm
 *
 */

@RestController
@RequestMapping("/db/list")
public class DBStatsService extends Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBStatsService.class);
	
	@Autowired
	private ConnectorService conService;
	
	@ApiOperation(value="Provides details of tables, columns, types and the table data")
	@PostMapping(path = "/meta", produces = { "application/json" })
	public DBDetails getDbData(Connection con) throws Exception {
		LOGGER.info("getDbData :"+START);
		
		DBDetails meta = null;
		try {
			if(conService.validateConnection(con)) {

				meta = conService.fetchDBDetails(con);
				return meta;
			} 
		} catch (Exception e) {
			LOGGER.error(ERROR + " : " + e.getMessage());
			throw new Exception("Failed to retrieve table Data. Check with support");
		}
		LOGGER.info("getDbData :"+END);
		return meta;
	}

	@ApiOperation(value="Provides details about tables, attributes & records count")
	@PostMapping(path = "/tableStats", produces = { "application/json" })
	public List<TablesData> getTableStats(Connection con) throws Exception {
		LOGGER.info("getTableStats :"+START);
		
		List<TablesData> meta = null;
		try {
			if (conService.validateConnection(con)) {
				
				meta = conService.fetchTablesData(con);

				return meta;
			} 
		} catch (Exception e) {
			LOGGER.error(ERROR + " : " + e.getMessage());
			throw new Exception("Failed to retrieve table Data. Check with support");
		}
		
		LOGGER.info("getTableStats :"+END);
		return meta;
	}
	
	@ApiOperation(value="Provides column stats like min, max, average and mean values for applicable columns")
	@PostMapping(path = "/colStats", produces = { "application/json" })
	public Map<String, List<ColumnsData>> getColStats(Connection con) throws Exception {
		LOGGER.info("getColStats :"+START);
		
		Map<String, List<ColumnsData>> meta = null;
		try {
			if (conService.validateConnection(con)) {
				
				meta = conService.fetchColumnsData(con);

				return meta;
			} 
		} catch (Exception e) {
			LOGGER.error(ERROR + " : " + e.getMessage());
			throw new Exception("Failed to retrieve table Data. Check with support");
		}
		LOGGER.info("getColStats :"+END);
		return meta;
	}
	
}
