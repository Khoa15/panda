package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import model.DBConnection;
import model.Project;

public class ProjectDAO {

    private static ArrayList<Project> projects = new ArrayList<Project>();

    public ProjectDAO() {
    }

    public static ArrayList<Project> load() {
        try {
            projects.clear();
            ResultSet rs = DBConnectionDAO.Load("select_projects");
            while (rs.next()) {
                projects.add(setProject(rs));
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static ArrayList<Project> load(int id) {
        try {
            projects.clear();
            ResultSet rs = DBConnectionDAO.Load(null, id);
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                projects.add(setProject(rs));
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int update(Project project) {
        try {
            return DBConnectionDAO.Update("UpdateProject", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("DeleteProject", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static DefaultTableModel getDataTable(){
        try{
            ResultSet resultSet = DBConnectionDAO.Load("select_projects");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            columnNames.add("Sửa");
            columnNames.add("Xóa");
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                int entityId = resultSet.getInt(1);
                JButton editButton = new JButton("Sửa");
                JButton deleteButton = new JButton("Xóa");
                //editButton.addActionListener(e -> editEntity(entityId));
                //deleteButton.addActionListener(e -> deleteEntity(entityId));
                rowData[columnCount] = editButton;
                rowData[columnCount + 1] = deleteButton;
                
                tableModel.addRow(rowData);
            }

            return tableModel;
        }catch(Exception e){
            return null;
        }
    }
    
    private static Project setProject(ResultSet rs) {
        try {
            Project project = new Project();
            project.setName(rs.getNString("name"));
            return project;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
