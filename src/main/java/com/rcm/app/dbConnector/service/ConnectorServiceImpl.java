package com.rcm.app.dbConnector.service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rcm.app.dbConnector.dao.ConnectionDao;
import com.rcm.app.dbConnector.model.ColumnProperties;
import com.rcm.app.dbConnector.model.ColumnsData;
import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.model.DBDetails;
import com.rcm.app.dbConnector.model.TablesData;
import com.rcm.app.dbConnector.util.ConnectionUtils;
import com.rcm.app.dbConnector.util.Constants;

@Component
public class ConnectorServiceImpl extends Constants implements ConnectorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorServiceImpl.class);
	
	@Autowired
	ConnectionDao daoObj;
	
	@Override
	public List<Connection> getAllConnections() throws Exception {
		
		LOGGER.info("getAllConnections :"+START);
		return daoObj.getConnections();
	}

	@Override
	public void addNewConnection(Connection con) throws Exception {
		LOGGER.info("addNewConnection :"+START);
		
		con.setPassword(ConnectionUtils.encrypt(con.getPassword()));
		daoObj.addNewConnection(con);
		
		LOGGER.info("addNewConnection :"+END);
	}

	@Override
	public Connection getConById(long conId) throws Exception {
		LOGGER.info("getConById :"+START);
		
		Connection con = new Connection();
		Connection returnCon = new Connection();
		
		con = daoObj.getConnectionById(conId);
		
		returnCon.setId(con.getId());
		returnCon.setDbName(con.getDbName());
		returnCon.setHostname(con.getHostname());
		returnCon.setUsername(con.getUsername());
		returnCon.setPort(con.getPort());
		
		LOGGER.info("getConById :"+END);
		return returnCon;
	}

	@Override
	public void updateConnection(Connection con) throws Exception {
		LOGGER.info("updateConnection :"+START);
		
		con.setPassword(ConnectionUtils.encrypt(con.getPassword()));
		daoObj.updateConnection(con);
		
		LOGGER.info("updateConnection :"+END);
		
	}

	@Override
	public void deleteConnection(long conId) throws Exception {
		LOGGER.info("deleteConnection :"+START);
		
		daoObj.deleteConnection(conId);
		
		LOGGER.info("deleteConnection :"+END);
	}

	@Override
	public Boolean validateConnection(Connection con) throws Exception {
		
		LOGGER.info("validateConnection :"+START);
		return daoObj.validateConnection(con);
		
	}

	@Override
	public DBDetails fetchDBDetails(Connection con) throws Exception {
		LOGGER.info("fetchDBDetails :"+START);
				
		String conStr = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", con.getHostname(), con.getDbName(), con.getUsername(), con.getPassword());
		java.sql.Connection conn = null;
		/*Key : table name, Value: list of columns*/
		Map<String, List<ColumnProperties>> TableColMap = new HashMap<String, List<ColumnProperties>>();
		/*Key : table name, Value: Map with key as row count and value as row entries inserted into a list*/
		Map<String, Map<Integer, List<String>>> tableDataMap = new HashMap<String, Map<Integer,List<String>>>();
		Set<String> tables = null;
		List<String> columns = null;
		List<String> data = null;
		ColumnProperties cProp = null;
		List<ColumnProperties> cPropList = null;
		/*Map with key as row count and value as row entries inserted into a list*/
		Map<Integer, List<String>> colIdMap = new HashMap<Integer, List<String>>();
		Statement stmt = null;
		DBDetails details = new DBDetails();
		
		try {
			conn = DriverManager.getConnection(conStr);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Show Tables");

			/*Fetch the list of tables in the db and add to the set*/
			tables = new HashSet<String>();
			while (rs.next()) {
				tables.add(rs.getObject(1).toString());
			}
			/*Provided there are tables, loop through the set prepared earlier and fetch the columns (along with column type type) for each of the tables*/
			if (tables.size() > 0) {
				for (String tableName : tables) {
					LOGGER.debug("Table :"+tableName);
					ResultSet rsCol = stmt.executeQuery("Show columns from " + tableName);
					columns = new ArrayList<String>();
					int col = 1;
					cPropList = new ArrayList<ColumnProperties>();
					while (rsCol.next()) {
						cProp = new ColumnProperties();
						
						cProp.setField(rsCol.getObject(1).toString());
						cProp.setType(rsCol.getObject(2).toString());
						cProp.setNullable(rsCol.getObject(3).toString());
						cProp.setKey(rsCol.getObject(4).toString());
						cProp.setDefaultVal(""+rsCol.getObject(5));
						columns.add(rsCol.getObject(1).toString() + "(" + rsCol.getMetaData().getColumnTypeName(col) + ")");
						cPropList.add(cProp);	
					}
					TableColMap.put(tableName, cPropList);
				}
			}
			/*Loop through the map prepared earlier to fetch the column data for each of the tables. 
			 *The column count is used to loop through the result set to retrieve corresponding column data entries.
			 */
			for (Map.Entry<String, List<ColumnProperties>> entry : TableColMap.entrySet()) {
				data = new ArrayList<String>();
				colIdMap = new HashMap<Integer, List<String>>();

				ResultSet rsdata = stmt.executeQuery("select * from " + entry.getKey());
				int rowNo = 1;
				while (rsdata.next()) {
					data = new ArrayList<String>();
					for (int i = 1; i <= entry.getValue().size(); i++) {
						LOGGER.debug("data :"+ rsdata.getObject(i).toString());
						data.add(rsdata.getObject(i).toString());
					}
					colIdMap.put(rowNo, data);
					rowNo++;
				}
				tableDataMap.put(entry.getKey(), colIdMap);
			}

			details.setTableColMap(TableColMap);
			details.setTableDataMap(tableDataMap);

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error(ERROR + " : " +e.getMessage());
			throw new SQLException(e);
		} 
		return details;
	}
	

	/**
	 * This method returns table data consisting of table name, columns and their types, records associated with each table
	 */
	@Override
	public List<TablesData> fetchTablesData(Connection con) throws Exception {
		LOGGER.info("fetchTablesData :"+START);
		
		List<TablesData> tablesData = new ArrayList<TablesData>();
		Set<String> tables = new HashSet<String>();
		List<String> attributes = new ArrayList<String>();
		TablesData tableData = null;
		java.sql.Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		String conStr = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", con.getHostname(), con.getDbName(), con.getUsername(), con.getPassword());
		
		conn = DriverManager.getConnection(conStr);
		stmt = stmt2 = conn.createStatement();
		ResultSet rs = stmt.executeQuery("Show Tables");
		
		
		/*Fetch the list of tables in the db and add to the set*/
		while (rs.next()) {
			tables.add(rs.getObject(1).toString());
		}
		if (tables.size() > 0) {
			for (String tableName : tables) {
				tableData = new TablesData();
				tableData.setTableName(tableName);
				ResultSet rsCol = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name like '" + tableName +"'");
				attributes = new ArrayList<>();
				while (rsCol.next()) {
					attributes.add(rsCol.getObject(1).toString());
				}
				rsCol.close();

				tableData.setAttributes(attributes);
				ResultSet rsRowCount = stmt2.executeQuery("SELECT count(*) FROM " + tableName);
				rsRowCount.next();
				tableData.setRecordCount(Integer.valueOf(rsRowCount.getObject(1).toString()));
				
				tablesData.add(tableData);
				
			}
		}
		
		conn.close();
		
		LOGGER.info("fetchTablesData :"+END);
		return tablesData;
		
	}

	/**
	 * This method builds column stats like min, max, average & median for an applicable column
	 * 
	 */
	@Override
	public Map<String, List<ColumnsData>> fetchColumnsData(Connection con) throws Exception {
		LOGGER.info("fetchColumnsData :"+START);
		
		Map<String, List<ColumnsData>> columnData = new HashMap<String, List<ColumnsData>>();
		List<ColumnsData> cData = new ArrayList<ColumnsData>();
		Set<String> tables = new HashSet<String>();
		String conStr = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", con.getHostname(), con.getDbName(), con.getUsername(), con.getPassword());
		java.sql.Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		
		conn = DriverManager.getConnection(conStr);
		stmt = conn.createStatement();
		stmt1 = conn.createStatement();
		stmt2 = conn.createStatement();
		ResultSet rs = stmt.executeQuery("Show Tables");
		while (rs.next()) {
			String tname = rs.getObject(1).toString();
			tables.add(rs.getObject(1).toString());
			ResultSet rsCol = stmt1.executeQuery("Show Columns from " + rs.getObject(1).toString());
			cData = new ArrayList<ColumnsData>();
			while(rsCol.next()) {
				if(rsCol.getObject(2).toString().contains("int")) {
					
					ResultSet rsCol2 = stmt2.executeQuery("SELECT min("+rsCol.getObject(1)+") , max("+rsCol.getObject(1)+"), avg("+rsCol.getObject(1)+") from "+tname);
					ColumnsData colData = new ColumnsData();
					colData.setColName(rsCol.getObject(1).toString());
					rsCol2.next();
					
					if(rsCol2.getObject(1) != null) {
						colData.setMin(rsCol2.getObject(1).toString());
					}
					
					if(rsCol2.getObject(2) != null) {
						colData.setMax(rsCol2.getObject(2).toString());
					}
					
					if(rsCol2.getObject(3) != null) {
						colData.setAvg(rsCol2.getObject(3).toString());
					}
					
					cData.add(colData);
				}
			}
			columnData.put(tname, cData);
		}
		
		conn.close();
		
		LOGGER.info("fetchColumnsData :"+END);
		return columnData;
	}

}
