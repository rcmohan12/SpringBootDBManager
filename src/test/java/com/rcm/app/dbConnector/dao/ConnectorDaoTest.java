package com.rcm.app.dbConnector.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.util.ConnectionUtilsTest;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class ConnectorDaoTest {
	
	@Autowired
	ConnectionDao conDao;
	
	@Test
	public void addNewConnection() throws Exception {
			Connection con = createNewConObj();
			conDao.addNewConnection(con);
			assert(con.getId() != 0);
	}
	
	@Test(expected = Exception.class)
	public void addNewConnectionFail() throws Exception {
		
			Connection con = createNewConObj();
			con.setPort(6596255);
			conDao.addNewConnection(con);
			assert(con.getId() == 0);
		
	}
	
	@Test
	public void getConnectionById() throws Exception{
			Connection con = conDao.getConnectionById(1);
			assert(con != null);
	}
	
	@Test
	public void validateConnection() throws Exception {
			Connection con = createNewConObj();
			Connection validateCon = createNewConObj();
			
			conDao.addNewConnection(con);
			
			validateCon.setId(con.getId());
			validateCon.setPassword("admin");//as passed by user, hence create a new obj and reset password & obj ID
			
			Boolean isValidated = conDao.validateConnection(validateCon);
			assert(isValidated != false);
		
	}
	
	
	@Test(expected = Exception.class)
	public void validateConnectionFail() throws Exception {
		
			Connection con = createNewConObj();
			conDao.addNewConnection(con);
			con.setPassword("updated");
			
			conDao.validateConnection(con);
		
	}
	
	@Test
	public void deleteConnection() throws Exception {

		Connection con = createNewConObj();
		assert (con.getId() == 0);

		conDao.addNewConnection(con);
		assert (con.getId() != 0);

		conDao.deleteConnection(con.getId());

		Connection retrieveCon = conDao.getConnectionById(con.getId());
		assert(retrieveCon == null);

	}
	
	@Test(expected = Exception.class)
	public void deleteConnectionFail() throws Exception {

		Connection con = createNewConObj();
		Connection deleteAgain = createNewConObj();
		assert (con.getId() == 0);

		conDao.addNewConnection(con);
		assert (con.getId() != 0);
		deleteAgain.setId(con.getId());

		conDao.deleteConnection(con.getId());

		Connection retrieveCon = conDao.getConnectionById(con.getId());
		assert(retrieveCon == null);
		
		conDao.deleteConnection(deleteAgain.getId());

	}
	
	private Connection createNewConObj() {
		Connection newCon = new Connection();
		
		newCon.setHostname("localost");
		newCon.setPort(3306);
		newCon.setDbName("test");
		newCon.setUsername("root");
		newCon.setPassword(ConnectionUtilsTest.encrypt("admin"));
		return newCon;
	}

}
