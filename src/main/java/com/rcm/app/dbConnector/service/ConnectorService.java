package com.rcm.app.dbConnector.service;

import java.util.List;

import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.model.DBDetails;


public interface ConnectorService {
	
	List<Connection> getAllConnections() throws Exception;
	
	void addNewConnection(Connection con) throws Exception;
	
	Connection getConById(long conId) throws Exception;
	
	void updateConnection(Connection con) throws Exception;
	
	void deleteConnection(long con) throws Exception;
	
	Boolean validateConnection(Connection con) throws Exception;
	
	DBDetails fetchDBDetails(Connection con) throws Exception; 
	
}
