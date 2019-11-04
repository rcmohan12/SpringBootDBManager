package com.rcm.app.dbConnector.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.util.ConnectionUtils;
import com.rcm.app.dbConnector.util.Constants;


@Transactional
@Repository
public class ConnectionDaoImpl extends Constants implements ConnectionDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionDaoImpl.class);
	private final String query = "FROM Connection";
	
	@PersistenceContext	
	private EntityManager entityManager;
	
	/**
	 * This method fetches all the connections defined by the user
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Connection> getConnections() throws Exception {
		LOGGER.info("getConnections :"+START);
		
		List<Connection> cons = new ArrayList<>();
		
		cons = (List<Connection>) entityManager.createQuery(query).getResultList();
		
//		throw new Exception();
		
		LOGGER.info("getConnections :"+END);
		return cons;
	}

	/**
	 * This method takes in a connection object defined by user and saves it to the DB
	 * 
	 * @param con
	 * @throws Exception
	 */
	@Override
	public void addNewConnection(Connection con) {
		LOGGER.info("addNewConnection :"+START);
		
		entityManager.persist(con);
	
		LOGGER.info("addNewConnection :"+END);
	}

	/**
	 * This method accepts the connection id and retrieves the corresponding connection from the DB
	 * 
	 * @param conId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Connection getConnectionById(long conId) {
		LOGGER.info("getConnectionById :"+START);

		return entityManager.find(Connection.class, conId);
	}

	/**
	 * This method accepts a connection object that has been modified by the user and updates the corresponding record in the DB
	 * 
	 * @param con
	 * @throws Exception
	 */
	@Override
	public void updateConnection(Connection con) {
		LOGGER.info("updateConnection :"+START);
		
		LOGGER.debug("Connection ID :"+con.getId());
		
		Connection conToUpdate = getConnectionById(con.getId());
		
		conToUpdate.setDbName(con.getDbName());
		conToUpdate.setHostname(con.getHostname());
		conToUpdate.setPort(con.getPort());
		conToUpdate.setUsername(con.getUsername());
		conToUpdate.setPassword(con.getPassword());
		
		entityManager.flush();
		
		LOGGER.info("updateConnection :"+END);
	}

	 /**
     * This method accepts the con id (PK) of a particular connection and uses it to find and delete corresponding record in the DB
     * 
     * @param conId
     * @throws Exception
     */
	@Override
	public void deleteConnection(long conId) {
		LOGGER.info("deleteConnection :"+START);
		
		entityManager.remove(getConnectionById(conId));
		
		LOGGER.info("deleteConnection :"+END);
	}

	/**
     * This method uses the connection object details to validate 
     * 
     * @param con
     * @return
     * @throws Exception
     */
	@Override
	public Boolean validateConnection(Connection con) throws Exception {
		LOGGER.info("validateConnection :"+START);
		
		Connection dbCon = getConnectionById(con.getId());
		if(dbCon != null && ConnectionUtils.encrypt(con.getPassword()).equals(dbCon.getPassword())) {
			con.setDbName(dbCon.getDbName());
			con.setHostname(dbCon.getHostname());
			con.setPort(dbCon.getPort());
			con.setUsername(dbCon.getUsername());
			LOGGER.info("validateConnection :"+END);
			return true;
		} else {
			
			LOGGER.error("validateConnection :"+END);
			throw new Exception("Connection Doesnt Exist. Recheck values");
		}
		
	}

	
}
