/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JFrame;
import model.Account;
import model.Profile;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Khoa
 */
public class SystemDAO {
    public static boolean deleteProfile(String profile){
        try{
            Object[] values =  new Object[] {profile};
            DBConnectionDAO.CallProcedureNoParameterOut("DROP_PROFILE", values);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean insertProfile(Profile profile) throws Exception{
        try{
            Object[] values = new Object[]{
                profile.profile,
                profile.resource_name,
                profile.limit
            };
            DBConnectionDAO.CallProcedureNoParameterOut("CREATE_PROFILE", values);
        }catch(Exception e){
            throw e;
        }
        return true;
    }
    public static boolean updateProfile(Profile profile){
        try{
            Object[] values = new Object[]{
                profile.profile,
                profile.resource_name,
                profile.limit
            };
            return DBConnectionDAO.CallProcedureNoParameterOut("update_profile", values);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static ListProfile getProfile(String profile){
        ListProfile listProfile = new ListProfile();
        try{
            ResultSet rs = (ResultSet)DBConnectionDAO.CallFunction("GET_PROFILE", profile);
            while(rs.next()){
                Profile p = new Profile(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                );
                listProfile.profiles.add(p);
            }
            listProfile.profile = profile;
        }catch(Exception e){
            e.printStackTrace();
        }
        return listProfile;
    }
    public static DefaultTableModel loadProfiles() {
        try {
            return setDefaultDataTableModel("get_all_profiles");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUser(String username, String password, String profile, boolean isLock) throws Exception{
        try{
            int lock = (isLock) ? 1 : 0;
            Object[] values = new Object[]{
                username,
                "",
                password,
                profile,
                lock
            };
            DBConnectionDAO.CallProcedureNoParameterOut("ADD_ACCOUNT_PROFILE", values);
        }catch(Exception e){
            throw e;
        }
        return true;
    }
    
    public static boolean saveUser(String username, String password, String profile, boolean isLock) throws Exception {
        try {
            int cpass = (password.isEmpty() || password == null) ? 0 : 1;
            int lock = (isLock) ? 1 : 0;
            Object[] values = new Object[]{
                username,
                password,
                cpass,
                profile,
                lock
            };
            DBConnectionDAO.CallProcedureNoParameterOut("modify_user_profile", values);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public static ArrayList<String> loadListProfiles() {
        ArrayList<String> profiles = new ArrayList<>();
        try {
            ResultSet rs = (ResultSet) DBConnectionDAO.CallFunction("get_dba_profiles");
            while (rs.next()) {
                profiles.add(rs.getString(1));// Profile
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profiles;
    }

    public static ArrayList<Object[]> loadListUsers() throws Exception {
        ArrayList<Object[]> accounts = new ArrayList<>();
        try {
            ResultSet rs = (ResultSet) DBConnectionDAO.CallFunction("get_list_users");
            while (rs.next()) {
                Object[] objs = new Object[7];
                objs[0] = rs.getNString(1);
                objs[1] = rs.getNString(2);
                objs[2] = rs.getNString(3);
                objs[3] = rs.getNString(4);
                objs[4] = rs.getNString(5);
                objs[5] = rs.getNString(6);
                objs[6] = rs.getNString(7);

                accounts.add(objs);
            }
        } catch (Exception e) {
            throw e;
        }
        return accounts;
    }

    public static boolean createTablespace(String tablespace, ArrayList<String> location, ArrayList<String> size) throws Exception {
        try {
            if (location.size() != size.size()) {
                return false;
            }
            StringBuilder datafiles = new StringBuilder();
            int i = 0;
            for (; i < location.size() - 1; i++) {
                datafiles.append("'").append(location.get(i)).append("'").append(" size ").append(size.get(i)).append("M,");
            }
            datafiles.append("'").append(location.get(i)).append("'").append(" size ").append(size.get(i)).append("M");
            Object[] values = {
                tablespace,
                datafiles.toString()
            };
            return DBConnectionDAO.CallProcedureNoParameter("AddTableSpacesAndManyDatafiles", values);
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean createDatafile(String tablespace, String size, String maxsize, String quota, String location) throws Exception {
        try {
            Object[] values = {
                tablespace,
                location,
                size
            };
            return DBConnectionDAO.CallProcedureNoParameter("add_datafile", values);
        } catch (Exception e) {
            throw e;
        }

    }

    public static DefaultTableModel LoadUsers() {
        try {
            return setDefaultDataTableModel("get_all_users");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadSGA() {
        try {
            return setDefaultDataTableModel("GetSgaInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadPGA() {
        try {
            return setDefaultDataTableModel("GetPgaStat");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static DefaultTableModel LoadControlFile() {
        try {
            return setDefaultDataTableModel("GetControlfileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadDatabaseInfo() {
        try {
            return setDefaultDataTableModel("GetDatabaseInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadDataFileInfo() {
        try {
            return setDefaultDataTableModel("GetDatafileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadInstanceInfo() {
        try {
            return setDefaultDataTableModel("GetInstanceInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadProcessInfo() {
        try {
            return setDefaultDataTableModel("GetProcessInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadSpFileInfo() {
        try {
            return setDefaultDataTableModel("GetSpfileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadSession() {
        try {
            return setDefaultDataTableModel("SelectSession");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadTableSpace() {
        try {

            return setDefaultDataTableModel("GetTableSpaces");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> LoadTableSpaceName() {
        ArrayList<String> result = new ArrayList<>();
        try {
            ResultSet rs = DBConnectionDAO.CallFunction("get_tablespaces");
            while (rs.next()) {
                result.add(rs.getString("tablespace_name"));
            }
        } catch (Exception e) {
            //throw e;
        }
        return result;
    }

    public static DefaultTableModel LoadDataFiles() {
        try {

            ResultSet rs = DBConnectionDAO.ExecuteSelectQuery("SELECT file_id, file_name, tablespace_name from dba_data_files");
            return setDefaultDataTableModel(rs);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadPolicy() {
        try {
            DefaultTableModel tmp = setDefaultDataTableModel("get_policy_in_user");

            return tmp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadAudit() {
        try {
            ResultSet rs = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM DBA_AUDIT_TRAIL");
            return setDefaultDataTableModel(rs);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel loadInfoSystem(String type) {
        try {
            String table;
            switch (type) {
                case "PGA":
                    table = "v$pgastat";
                    break;
                case "PROCESS":
                    table = "v$process";
                    break;
                case "INSTANCE":
                    table = "v$instance";
                    break;
                case "DATABASE":
                    table = "v$database";
                    break;
                case "DATAFILE":
                    table = "v$datafile";
                    break;
                case "CONTROL FILES":
                    table = "v$controlfile";
                    break;
                case "SPFILE":
                    table = "v$spparameter";
                    break;
                case "SESSION":
                    table = "v$session where type!='BACKGROUND'";
                    break;
                case "TABLESPACE":
                    table = "dba_tablespaces";
                    break;
                default:
                    table = "v$sgainfo";
                    break;
            }
            ResultSet resultSet = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM " + table);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
//            ResultSet resultSet = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM "+table);
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            DefaultTableModel tableModel = new DefaultTableModel(new Object[columnCount], 0);
//
//            while (resultSet.next()) {
//                Object[] rowData = new Object[columnCount];
//                for (int i = 1; i <= columnCount; i++) {
//                    rowData[i - 1] = resultSet.getObject(i);
//                }
//                tableModel.addRow(rowData);
//            }
//
//            return tableModel;
            //jtable.setModel(tableModel);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean killSession(Object sid, Object serial) {
        try {
            Connection connection = DBConnection.getConn();// Establish database connection

            String sql = "ALTER SYSTEM KILL SESSION '" + sid + ", " + serial + "' IMMEDIATE";

            return DBConnectionDAO.ExecuteScalarQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DefaultTableModel LoadProcessWithSession(String sid) {
        try {
            Object[] values = {sid};
            ResultSet resultSet = (ResultSet) DBConnectionDAO.CallFunction("get_process_with_session", values, OracleTypes.CURSOR);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(new Object[columnCount], 0);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static DefaultTableModel setDefaultDataTableModel(String name) {
        try {
            ResultSet resultSet = DBConnectionDAO.CallFunction(name);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                int i = 1;
                for (; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
        } catch (Exception e) {
            return null;
        }
    }

    private static DefaultTableModel setDefaultDataTableModel(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
        } catch (Exception e) {
            return null;
        }
    }

    public static DefaultTableModel LoadDbaSysPrivs() {
        try {
            return setDefaultDataTableModel("get_dba_sys_privs");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadDbaTabPrivs() {
        try {
            return setDefaultDataTableModel("get_dba_tab_privs");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadRole() {
        try {
            return setDefaultDataTableModel("get_dba_role_privs");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SystemDAO() {
    }

}
