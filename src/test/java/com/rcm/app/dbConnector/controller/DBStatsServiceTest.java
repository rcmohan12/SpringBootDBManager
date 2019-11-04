package com.rcm.app.dbConnector.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcm.app.dbConnector.model.ColumnProperties;
import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.model.DBDetails;
import com.rcm.app.dbConnector.service.ConnectorService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DBStatsServiceTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ConnectorService conService;
	
	@Test
	public void getMeta() throws Exception {
		
		when(conService.validateConnection(Mockito.any(Connection.class))).thenReturn(true);
		DBDetails dets = createRetObj();
		when(conService.fetchDBDetails(Mockito.any(Connection.class))).thenReturn(dets);
		
		mvc.perform(post("/db/list/meta")
				.param("id", "1")
				.param("username", "root")
				.param("password", "admin"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(content().json(getJson()));
		
	}
	
	private String getJson() throws JsonProcessingException {
		
		String returnObj = "";
		ObjectMapper mapper = new ObjectMapper();
		
		returnObj = mapper.writeValueAsString(createRetObj());
		
		
		return returnObj;
	}
	
	private String getJson2() throws JsonProcessingException {
		
		String returnObj = "";
		ObjectMapper mapper = new ObjectMapper();
		
		DBDetails d = createRetObj();
		d.setSchemaName("some schem name");
		returnObj = mapper.writeValueAsString(d);
		
		
		return returnObj;
	}
 	
	private DBDetails createRetObj() {
		
		DBDetails dbdetails = new DBDetails();
		Map<String, List<ColumnProperties>> tableColMap = new HashMap<String, List<ColumnProperties>>();
		Map<String, Map<Integer, List<String>>> tableDataMap = new HashMap<String, Map<Integer, List<String>>>();
		ColumnProperties col1 = new ColumnProperties();
		Map<Integer, List<String>> tableValMap = new HashMap<Integer, List<String>>();
		List<String> data = new ArrayList<String>();
		List<ColumnProperties> colPropList = new ArrayList<ColumnProperties>();
		
		col1.setField("id");
		col1.setType("bigint(20) unsigned");
		col1.setDefaultVal("null");
		col1.setKey("PRI");
		col1.setNullable("NO");
		colPropList.add(col1);
		
		tableColMap.put("userdetails", colPropList);
		
		data.add("1");
		tableValMap.put(1, data);
		
		data = new ArrayList<>();
		data.add("2");
		tableValMap.put(2, data);
		
		data = new ArrayList<>();
		data.add("3");
		tableValMap.put(3, data);
		
		tableDataMap.put("userdetails", tableValMap);
		
		dbdetails.setTableColMap(tableColMap);
		dbdetails.setTableDataMap(tableDataMap);
		
		
		return dbdetails;
	}
	
}
