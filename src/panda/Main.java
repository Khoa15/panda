/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package panda;

import java.awt.Component;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import panda.user.CpnMain;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.DBConnection;
import panda.user.CpnCollection;
import panda.user.CpnDictionary;
import panda.user.CpnInbox;
import panda.user.CpnProfile;
import panda.user.CpnProject;
import panda.user.TrackCpnProfileSystem;

/**
 *
 * @author nguye
 */
public class Main extends javax.swing.JFrame  {
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        DBConnection dbconnection = new DBConnection();
        dbconnection.start();
        
        pnMain.add(new CpnMain());
        try{
            LocalDateTime lastLoginTimestamp = DBConnection.getLastLogin();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedLastLogin = lastLoginTimestamp.format(formatter);

            setTitle("Hello: " + DBConnection.getUsername() + ", Last login: " + formattedLastLogin);
        }catch(Exception e){}
    } 


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        pnMain = new javax.swing.JPanel();
        pnNavs = new javax.swing.JPanel();
        btnNavProfile = new javax.swing.JButton();
        btnNavProject = new javax.swing.JButton();
        btnNavCollection = new javax.swing.JButton();
        btnNavDictionary = new javax.swing.JButton();
        btnNavInbox = new javax.swing.JButton();
        btnNavHome = new javax.swing.JButton();

        jFormattedTextField1.setText("jFormattedTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("123");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnMain.setLayout(new javax.swing.BoxLayout(pnMain, javax.swing.BoxLayout.LINE_AXIS));

        btnNavProfile.setText("Profile");
        btnNavProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavProfileMouseClicked(evt);
            }
        });

        btnNavProject.setText("Project");
        btnNavProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavProjectMouseClicked(evt);
            }
        });

        btnNavCollection.setText("Collection");
        btnNavCollection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavCollectionMouseClicked(evt);
            }
        });

        btnNavDictionary.setText("Dictionary");
        btnNavDictionary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavDictionaryMouseClicked(evt);
            }
        });

        btnNavInbox.setText("Inbox");
        btnNavInbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavInboxMouseClicked(evt);
            }
        });

        btnNavHome.setText("Home");
        btnNavHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNavHomeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnNavsLayout = new javax.swing.GroupLayout(pnNavs);
        pnNavs.setLayout(pnNavsLayout);
        pnNavsLayout.setHorizontalGroup(
            pnNavsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
            .addGroup(pnNavsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnNavsLayout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(btnNavHome, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(btnNavCollection, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(btnNavDictionary, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(btnNavInbox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(btnNavProject, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(btnNavProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(27, 27, 27)))
        );
        pnNavsLayout.setVerticalGroup(
            pnNavsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
            .addGroup(pnNavsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnNavsLayout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(pnNavsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNavProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNavProject, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNavCollection, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNavDictionary, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNavInbox, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNavHome, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(12, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnNavs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnNavs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void navigateToPanel(Component comp){
        pnMain.removeAll();
        pnMain.add(comp);
        pnMain.revalidate();
        pnMain.repaint();
    }
    private void btnNavProfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavProfileMouseClicked
        // TODO add your handling code here:
        //navigateToPanel(new CpnProfile());
        TrackCpnProfileSystem track = new TrackCpnProfileSystem();
        new Thread(track).start();
        navigateToPanel(track.cpnProfile);
    }//GEN-LAST:event_btnNavProfileMouseClicked

    private void btnNavProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavProjectMouseClicked
        // TODO add your handling code here:
        navigateToPanel(new CpnProject());
    }//GEN-LAST:event_btnNavProjectMouseClicked

    private void btnNavCollectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavCollectionMouseClicked
        // TODO add your handling code here:
        navigateToPanel(new CpnCollection());
    }//GEN-LAST:event_btnNavCollectionMouseClicked

    private void btnNavDictionaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavDictionaryMouseClicked
        // TODO add your handling code here:
        navigateToPanel(new CpnDictionary());
    }//GEN-LAST:event_btnNavDictionaryMouseClicked

    private void btnNavInboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavInboxMouseClicked
        // TODO add your handling code here:
        navigateToPanel(new CpnInbox());
    }//GEN-LAST:event_btnNavInboxMouseClicked

    private void btnNavHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNavHomeMouseClicked
        // TODO add your handling code here:
        navigateToPanel(new CpnMain());
    }//GEN-LAST:event_btnNavHomeMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            // TODO add your handling code here:
            Panda.exit(evt);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main m = new Main();
                m.setVisible(true);
                m.setLocationRelativeTo(null);
            }
        });
    }
    @Override
    public void setVisible(boolean b){
        super.setVisible(b);
        this.setLocationRelativeTo(null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNavCollection;
    private javax.swing.JButton btnNavDictionary;
    private javax.swing.JButton btnNavHome;
    private javax.swing.JButton btnNavInbox;
    private javax.swing.JButton btnNavProfile;
    private javax.swing.JButton btnNavProject;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnNavs;
    // End of variables declaration//GEN-END:variables
}
