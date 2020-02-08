/**
 * 
 */
package com.avc.mis.beta.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * @author Zvi
 *
 */
public class PreparedStatementCreatorImpl implements PreparedStatementCreator {
	
	private String sql;
	private Object[] parameters;
	private String[] columnNames;
	
	/**
	 * 
	 * @param sql
	 * @param parameters
	 * @param columnNames columns to be returned after the statement (e.g. generated columns).
	 */
	public PreparedStatementCreatorImpl(String sql, Object[] parameters, String[] columnNames) {
		this.sql = sql;
		this.parameters = parameters;
		this.columnNames = columnNames;
	}

	public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement(sql, columnNames);
        for(int i=0; i < parameters.length; i++) {
        	ps.setObject(i+1, parameters[i]);
        }
        return ps;
	}

}
