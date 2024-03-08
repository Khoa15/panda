package dao;

import model.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLType;
import java.sql.Types;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.OracleTypes;

public class DBConnectionDAO {

    public DBConnectionDAO() {
    }
    
    public static ResultSet ExecuteSelectQuery(String query) {
        try {
            Connection con = DBConnection.getConn();
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean ExecuteScalarQuery(String query) {
        try {
            Connection con = DBConnection.getConn();
            Statement statement = con.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet Load(String storedProcedure) {
        try {
            String call = "{call PANDA." + storedProcedure + "(?)}";
            Connection con = DBConnection.getConn();
            CallableStatement callStatement = con.prepareCall(call);
            //Connection con = DBConnection.openConnection();
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally{
            
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
            return (ResultSet) callStatement.getObject(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet Load(String storedProcedure, Object[] values) {
        try {
            values = add(values, null);
            CallableStatement callStatement = setCallable(storedProcedure, values, false);
            callStatement.registerOutParameter(values.length + 1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(values.length + 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int Update(String storedProcedure, Object[] values) {
        try {
            CallableStatement callStatement = setCallable(storedProcedure, values, false);
            Object rs = callStatement.execute();
            //int rs = callStatement.executeUpdate();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean CallProcedureNoParameter(String procedure, Object[] values) throws Exception {
        try {
            CallableStatement callStatement = setCallable(procedure, values, false);
            Object x = callStatement.execute();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    public static ResultSet CallProcedure(String procedure, Object[] values) {
        try {
            values = add(values, null);
            CallableStatement callStatement = setCallable(procedure, values, false);
            callStatement.registerOutParameter(values.length + 1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(values.length + 1);
        } catch (Exception e) {
            return null;
        }
    }

    public static ResultSet CallFunction(String function) {
        try {
            CallableStatement callStatement = DBConnection.getConn().prepareCall("{? = call " + function + "}");
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object CallFunction(String function, Object[] values, Object type) {
        try {
            CallableStatement callStatement = setCallable(function, values, true);
            callStatement.registerOutParameter(1, (int)type);

            callStatement.execute();
            return (Object) callStatement.getObject(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static CallableStatement setCallable(String storedProcedure, Object[] values, boolean isFunction) {
        try {
            String call = "{";
            if (isFunction) {
                call += "? = ";
            }
            call += "call PANDA." + storedProcedure;
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
            int i = (isFunction) ? 1 : 0;
            int length = values.length + i;
            for (; i < length; i++) {
                Object value = values[i - ((isFunction) ? 1 : 0)];
                callStatement.setObject(i + 1, value);
            }
            return callStatement;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object[] add(Object[] data, Object x) {
        Object[] newArray = new Object[data.length + 1];
        System.arraycopy(data, 0, newArray, 0, data.length); // Copy to the correct destination array
        newArray[newArray.length - 1] = x;
        return newArray;
    }

}
