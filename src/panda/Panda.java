/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package panda;

import com.sun.jdi.connect.spi.Connection;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import model.DBConnection;

/**
 *
 * @author nguye
 */
public class Panda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Login.main(args);
        SignIn.main(args);
//        DBConnection db = new DBConnection();
//        DBConnection.closeConnection();
    }

    public static void exit(WindowEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(e.getComponent(),
                "Bạn muốn tắt chương trình?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            DBConnection.closeConnection();
            System.exit(0);
        }
    }
}
