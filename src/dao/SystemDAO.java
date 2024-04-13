/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Timestamp;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Clob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.table.TableModel;
import model.Account;
import model.Profile;
import oracle.jdbc.OracleTypes;
import static oracle.jdbc.OracleTypes.TIMESTAMPTZ;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;

/**
 *
 * @author Khoa
 */
public class SystemDAO {
    
    public static ArrayList<String> getListPrivsOf(String procedure, String object){
        ArrayList<String> privs = new ArrayList<>();
        try{
            Object[] values = new Object[]{
                procedure,
                object
            };
            ResultSet rs = DBConnectionDAO.CallFunction("get_role_tab_privs_privs", values);
            while(rs.next()){
                privs.add(rs.getString(1));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return privs;
    }
    public static ArrayList<String> getListProcedures(){
        ArrayList<String> procedures = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_panda_procedures");
            while(rs.next()){
                procedures.add(rs.getString(1));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return procedures;
    }
    public static ArrayList<String> getListTables() {
        ArrayList<String> tables = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_panda_tables");
            while(rs.next()){
                tables.add(rs.getString(1));
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return tables;
    }
    public static Account getUser(String username) throws Exception {
        Account user = new Account();
        try {
            ResultSet rs = (ResultSet) DBConnectionDAO.CallFunction("get_user", username);
            while (rs.next()) {
                user.profile = rs.getString(1);
                user.tablespace = rs.getString(2);
                user.setAvatar(rs.getBytes(3));
                user.setUsername(rs.getString(5));
                user.setFullname(rs.getString(6));
                user.setLock(rs.getString(7));
            }
            rs.close();
        } catch (Exception e) {
            throw e;
        }
        return user;
    }

    public static boolean deleteProfile(String profile) {
        try {
            Object[] values = new Object[]{profile};
            DBConnectionDAO.CallProcedureNoParameterOut("DROP_PROFILE", values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertProfile(Profile profile) throws Exception {
        try {
            Object[] values = new Object[]{
                profile.profile,
                profile.resource_name,
                profile.limit
            };
            DBConnectionDAO.CallProcedureNoParameterOut("CREATE_PROFILE", values);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public static boolean updateProfile(Profile profile) {
        try {
            Object[] values = new Object[]{
                profile.profile,
                profile.resource_name,
                profile.limit
            };
            return DBConnectionDAO.CallProcedureNoParameterOut("update_profile", values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ListProfile getProfile(String profile) {
        ListProfile listProfile = new ListProfile();
        try {
            ResultSet rs = (ResultSet) DBConnectionDAO.CallFunction("GET_PROFILE", profile);
            while (rs.next()) {
                Profile p = new Profile(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                );
                listProfile.profiles.add(p);
            }
            rs.close();
            listProfile.profile = profile;
        } catch (Exception e) {
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

    public static boolean insertUser(String username, String password, String profile, String tablespace, String quota, boolean isLock) throws Exception {
        try {
            int lock = (isLock) ? 1 : 0;
            Object[] values = new Object[]{
                username,
                "",
                password,
                profile,
                tablespace,
                quota,
                lock
            };
            DBConnectionDAO.CallProcedureNoParameterOut("ADD_ACCOUNT_PROFILE", values);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public static boolean saveUser(String username, String password, String profile, String tablespace, String quota, boolean isLock) throws Exception {
        try {
            int cpass = (password.isEmpty() || password == null) ? 0 : 1;
            int lock = (isLock) ? 1 : 0;
            Object[] values = new Object[]{
                username,
                password,
                profile,
                tablespace,
                quota,
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
            rs.close();
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
            rs.close();
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

    public static boolean createDatafile(String tablespace, String size, String location) throws Exception {
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

    public static DefaultTableModel LoadUsernames() {
        DefaultTableModel model = new DefaultTableModel();
        try {
            model = setDefaultDataTableModel("get_users_privs");
        } catch (Exception e) {

        }
        return model;
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
            rs.close();
        } catch (Exception e) {
            //throw e;
        }
        return result;
    }

    public static DefaultTableModel LoadPolicy() {
        try {
            DefaultTableModel tmp = setDefaultDataTableModel("get_audit_policies");//get_policy_in_user");

            return tmp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadAudit() {
        try {
            return setDefaultDataTableModel("get_audit_trail");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    

    public static boolean killSession(Object sid, Object serial) {
        try {
            Object[] values = new Object[]{
                sid,
                serial
            };
            DBConnectionDAO.CallProcedureNoParameterOut("kill_session", values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DefaultTableModel LoadProcessWithSession(String sid) {
        try {
            Object[] values = {sid};
            ResultSet resultSet = (ResultSet) DBConnectionDAO.CallFunction("get_process_with_session", values, OracleTypes.CURSOR);
            return setDefaultDataTableModel(resultSet);
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
                    Object value = resultSet.getObject(i);
                    if(value != null){
                        if(value instanceof Clob){
                            Clob clob = (Clob) value;
                            value = clob.getSubString(1, (int) clob.length());
                        } else if(value instanceof TIMESTAMPTZ){
                            try{
                                TIMESTAMPTZ t = (TIMESTAMPTZ) value;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
                                String localDateTime = t.timestampValue(DBConnection.getConn()).toLocalDateTime().format(formatter);
                                value = localDateTime;
                            }catch(Exception r){
                                r.printStackTrace();
                            }
                        }
                    }
                    rowData[i - 1] = value;
                }
                tableModel.addRow(rowData);
            }
            resultSet.close();
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
            resultSet.close();
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

    public static DefaultTableModel LoadUserPrivs(String username) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_user_tab_privs", username));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    
    public static DefaultTableModel LoadUsersName() {
        DefaultTableModel model = new DefaultTableModel();
        try {
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_users_privs"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;

    }    

    public static DefaultTableModel LoadRoleName() {
        DefaultTableModel model = new DefaultTableModel();
        try {
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_roles"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;

    }

    /**
     *
     * @return
     */
    public static String[] LoadStrRoleName() {
         ArrayList<String> result = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_roles");
            if(rs == null){
                throw new IllegalStateException("Data not found");
            }
            while(rs.next()){
                result.add(rs.getString(1));
            }
        }catch(Exception e){
            
        }
        return result.toArray(new String[result.size()]);
    }

    public static TableModel LoadRolePrivs(String role) {
        DefaultTableModel model = new DefaultTableModel();
        if(role == null || role.isEmpty()) return model;
        try {
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_role_tab_privs", role));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    
    public static HashMap<String, String> getListObjects(){
        HashMap<String, String> objects = new HashMap<>();
        try{
            Object[] values = new Object[]{
                ""
            };
            ResultSet rs = DBConnectionDAO.CallFunction("get_procedures_functions_tables", values);
            while(rs.next()){
                objects.put(rs.getString(1), rs.getString(2));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return objects;
    }

    public static TableModel getProceduresAndTables(String role) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            //get_procedures_functions_tables
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_role_others", role));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void UpdateRolePrivs(
            String role, 
            String object, 
            String typeObject, 
            boolean execute, 
            boolean grant_execute, 
            boolean select, 
            boolean grant_select, 
            boolean update, 
            boolean grant_update, 
            boolean delete, 
            boolean grant_delete, 
            boolean insert,
            boolean grant_insert, 
            boolean sys_priv, 
            boolean option
    ) throws Exception {
        try{
            
            Object[] values = new Object[]{
                role,
                object,
                (execute) ? "Y": "N",
                (grant_execute) ? "Y" : "N",
                (select) ? "Y": "N",
                (grant_select) ? "Y" : "N",
                (update) ? "Y": "N",
                (grant_update) ? "Y" : "N",
                (delete) ? "Y": "N",
                (grant_delete) ? "Y" : "N",
                (insert) ? "Y": "N",
                (grant_insert) ? "Y" : "N",
                (sys_priv) ? "Y": "N",
                (option) ? "Y" : "N"
            };
            DBConnectionDAO.CallProcedureNoParameterOut("update_role_privs", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static void InsertRolePrivs(
            String role, 
            String object, 
            String typeObject, 
            boolean execute, 
            boolean grant_execute, 
            boolean select, 
            boolean grant_select, 
            boolean update, 
            boolean grant_update, 
            boolean delete, 
            boolean grant_delete, 
            boolean insert,
            boolean grant_insert, 
            boolean sys_priv, 
            boolean option
    ) throws Exception {
        try{           
            Object[] values = new Object[]{
                role,
                object,
                (execute) ? "Y": "N",
                (grant_execute) ? "Y" : "N",
                (select) ? "Y": "N",
                (grant_select) ? "Y" : "N",
                (update) ? "Y": "N",
                (grant_update) ? "Y" : "N",
                (delete) ? "Y": "N",
                (grant_delete) ? "Y" : "N",
                (insert) ? "Y": "N",
                (grant_insert) ? "Y" : "N",
                (sys_priv) ? "Y": "N",
                (option) ? "Y" : "N"
            };
            DBConnectionDAO.CallProcedureNoParameterOut("insert_role_privs", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static void deleteRole(String currentRole) throws Exception {
        try{           
            Object[] values = new Object[]{
                currentRole
            };
            DBConnectionDAO.CallProcedureNoParameterOut("delete_role_privs", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static TableModel LoadRoleSysPrivs(String role, boolean insert) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            String procedure = "get_sys_privs";
            if(insert){
                procedure = "get_insert_sys_privs";
            }
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction(procedure, role));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void revokeRole(String grantee,String object_name, String role) throws Exception {
        try{
            Object[] values = new Object[]{
                grantee,
                object_name,
                role
            };
            DBConnectionDAO.CallProcedureNoParameterOut("revoke_role", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static TableModel LoadGrantedRoles(String currentRole, boolean insert) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            String procedure = "get_granted_roles";
            if(insert){
                procedure = "get_insert_granted_roles";
            }
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction(procedure, currentRole));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static TableModel LoadOthersPrivs(String currentRole, boolean insert) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            Object[] values = new Object[]{
                currentRole,
                (insert) ? "Y":"N"
            };
            model = setDefaultDataTableModel(DBConnectionDAO.CallFunction("get_role_others", values));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void removeUser(String username) throws Exception {
        try{
            Object[] values = new Object[]{
                username
            };
            DBConnectionDAO.CallProcedureNoParameter("remove_user", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static String getQuotaOfUser(String username) {
        String quota = null;
        try{
            Object[] values = new Object[]{
                username
            };
            ResultSet rs = (ResultSet)DBConnectionDAO.CallFunction("get_quota_user", values);
            if(rs == null) return quota;
            while(rs.next()){
                int iquota = rs.getInt(8) / (1024*1024);
                quota = String.valueOf(iquota);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return quota;
    }

    public static void deleteTablespace(String tablespace) throws Exception {
        try{
            Object[] values = new Object[]{
                tablespace
            };
            DBConnectionDAO.CallProcedureNoParameterOut("delete_tablespace", values);
            
        }catch(Exception e){
            throw e;
        }
    }

    public static void deleteDatafile(String pathDatafile) throws Exception {
        try{
            Object[] values = new Object[]{
                pathDatafile
            };
            DBConnectionDAO.CallProcedureNoParameterOut("delete_datafile", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static void AddPolicy(String Policy,
            String ObjectName,
            String AuditCondition,
            boolean enable,
            boolean delete,
            boolean select,
            boolean update,
            boolean insert
    ) throws Exception {
        try{
            String statement = getStatementType(delete, select, update, insert);
            
            
            Object[] values = new Object[]{
                (ObjectName.isEmpty()) ? null : ObjectName,
                (Policy.isEmpty()) ? null : Policy,
                (statement.isEmpty()) ? null : statement.toString(),
                (AuditCondition.isEmpty()) ? null : AuditCondition
            };
            DBConnectionDAO.CallProcedureNoParameterOut("ADD_FGA_POLICY", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static void deleteAudit(String policyName, String objectName) throws Exception {
        Object[] values = new Object[]{
            objectName,
            policyName
        };
        DBConnectionDAO.CallProcedureNoParameterOut("DROP_FGA_POLICY", values);//drop_audit_policy", values);
    }

    public static void ModifyPolicy(
            String Policy, 
            String ObjectName, 
            String AuditCondition, 
            boolean enable, 
            boolean delete, 
            boolean select, 
            boolean update, 
            boolean insert
    ) throws Exception
    {
        String statement = getStatementType(delete, select, update, insert);
        Object[] values = new Object[]{
                (ObjectName.isEmpty()) ? null : ObjectName,
                (Policy.isEmpty()) ? null : Policy,
                (AuditCondition.isEmpty()) ? null : AuditCondition,
                (enable) ? "1" : "0",
                (statement.isEmpty()) ? null : statement.toString()
            };
        DBConnectionDAO.CallProcedureNoParameterOut("MODIFY_FGA_POLICY", values);
    }
    
    private static String getStatementType(
            boolean delete, 
            boolean select, 
            boolean update, 
            boolean insert
    )
    {
        StringBuilder statement = new StringBuilder();
            if (delete) {
                statement.append("DELETE");
            }

            if (select) {
                if (statement.length() > 0) {
                    statement.append(", ");
                }
                statement.append("SELECT");
            }

            if (update) {
                if (statement.length() > 0) {
                    statement.append(", ");
                }
                statement.append("UPDATE");
            }
            
            if (insert) {
                if (statement.length() > 0) {
                    statement.append(", ");
                }
                statement.append("INSERT");
            }
        return statement.toString();
    }

    public static String[] LoadAuditPolicies() {
        ArrayList<String> result = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_policies_audit");
            if(rs == null){
                throw new IllegalStateException("Data not found");
            }
            while(rs.next()){
                result.add(rs.getString(1));
            }
        }catch(Exception e){
            
        }
        return result.toArray(new String[result.size()]);
    }

    public static Object[] LoadInfoAuditPolicy(String auditPolicy) {
        Object[] objects = null;
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_info_policies_audit", auditPolicy);
            if(rs==null){
                throw new IllegalStateException("Data not found");
            }
            objects = new Object[8];
            HashMap<String, String> result = new HashMap<>();
            while(rs.next()){
                result.put(rs.getString("AUDIT_OPTION_TYPE"), rs.getString("AUDIT_OPTION"));
            }
            objects[0] = result;
        }catch(Exception e){
            
        }
        return objects;
    }

    public static String[] LoadSystemPrivileges() {
        ArrayList<String> result = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_audit_option_syspriv");
            if(rs == null){
                throw new IllegalStateException("Data not found");
            }
            while(rs.next()){
                result.add(rs.getString(1));
            }
        }catch(Exception e){
            
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] LoadAuditAction() {
        ArrayList<String> result = new ArrayList<>();
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_audit_option_action");
            if(rs == null){
                throw new IllegalStateException("Data not found");
            }
            while(rs.next()){
                result.add(rs.getString(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toArray(new String[result.size()]);
    }

    public static void insertAudit(
            String name, 
            List<String> privs, 
            List<String> actions, 
            String on,
            List<String> roles, 
            boolean allUsers, 
            String user, 
            String evaluate, 
            boolean onlyTopLevel) throws Exception {
        
        try{
            String sql_privs = (privs.size()>0) ? " "+ String.join(", ", privs) + " " : "";
            String sql_roles = (roles.size()>0) ? " "+ String.join(", ", roles) + " " : "";
            String sql_actions = "";
            if(actions.size()>0){
                if(on.toUpperCase().equals("ALL")){
                    sql_actions = " ALL ";
                }else{
                    sql_actions += " ";
                    for(int i = 0; i < actions.size() - 1; i++){
                        sql_actions += actions.get(i) + " ON " + DBConnection.getSchema() + "." + on +", ";
                    }
                    sql_actions += actions.get(actions.size() - 1) + " ON " + DBConnection.getSchema() + "." + on + " ";
                }
            }
            Object[] values = new Object[]{
                name,
                sql_privs,
                sql_actions,
                sql_roles,
                (allUsers) ? null : user,
                evaluate.toUpperCase(),
                (onlyTopLevel) ? "Y": "N"
            };
            DBConnectionDAO.CallProcedureNoParameterOut("CREATE_AUDIT", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static ArrayList<String> LoadTablesName() {
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("get_tables");
            ArrayList<String> result = new ArrayList<>();
            while(rs.next()){
                result.add(rs.getNString(2));
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }

    public static void deleteAuditPolicy(String auditName) throws Exception {
        Object[] values = new Object[]{
            auditName
        };
        DBConnectionDAO.CallProcedureNoParameterOut("delete_audit_policy", values);
    }

    public static DefaultTableModel LoadActions() {
        try {

            return setDefaultDataTableModel("get_actions");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String GenerateUsername(String fullname) {
        return (String) DBConnectionDAO.CallFunction("generate_username", new Object[]{fullname}, OracleTypes.NVARCHAR);
    }
    
    

    public SystemDAO() {
    }

}
