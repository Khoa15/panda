package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import model.DBConnection;
import model.Project;

public class ProjectDAO {

    private static ArrayList<Project> projects = new ArrayList<Project>();

    public ProjectDAO() {
    }

    public static ArrayList<Project> load() {
        try {
            projects.clear();
            ResultSet rs = DBConnectionDAO.Load("SelectProjects");
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                projects.add(setProject(rs));
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
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
            DBConnection.closeConnection();
        }
    }

    public static int update(Project project) {
        try {
            return DBConnectionDAO.Update("UpdateProject", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("DeleteProject", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.closeConnection();
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
