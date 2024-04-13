package dao;

import model.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLType;
import java.sql.Types;
import java.sql.Blob;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static Boolean ExecuteScalarQuery(String query) throws Exception {
        try {
            Connection con = DBConnection.getConn();
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ResultSet Load(String storedProcedure) {
        CallableStatement callStatement = null;
        try {
            String call = "{call PANDA." + storedProcedure + "(?)}";
            Connection con = DBConnection.getConn();
            callStatement = con.prepareCall(call);
            //Connection con = DBConnection.openConnection();
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet Load(String storedProcedure, Object id) {
        CallableStatement callStatement = null;
        try {
            String call = "{call PANDA." + storedProcedure + "(?, ?)}";
            //Connection con = DBConnection.openConnection();
            Connection con = DBConnection.getConn();
            callStatement = con.prepareCall(call);
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
        CallableStatement callStatement = null;
        try {
            values = add(values, null);
            callStatement = setCallable(storedProcedure, values, false);
            callStatement.registerOutParameter(values.length + 1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(values.length + 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int Update(String storedProcedure, Object[] values) throws Exception {
        CallableStatement callStatement = null;
        try {
            callStatement = setCallable(storedProcedure, values, false);
            Object rs = callStatement.execute();
            return 0;

        } catch (SQLException e) {
            throw e;
        }
    }

    public static boolean CallProcedureNoParameter(String procedure, Object[] values) throws Exception {
        CallableStatement callStatement = null;
        try {
            callStatement = setCallable(procedure, values, false);
            Object x = callStatement.execute();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ResultSet CallProcedure(String procedure, Object[] values) throws Exception {
        CallableStatement callStatement = null;
        ResultSet resultSet = null;
        try {
            values = add(values, null);
            callStatement = setCallable(procedure, values, false);
            callStatement.registerOutParameter(values.length + 1, OracleTypes.CURSOR);
            callStatement.execute();
            resultSet = (ResultSet) callStatement.getObject(values.length + 1);
        } catch (Exception e) {
            throw e;
        }
        return resultSet;
    }

    public static boolean CallProcedureNoParameterOut(String procedure, Object[] values) throws Exception {
        CallableStatement callStatement = null;
        try {
            callStatement = setCallable(procedure, values, false);
            callStatement.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet CallFunction(String function) {
        CallableStatement callStatement = null;
        try {
            callStatement = DBConnection.getConn().prepareCall("{? = call PANDA." + function + "}");
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            ResultSet rs = (ResultSet) callStatement.getObject(1);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    public static ResultSet CallFunction(String function, Object[] values) {
        CallableStatement callStatement = null;
        try {
            callStatement = setCallable(function, values, true);
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet CallFunction(String function, Object value) {
        CallableStatement callStatement = null;
        try {
            callStatement = DBConnection.getConn().prepareCall("{? = call PANDA." + function + "(?)}");
            callStatement.setObject(2, value);
            callStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callStatement.execute();
            return (ResultSet) callStatement.getObject(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object CallFunctionWithOutValues(String function, Object type) {
        CallableStatement callStatement = null;
        try {
            callStatement = DBConnection.getConn().prepareCall("{? = call PANDA." + function + "}");
            callStatement.registerOutParameter(1, (int) type);

            callStatement.execute();
            return (Object) callStatement.getObject(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object CallFunction(String function, Object[] values, Object type) {
        CallableStatement callStatement = null;
        try {
            callStatement = setCallable(function, values, true);
            callStatement.registerOutParameter(1, (int) type);

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
                if (value instanceof Boolean) {
                    callStatement.setBoolean(i + 1, ((Boolean) value));
                } else {
                    callStatement.setObject(i + 1, value);
                }
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
