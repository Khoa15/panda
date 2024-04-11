/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package panda.user.system;

import dao.SystemDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Khoa
 */
public class mgnRole extends javax.swing.JFrame {

    /**
     * Creates new form mgnRole
     */
    ArrayList<String> listProcedures;
    ArrayList<String> listTables;
    ArrayList<String> listPrivs;
    HashMap<String, String> listObjects;
    String currentRole;
    String currentObject;

    public mgnRole() {
        initComponents();
        listTables = SystemDAO.getListTables();
        listProcedures = SystemDAO.getListProcedures();
        listObjects = SystemDAO.getListObjects();
        setRoleModel();
        rdBtnOthers.setSelected(true);
    }

    private void setRoleModel() {
        if (cbUser.isSelected()) {
            jLabel1.setText("Username");
            tbListRoles.setModel(SystemDAO.LoadUsersName());
        } else {
            jLabel1.setText("Role");
            tbListRoles.setModel(SystemDAO.LoadRoleName());
        }
    }

    private void setPrivsModel(String role) {
        tbListPrivs.setModel(SystemDAO.LoadRolePrivs(role));
    }

    private void setObjectModel() {
        tbListPrivs.setModel(SystemDAO.getProceduresAndTables(currentRole));
    }

    private void setSysPrivsModel(String role) {
        tbListPrivs.setModel(SystemDAO.LoadRoleSysPrivs(role, cbBoxInsert.isSelected()));
    }

    private void setGrantedRolesModel() {
        tbListPrivs.setModel(SystemDAO.LoadGrantedRoles(currentRole, cbBoxInsert.isSelected()));
    }

    private void setOthersModel() {
        tbListPrivs.setModel(SystemDAO.LoadOthersPrivs(currentRole, cbBoxInsert.isSelected()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbListRoles = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbListPrivs = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFieldRole = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtFieldObject = new javax.swing.JTextField();
        cbBoxExecute = new javax.swing.JCheckBox();
        cbBoxSelect = new javax.swing.JCheckBox();
        cbBoxUpdate = new javax.swing.JCheckBox();
        cbBoxDelete = new javax.swing.JCheckBox();
        cbBoxInsert = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        rdBtnGrantedRoles = new javax.swing.JRadioButton();
        rdBtnSysPrivs = new javax.swing.JRadioButton();
        rdBtnOthers = new javax.swing.JRadioButton();
        cbBoxGrantInsert = new javax.swing.JCheckBox();
        cbBoxInsertPriv = new javax.swing.JCheckBox();
        cbBoxGrantDelete = new javax.swing.JCheckBox();
        cbBoxGrantUpdate = new javax.swing.JCheckBox();
        cbBoxGrantSelect = new javax.swing.JCheckBox();
        cbBoxGrantExecute = new javax.swing.JCheckBox();
        cbBoxGrantAdmin = new javax.swing.JCheckBox();
        txtFieldUsername = new javax.swing.JTextField();
        cbUser = new javax.swing.JCheckBox();
        btnSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tbListRoles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListRolesMouseClicked(evt);
            }
        });
        tbListRoles.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbListRolesPropertyChange(evt);
            }
        });
        jScrollPane5.setViewportView(tbListRoles);

        tbListPrivs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListPrivsMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbListPrivs);

        jLabel1.setText("Role");

        txtFieldRole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFieldRoleKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFieldRoleKeyReleased(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });

        jLabel2.setText("Object name:");

        cbBoxExecute.setText("Execute");
        cbBoxExecute.setEnabled(false);

        cbBoxSelect.setText("Select");

        cbBoxUpdate.setText("Update");

        cbBoxDelete.setText("Delete");

        cbBoxInsert.setText("Insert Mode");
        cbBoxInsert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbBoxInsertMouseClicked(evt);
            }
        });

        btnGroup.add(rdBtnGrantedRoles);
        rdBtnGrantedRoles.setText("Granted Roles");
        rdBtnGrantedRoles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdBtnGrantedRolesMouseClicked(evt);
            }
        });

        btnGroup.add(rdBtnSysPrivs);
        rdBtnSysPrivs.setText("System Privileges");
        rdBtnSysPrivs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdBtnSysPrivsMouseClicked(evt);
            }
        });

        btnGroup.add(rdBtnOthers);
        rdBtnOthers.setText("Others");
        rdBtnOthers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdBtnOthersMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdBtnGrantedRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdBtnSysPrivs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdBtnOthers))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdBtnGrantedRoles)
                    .addComponent(rdBtnSysPrivs)
                    .addComponent(rdBtnOthers))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbBoxGrantInsert.setText("GRANT");

        cbBoxInsertPriv.setText("Insert");

        cbBoxGrantDelete.setText("GRANT");

        cbBoxGrantUpdate.setText("GRANT");

        cbBoxGrantSelect.setText("GRANT");

        cbBoxGrantExecute.setText("GRANT");

        cbBoxGrantAdmin.setText("GRANT ADMIN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFieldRole)
                    .addComponent(txtFieldObject, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbBoxInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbBoxExecute)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxGrantExecute)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(cbBoxSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxGrantSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxGrantUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxGrantDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxInsertPriv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxGrantInsert))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbBoxGrantAdmin)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbBoxGrantAdmin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtFieldRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFieldObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbBoxExecute)
                    .addComponent(cbBoxSelect)
                    .addComponent(cbBoxUpdate)
                    .addComponent(cbBoxDelete)
                    .addComponent(cbBoxGrantInsert)
                    .addComponent(cbBoxInsertPriv)
                    .addComponent(cbBoxGrantDelete)
                    .addComponent(cbBoxGrantUpdate)
                    .addComponent(cbBoxGrantSelect)
                    .addComponent(cbBoxGrantExecute))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancel)
                        .addComponent(btnUpdate)
                        .addComponent(btnDelete)
                        .addComponent(cbBoxInsert))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cbUser.setText("User");
        cbUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbUserMouseClicked(evt);
            }
        });

        btnSearch.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFieldUsername)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbUser)
                            .addComponent(btnSearch))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void tbListRolesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListRolesMouseClicked
        // TODO add your handling code here:
        currentRole = tbListRoles.getValueAt(tbListRoles.getSelectedRow(), 0).toString();
        loadTypeRole();
        txtFieldRole.setText(currentRole);
        txtFieldObject.setText("");
        resetCheckBox();
    }//GEN-LAST:event_tbListRolesMouseClicked

    private void tbListPrivsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListPrivsMouseClicked
        // TODO add your handling code here:
        currentObject = tbListPrivs.getValueAt(tbListPrivs.getSelectedRow(), 0).toString();
        txtFieldObject.setText(currentObject);
        //listPrivs = SystemDAO.getListPrivsOf(currentRole, currentObject);
        resetCheckBox();
        if (rdBtnOthers.isSelected()) {
            String type = tbListPrivs.getValueAt(tbListPrivs.getSelectedRow(), 1).toString();
            switch (type) {
                case "FUNCTION":
                case "PROCEDURE":
                    cbBoxExecute.setSelected(true);
                    cbBoxSelect.setEnabled(false);
                    cbBoxUpdate.setEnabled(false);
                    cbBoxDelete.setEnabled(false);
                    break;
                case "TABLE":
                    cbBoxSelect.setEnabled(true);
                    cbBoxUpdate.setEnabled(true);
                    cbBoxDelete.setEnabled(true);
                    break;
            }
        }
    }//GEN-LAST:event_tbListPrivsMouseClicked

    private void cbBoxInsertMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbBoxInsertMouseClicked
        // TODO add your handling code here:
        loadTypeRole();
    }//GEN-LAST:event_cbBoxInsertMouseClicked
    private int getIndexRole(String role) {
        for (int row = 0; row < tbListRoles.getRowCount(); row++) {
            if (txtFieldRole.getText().equals(tbListRoles.getValueAt(row, 0).toString())) {
                return row;
            }
        }
        return -1;
    }
    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // TODO add your handling code here:
        String role = txtFieldRole.getText();
        String object = txtFieldObject.getText();
        boolean execute = cbBoxExecute.isSelected();
        boolean select = cbBoxSelect.isSelected();
        boolean update = cbBoxUpdate.isSelected();
        boolean delete = cbBoxDelete.isSelected();
        boolean insert = cbBoxInsertPriv.isSelected();
        boolean grant_execute = cbBoxGrantExecute.isSelected();
        boolean grant_select = cbBoxGrantSelect.isSelected();
        boolean grant_update = cbBoxGrantUpdate.isSelected();
        boolean grant_delete = cbBoxGrantDelete.isSelected();
        boolean grant_insert = cbBoxGrantInsert.isSelected();
        boolean option = cbBoxGrantAdmin.isSelected();
        boolean sys_priv = (rdBtnGrantedRoles.isSelected() == true || rdBtnSysPrivs.isSelected() == true) && cbBoxInsert.isSelected() == true;
        String typeObject = getTypeObject(object);
        try {
            if (getIndexRole(role) != -1) {
                SystemDAO.UpdateRolePrivs(
                        role, 
                        object, 
                        typeObject, 
                        execute, 
                        grant_execute, 
                        select, 
                        grant_select, 
                        update, 
                        grant_update, 
                        delete, 
                        grant_delete, 
                        insert, 
                        grant_insert, 
                        sys_priv, 
                        option
                );
            } else {
                SystemDAO.InsertRolePrivs(
                        role, 
                        object, 
                        typeObject, 
                        execute, 
                        grant_execute, 
                        select, 
                        grant_select, 
                        update, 
                        grant_update, 
                        delete, 
                        grant_delete, 
                        insert, 
                        grant_insert, 
                        sys_priv, 
                        option
                );
                setRoleModel();
            }

            JOptionPane.showConfirmDialog(this, "Successfully!", "Thông báo!", JOptionPane.DEFAULT_OPTION);
            loadTypeRole();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Thông báo!", JOptionPane.DEFAULT_OPTION);
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void txtFieldRoleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFieldRoleKeyReleased
        // TODO add your handling code here:
        String role = txtFieldRole.getText().toUpperCase();
        txtFieldRole.setText(role);
        if (!role.isEmpty()) {
            int row = getIndexRole(role);
            if (row != -1) {
                tbListRoles.getSelectionModel().setSelectionInterval(row, row);
                cbBoxInsert.setSelected(false);
                currentRole = role;
                //setPrivsModel(currentRole);
                return;
            }
        }
        currentRole = null;
        cbBoxInsert.setSelected(true);
        tbListRoles.clearSelection();
        tbListPrivs.setModel(new DefaultTableModel());
    }//GEN-LAST:event_txtFieldRoleKeyReleased

    private void txtFieldRoleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFieldRoleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldRoleKeyPressed

    private void tbListRolesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbListRolesPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tbListRolesPropertyChange
    private int optionDelete() throws Exception {
        int optionButton = JOptionPane.OK_CANCEL_OPTION;
        String[] btn = new String[]{"Xóa Nhóm Quyền", "Hủy"};
        String confirm = "Bạn muốn xóa \"Nhóm Quyền (" + currentRole + ")\"";
        if (!txtFieldObject.getText().isEmpty()) {
            confirm += " hay \"Quyền (" + txtFieldObject.getText() + ")\"";
            optionButton = JOptionPane.YES_NO_CANCEL_OPTION;
            btn = new String[]{"Xóa Nhóm Quyền", "Xóa Quyền", "Hủy"};
        }
        int option = JOptionPane.showOptionDialog(null,
                confirm,
                "Thông báo!",
                optionButton,
                JOptionPane.QUESTION_MESSAGE,
                null,
                btn,
                "Đồng ý");
        return option;
    }
    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        try {
            int option;
            if (cbUser.isSelected()) {
                option = JOptionPane.showConfirmDialog(this, "Bạn muốn gỡ quyền " + txtFieldObject.getText() + " !?", "Thông báo!", JOptionPane.YES_NO_OPTION);
            } else {
                option = optionDelete();
            }
            if (option == JOptionPane.CANCEL_OPTION
                    || (option == 1 && txtFieldObject.getText().isEmpty())
                    || (option == JOptionPane.NO_OPTION && cbUser.isSelected())) {
                return;
            } else if (!cbUser.isSelected()
                    && (option == JOptionPane.YES_OPTION
                    || option == JOptionPane.OK_OPTION
                    || (option == 0 && txtFieldObject.getText().isEmpty()))) {
                SystemDAO.deleteRole(currentRole);
                setRoleModel();
                loadTypeRole();
                return;
            }
            int i = (rdBtnOthers.isSelected()) ? 2 : 0;
            SystemDAO.revokeRole(currentRole, txtFieldObject.getText(), tbListPrivs.getValueAt(tbListPrivs.getSelectedRow(), i).toString());
            JOptionPane.showConfirmDialog(this, "Successfully!", "Thông báo!", JOptionPane.DEFAULT_OPTION);
            loadTypeRole();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Thông báo!", JOptionPane.DEFAULT_OPTION);
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void rdBtnSysPrivsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdBtnSysPrivsMouseClicked
        // TODO add your handling code here:
        loadTypeRole();
        setAllDisabled();
    }//GEN-LAST:event_rdBtnSysPrivsMouseClicked

    private void rdBtnOthersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdBtnOthersMouseClicked
        // TODO add your handling code here:
        loadTypeRole();
        setAllEnabled();
    }//GEN-LAST:event_rdBtnOthersMouseClicked

    private void rdBtnGrantedRolesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdBtnGrantedRolesMouseClicked
        // TODO add your handling code here:
        loadTypeRole();
        setAllDisabled();
        cbBoxGrantInsert.setText("With Grant Option");
    }//GEN-LAST:event_rdBtnGrantedRolesMouseClicked

    private void cbUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbUserMouseClicked
        // TODO add your handling code here:
        setRoleModel();
    }//GEN-LAST:event_cbUserMouseClicked

    private void loadTypeRole() {
        if (rdBtnGrantedRoles.isSelected()) {
            setGrantedRolesModel();
        } else if (rdBtnSysPrivs.isSelected()) {
            setSysPrivsModel(currentRole);
        } else {
            setOthersModel();
        }
    }

    private String getTypeObject(String object) {
        for (String key : listObjects.keySet()) {
            if (key.equals(object)) {
                return listObjects.get(key);
            }
        }
        return null;
    }

    private int getTypePrivs() {
        if (rdBtnGrantedRoles.isSelected()) {
            return 0;
        }
        if (rdBtnSysPrivs.isSelected()) {
            return 1;
        }
        return 2;
    }

    private void resetCheckBox() {
        cbBoxExecute.setSelected(false);
        cbBoxSelect.setSelected(false);
        cbBoxUpdate.setSelected(false);
        cbBoxDelete.setSelected(false);
    }

    private void setAllDisabled() {
        cbBoxExecute.setEnabled(false);
        cbBoxSelect.setEnabled(false);
        cbBoxUpdate.setEnabled(false);
        cbBoxDelete.setEnabled(false);
    }

    private void setAllEnabled() {
        cbBoxExecute.setEnabled(true);
        cbBoxSelect.setEnabled(true);
        cbBoxUpdate.setEnabled(true);
        cbBoxDelete.setEnabled(true);
    }

    private void setModel(DefaultTableModel model) {
        tbListPrivs.setModel(model);
    }

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
            java.util.logging.Logger.getLogger(mgnRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mgnRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mgnRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mgnRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mgnRole role = new mgnRole();
                role.setVisible(true);
                role.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox cbBoxDelete;
    private javax.swing.JCheckBox cbBoxExecute;
    private javax.swing.JCheckBox cbBoxGrantAdmin;
    private javax.swing.JCheckBox cbBoxGrantDelete;
    private javax.swing.JCheckBox cbBoxGrantExecute;
    private javax.swing.JCheckBox cbBoxGrantInsert;
    private javax.swing.JCheckBox cbBoxGrantSelect;
    private javax.swing.JCheckBox cbBoxGrantUpdate;
    private javax.swing.JCheckBox cbBoxInsert;
    private javax.swing.JCheckBox cbBoxInsertPriv;
    private javax.swing.JCheckBox cbBoxSelect;
    private javax.swing.JCheckBox cbBoxUpdate;
    private javax.swing.JCheckBox cbUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JRadioButton rdBtnGrantedRoles;
    private javax.swing.JRadioButton rdBtnOthers;
    private javax.swing.JRadioButton rdBtnSysPrivs;
    private javax.swing.JTable tbListPrivs;
    private javax.swing.JTable tbListRoles;
    private javax.swing.JTextField txtFieldObject;
    private javax.swing.JTextField txtFieldRole;
    private javax.swing.JTextField txtFieldUsername;
    // End of variables declaration//GEN-END:variables
}
