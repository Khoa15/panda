package dao;

import model.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.Statement;
import oracle.jdbc.OracleTypes;

public class DBConnectionDAO {

    public DBConnectionDAO() {
    }
    
    public static ResultSet ExecuteSelectQuery(String query){
        try{
            Connection con = DBConnection.getConn();
            Statement statement =con.createStatement();
            return (ResultSet)statement.executeQuery(query);
        }catch(Exception e){
            return null;
        }
    }

    public static ResultSet Load(String storedProcedure) {
        try {
            String call = "{call PANDA." + storedProcedure + "(?)}";
            //Connection con = DBConnection.openConnection();
            Connection con = DBConnection.getConn();
            CallableStatement callStatement = con.prepareCall(call);
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet)callStatement.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet Load(String storedProcedure, Object id) {
        try {
            String call = "{call PANDA." + storedProcedure + "(?, ?)}";
            //Connection con = DBConnection.openConnection();
            Connection con = DBConnection.getConn();
            CallableStatement callStatement = con.prepareCall(call);
            callStatement.setObject(1, id);
            callStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet)callStatement.getObject(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet Load(String storedProcedure, Object[] values) {
        try {            
            CallableStatement callStatement = setCallable(storedProcedure, values, true);
            callStatement.registerOutParameter(values.length+ 1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(values.length + 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int Update(String storedProcedure, Object[] values) {
        try {
            CallableStatement callStatement = setCallable(storedProcedure, values);
            int rs = callStatement.executeUpdate();
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    /// Method set Call Statement with name stored procedure and set parameter values with Object[] values

    private static CallableStatement setCallable(String storedProcedure, Object[] values) {
        try {
            String call = "{call PANDA." + storedProcedure;
            if (values.length > 0) {
                call += "(";
                for (int i = 0; i < values.length - 1; i++) {
                    call += "?,";
                }
                call += "?";
                call += ")";
            }
            call += "}";

            //Connection con = DBConnection.openConnection();
            Connection con = DBConnection.getConn();
            CallableStatement callStatement = con.prepareCall(call);

            // Set parameter values
            for (int i = 0; i < values.length; i++) {
                callStatement.setObject(i + 1, values[i]);
            }
            return callStatement;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static CallableStatement setCallable(String storedProcedure, Object[] values, boolean isSelect){
        try {
            String call = "{call PANDA." + storedProcedure;
            if (values.length > 0) {
                call += "(";
                for (int i = 0; i < values.length; i++) {
                    call += "?,";
                }
                call += "?";
                call += ")";
            }
            call += "}";

            //Connection con = DBConnection.openConnection();
            Connection con = DBConnection.getConn();
            CallableStatement callStatement = con.prepareCall(call);

            // Set parameter values
            for (int i = 0; i < values.length; i++) {
                callStatement.setObject(i + 1, values[i]);
            }
            return callStatement;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
