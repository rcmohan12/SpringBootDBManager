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
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rcm.app.dbConnector.dao.ConnectionDao;
import com.rcm.app.dbConnector.dao.ConnectionDaoImpl;
import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.model.DBDetails;
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
		
		java.sql.Connection conn = null;
		Map<String, Set<String>> TableColMap = new HashMap<String, Set<String>>();
		Map<String, Map<Integer, List<String>>> tableDataMap = new HashMap<String, Map<Integer,List<String>>>();
		Set<String> tables = new HashSet<String>();
		Set<String> columns = new HashSet<String>();
		List<String> data = new ArrayList<String>();
		Map<Integer, List<String>> colIdMap = new HashMap<Integer, List<String>>();
		Statement stmt = null;
		DBDetails details = new DBDetails();
		
		String conStr = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", con.getHostname(), con.getDbName(), con.getUsername(), con.getPassword());
		
		try {
			conn = DriverManager.getConnection(conStr);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Show Tables");

			/*Fetch the list of tables in the db and add to the set*/
			while (rs.next()) {
				tables.add(rs.getObject(1).toString());
			}
			
			/*Provided there are tables, loop through the set prepared earlier and fetch the columns (along with column type type) for each of the tables*/
			if (tables.size() > 0) {
				for (String tableName : tables) {
					LOGGER.debug("Table :"+tableName);
					ResultSet rsCol = stmt.executeQuery("Show columns from " + tableName);
					columns = new HashSet<String>();
					int col = 1;
					while (rsCol.next()) {
						LOGGER.debug("Type :" + rsCol.getMetaData().getColumnTypeName(col));
						columns.add(rsCol.getObject(1).toString() + "(" + rsCol.getMetaData().getColumnTypeName(col) + ")");
						col++;
					}
					TableColMap.put(tableName, columns);
				}
			}
			
			/*Loop through the map prepared earlier to fetch the column data for each of the tables. 
			 *The column count is used to loop through the result set to retrieve corresponding column data entries.
			 */
			for (Map.Entry<String, Set<String>> entry : TableColMap.entrySet()) {
				data = new ArrayList<String>();
				colIdMap = new HashMap<Integer, List<String>>();
				
				LOGGER.debug("Table :"+entry.getKey());
				LOGGER.debug("Column size :"+entry.getValue().size());

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
			throw new Exception(e);
		} 
		return details;
	}

}
