package com.rcm.app.dbConnector.dao;

import java.util.List;

import com.rcm.app.dbConnector.model.Connection;

public interface ConnectionDao {
	
	List<Connection> getConnections() throws Exception;
	
	void addNewConnection(Connection con) throws Exception;
	
	Connection getConnectionById(long conId) throws Exception;
	
    void updateConnection(Connection con) throws Exception;
    
    void deleteConnection(long conId) throws Exception;
    
    Boolean validateConnection(Connection con) throws Exception;
}
