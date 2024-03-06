/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package panda;

import com.sun.jdi.connect.spi.Connection;
import dao.DBConnectionDAO;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.DBConnection;
import model.SystemListener;

/**
 *
 * @author nguye
 */
public class Panda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, InterruptedException {
        SignIn.main(args);
    }

    public static void exit(WindowEvent e) throws Exception {
        int confirmed = JOptionPane.showConfirmDialog(e.getComponent(),
                "Bạn muốn tắt chương trình?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            if(DBConnection.getConn() != null){
                Object[] values = {
                    DBConnection.getUsername().toUpperCase()
                };
                DBConnectionDAO.CallProcedureNoParameter("signout", values);
                DBConnection.closeConnection();
            }
            System.exit(0);
        }
    }
}
