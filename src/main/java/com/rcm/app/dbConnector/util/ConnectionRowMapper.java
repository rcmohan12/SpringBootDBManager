package com.rcm.app.dbConnector.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rcm.app.dbConnector.model.Connection;

public class ConnectionRowMapper implements RowMapper<Connection>{

	@Override
	public Connection mapRow(ResultSet rs, int arg1) throws SQLException {
		
		Connection con = new Connection();
		
        con.setHostname(rs.getString("hostname"));
        con.setPort(rs.getInt("port"));
        con.setDbName(rs.getString("dbName"));

        return con;
	}

	
	
}
