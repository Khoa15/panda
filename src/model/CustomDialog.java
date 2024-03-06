/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Khoa
 */
public class CustomDialog {
    JOptionPane option = new JOptionPane();
    public CustomDialog(){
    }
    public static void show(String message){
        JOptionPane.showConfirmDialog(null, message, "Thông báo", JOptionPane.DEFAULT_OPTION);
    }
}
