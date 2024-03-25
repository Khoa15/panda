package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import model.DBConnection;
import model.Project;
import model.Task;

public class ProjectDAO {

    private static ArrayList<Project> projects = new ArrayList<Project>();

    public static void add(Project project) throws Exception {
        try {
            Object[] values = new Object[]{
                project.getName(),
                project.getDescription(),
                project.getPriority(),
                project.getStartedAt(),
                project.getEndedAt()
            };
            DBConnectionDAO.CallProcedureNoParameterOut("add_project", values);
        } catch (Exception e) {
            throw e;
        }
    }

    public static ArrayList<Project> loadFull() throws Exception {
        try {
            ProjectDAO.load();
            ArrayList<Task> tasks = TaskDAO.load();
            int j = 0;
            for (int i = 0; i < tasks.size(); i++) {
                int idProject = tasks.get(i).getProject().getId();
                while (idProject != projects.get(j).getId()) {
                    j++;
                }
                if (idProject == projects.get(j).getId()) {
                    projects.get(j).addTask(tasks.get(i));
                }
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ProjectDAO() {
    }

    public static ArrayList<Project> load() {
        try {
            projects.clear();
            ResultSet rs = DBConnectionDAO.CallFunction("select_projects");
            if (rs == null) {
                return projects;
            }
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

    public static void update(Project project, String oldProjectName) throws Exception {
        try {
            Object[] values = new Object[]{
                project.getId(),
                oldProjectName,
                project.getName(),
                project.getDescription(),
                project.getPriority(),
                project.getStartedAt(),
                project.getEndedAt()
            };
            DBConnectionDAO.CallProcedureNoParameterOut("Update_Project", values);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void delete(int id) throws Exception {
        try {
            Object[] values = new Object[]{
                id
            };
            DBConnectionDAO.CallProcedureNoParameterOut("Delete_Project", values);

        } catch (Exception e) {
            throw e;
        }
    }

    public static DefaultTableModel getDataTable() {
        try {
            ResultSet resultSet = DBConnectionDAO.CallFunction("select_projects");
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
                Object[] rowData = new Object[columnCount + 2];
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
        } catch (Exception e) {
            return null;
        }
    }

    private static Project setProject(ResultSet rs) {
        try {
            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setName(rs.getNString("name"));
            project.setDescription(rs.getNString("description"));
            project.setPriority(rs.getByte("priority"));
            project.setCreatedAt(rs.getTimestamp("created_at"));
            project.setUpdatedAt(rs.getTimestamp("updated_at"));
            project.setStartedAt(rs.getTimestamp("started_at"));
            project.setEndedAt(rs.getTimestamp("ended_at"));

            return project;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
