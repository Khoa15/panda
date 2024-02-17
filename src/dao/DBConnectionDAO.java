package dao;

import model.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
public class DBConnectionDAO {
	public DBConnectionDAO() {}
	
	public static ResultSet Load(String storedProcedure) {
		try {
			String call = "{call " + storedProcedure + "}";
			Connection con = DBConnection.openConnection();
			CallableStatement callStatement = con.prepareCall(call);
			return callStatement.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet Load(String storedProcedure, Object id) {
		try {
			String call = "{call " + storedProcedure + "(?)}";
			Connection con = DBConnection.openConnection();
			CallableStatement callStatement = con.prepareCall(call);
			callStatement.setObject(1, id);
			return callStatement.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet Load(String storedProcedure, Object[] values) {
		try {
			CallableStatement callStatement = setCallable(storedProcedure, values);
			
			return callStatement.executeQuery();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int Update(String storedProcedure, Object[] values) {
		try {
			CallableStatement callStatement = setCallable(storedProcedure, values);
                        
			return callStatement.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/// Method set Call Statement with name stored procedure and set parameter values with Object[] values
	private static CallableStatement setCallable(String storedProcedure, Object[] values) {
		try {
                    String call = "{call " + storedProcedure;
                    if (values.length > 0) {
                        call += "(";
                        for (int i = 0; i < values.length - 1; i++) {
                            call += "?,";
                        }
                        call += "?";
                        call += ")";
                    }
                    call += "}";

                    Connection con = DBConnection.openConnection();
                    CallableStatement callStatement = con.prepareCall(call);

                    // Set parameter values
                    for (int i = 0; i < values.length; i++) {
                        callStatement.setObject(i + 1, values[i]);
                    }
                    return callStatement;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
