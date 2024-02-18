/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package panda.Collection;

import dao.CardDAO;
import dao.CollectionDAO;
import dao.FlashcardDAO;
import dao.SentenceDAO;
import dao.VocabDAO;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Card;
import model.Collection;
import model.Sentence;
import model.Flashcard;
import model.Vocab;
import panda.user.CpnExampleVocab;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Khoa
 */
public class FrmAddFlashcard extends javax.swing.JFrame {

    /**
     * Creates new form FrmAddFlashcard
     */
    private ArrayList<Collection> listCollections;
    private int iCollectionSelected = -1;
    private int idCollectionSelected = -1;
    private boolean isFlashcard = true;
    private ArrayList<CpnExampleVocab> cpnExampleVocabArr;
    private DefaultTableModel tbModel;

    public FrmAddFlashcard() {
        initComponents();
        listCollections = CollectionDAO.load();
        pnVocab.setVisible(true);
        //cbBoxCollection;
        //tbFlashcard
        //cbBoxPOS

        for (Collection c : listCollections) {
            cbBoxCollection.addItem(c.getName());
        }
        for (String pos : Vocab.partofspeeches) {
            //cbBoxPOS.addItem(pos);
            JCheckBox cb = new JCheckBox(pos);
            pnCheckboxPOS.add(new JCheckBox(pos));
        }
        tbModel = (DefaultTableModel) tbCard.getModel();
        cpnExampleVocabArr = new ArrayList<CpnExampleVocab>();
//        cpnExampleVocabArr.add(new CpnExampleVocab());
//        cpnExampleVocabArr.add(new CpnExampleVocab());
//        cpnExampleVocabArr.add(new CpnExampleVocab());
//        cpnExampleVocabArr.add(new CpnExampleVocab());
//        for(int i = 0; i < cpnExampleVocabArr.size(); i++){
//            jPanel9.add(cpnExampleVocabArr.get(i));
//        }
    }

//    private boolean initSample() {
//        try {
//            JPanel pnSentenceX = new JPanel();
//            JPanel pnBoxSampleX = new JPanel();
//            JButton btnDeleteSentenceX = new JButton("Delete");
//            JButton btnSaveSampleX = new JButton("Save");
//            JTextField jTextFieldX3 = new JTextField("Translated sample language");
//            JTextField jTextFieldX2 = new JTextField("Origin sample language");
//
//            javax.swing.GroupLayout pnSentenceLayout = new javax.swing.GroupLayout(pnSentenceX);
//            pnSentenceX.setLayout(pnSentenceLayout);
//            pnSentenceLayout.setHorizontalGroup(
//                    pnSentenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                            .addGroup(pnSentenceLayout.createSequentialGroup()
//                                    .addGap(18, 18, 18)
//                                    .addGroup(pnSentenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                            .addComponent(jTextFieldX3, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
//                                            .addComponent(jTextFieldX2))
//                                    .addContainerGap())
//            );
//            pnSentenceLayout.setVerticalGroup(
//                    pnSentenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                            .addGroup(pnSentenceLayout.createSequentialGroup()
//                                    .addGap(4, 4, 4)
//                                    .addComponent(jTextFieldX2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                    .addComponent(jTextFieldX3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//            );
//
//            javax.swing.GroupLayout pnBoxSampleLayout = new javax.swing.GroupLayout(pnBoxSampleX);
//            pnBoxSampleX.setLayout(pnBoxSampleLayout);
//            pnBoxSampleLayout.setHorizontalGroup(
//                    pnBoxSampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                            .addGroup(pnBoxSampleLayout.createSequentialGroup()
//                                    .addContainerGap()
//                                    .addComponent(pnSentenceX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                    .addGroup(pnBoxSampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                            .addComponent(btnDeleteSentenceX, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                            .addComponent(btnSaveSampleX, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                    .addGap(20, 20, 20))
//            );
//            pnBoxSampleLayout.setVerticalGroup(
//                    pnBoxSampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                            .addGroup(pnBoxSampleLayout.createSequentialGroup()
//                                    .addContainerGap()
//                                    .addGroup(pnBoxSampleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
//                                            .addComponent(pnSentenceX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                            .addGroup(pnBoxSampleLayout.createSequentialGroup()
//                                                    .addComponent(btnSaveSampleX)
//                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                    .addComponent(btnDeleteSentenceX)))
//                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//            );
//
//            jPanel9.add(pnBoxSampleX);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbBoxType = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        lbBack = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaBack = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        lbFront = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaFront = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        lbNotify = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCard = new javax.swing.JTable();
        cbBoxCollection = new javax.swing.JComboBox<>();
        cbBoxSort = new javax.swing.JComboBox<>();
        pnVocab = new javax.swing.JPanel();
        pnPOS = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnCheckboxPOS = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFieldIPA = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        btnAddSample = new javax.swing.JButton();
        ckBoxAddVocabInSample = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Type:");

        cbBoxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Card", "Vocab" }));
        cbBoxType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBoxTypeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbBoxType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbBack.setText("Back (Meaning):");

        txtAreaBack.setColumns(20);
        txtAreaBack.setRows(5);
        jScrollPane3.setViewportView(txtAreaBack);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbBack)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbFront.setText("Front (Word):");

        txtAreaFront.setColumns(20);
        txtAreaFront.setRows(5);
        txtAreaFront.setNextFocusableComponent(txtAreaBack);
        txtAreaFront.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAreaFrontFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(txtAreaFront);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lbFront)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFront)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSave.setText("Save All");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbNotify, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(lbNotify))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tbCard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Front", "Back", "Type", "Part of speech"
            }
        ));
        jScrollPane1.setViewportView(tbCard);

        cbBoxCollection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Collection --" }));
        cbBoxCollection.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBoxCollectionItemStateChanged(evt);
            }
        });
        cbBoxCollection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBoxCollectionActionPerformed(evt);
            }
        });

        cbBoxSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbBoxCollection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbBoxSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbBoxCollection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbBoxSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("Part of speech:");

        pnCheckboxPOS.setLayout(new java.awt.GridLayout(0, 2));

        javax.swing.GroupLayout pnPOSLayout = new javax.swing.GroupLayout(pnPOS);
        pnPOS.setLayout(pnPOSLayout);
        pnPOSLayout.setHorizontalGroup(
            pnPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPOSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(pnCheckboxPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnPOSLayout.setVerticalGroup(
            pnPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPOSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnCheckboxPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("IPA:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtFieldIPA))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFieldIPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane4.setViewportView(jPanel9);

        btnAddSample.setText("Add sample");
        btnAddSample.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddSampleMouseClicked(evt);
            }
        });

        ckBoxAddVocabInSample.setText("Add vocab in sample");

        javax.swing.GroupLayout pnVocabLayout = new javax.swing.GroupLayout(pnVocab);
        pnVocab.setLayout(pnVocabLayout);
        pnVocabLayout.setHorizontalGroup(
            pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnVocabLayout.createSequentialGroup()
                .addGroup(pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnVocabLayout.createSequentialGroup()
                        .addGroup(pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnVocabLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7))
                    .addGroup(pnVocabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ckBoxAddVocabInSample)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddSample)
                        .addGap(14, 14, 14)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnVocabLayout.setVerticalGroup(
            pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnVocabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnVocabLayout.createSequentialGroup()
                        .addComponent(pnPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnVocabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddSample)
                            .addComponent(ckBoxAddVocabInSample)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnVocab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnVocab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbBoxTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBoxTypeItemStateChanged
        // TODO add your handling code here:
        int type = cbBoxType.getSelectedIndex();
        if (type == 1) {
            isFlashcard = false;
            pnVocab.setVisible(true);
        } else {
            isFlashcard = true;
        }
    }//GEN-LAST:event_cbBoxTypeItemStateChanged

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        String front = txtAreaFront.getText();
        String back = txtAreaBack.getText();
        cbBoxType.getSelectedIndex();
        //cbBoxPOS.getSelectedIndex();
        txtFieldIPA.getText();

        if (isFlashcard) {
            Flashcard flashcard = new Flashcard();
            flashcard.setFront(front);
            flashcard.setBack(back);
            if (FlashcardDAO.add(flashcard, idCollectionSelected)) {
                lbNotify.setText("Successfully!");
                addRow(setDataRowTable(flashcard));
            } else {
                lbNotify.setText("Failed!");
            }
        } else {
            String ipa = txtFieldIPA.getText();

            Vocab v = new Vocab();
            v.setWord(front);
            v.setMeaning(back);
            v.setIpa(ipa);
            v.setSentence(convertToExampleArr());
            boolean isSavedVocab = false;
            for (Component c : pnCheckboxPOS.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox d = (JCheckBox) c;
                    if (d.isSelected()) {
                        v.setPartOfSpeech(d.getText());
                        if (VocabDAO.add(v)) {
                            addRow(setDataRowTable(v));
                            isSavedVocab = true;
                        }
                    }
                }
            }

            if (isSavedVocab && (SentenceDAO.add(v) || v.getSentence().isEmpty())) {
                lbNotify.setText("Successfully");
            }
        }
    }//GEN-LAST:event_btnSaveMouseClicked

    private void addRow(Object[] data) {
        tbModel.addRow(data);
    }

    private ArrayList<Sentence> convertToExampleArr() {
        ArrayList<Sentence> arr = new ArrayList<>();
        for (int i = 0; i < cpnExampleVocabArr.size(); i++) {
            arr.add(cpnExampleVocabArr.get(i).getExample());
        }
        return arr;
    }

    private void btnAddSampleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSampleMouseClicked
        // TODO add your handling code here:
        cpnExampleVocabArr.add(new CpnExampleVocab());
        jPanel9.add(cpnExampleVocabArr.get(cpnExampleVocabArr.size() - 1));
        revalidate();
        repaint();
    }//GEN-LAST:event_btnAddSampleMouseClicked

    private void cbBoxCollectionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBoxCollectionItemStateChanged
        // TODO add your handling code here:
        int index = cbBoxCollection.getSelectedIndex() - 1;
        if (index == -1) {
            loadJTable(null);
        } else if (index != iCollectionSelected) {
            iCollectionSelected = cbBoxCollection.getSelectedIndex() - 1;
            idCollectionSelected = listCollections.get(iCollectionSelected).getId();
            ArrayList<Card> cards = CardDAO.load(listCollections.get(iCollectionSelected).getId());
            loadJTable(cards);
        }
    }//GEN-LAST:event_cbBoxCollectionItemStateChanged

    private void cbBoxCollectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBoxCollectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBoxCollectionActionPerformed

    private void txtAreaFrontFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaFrontFocusLost
        try {
            // TODO add your handling code here:
            String word = txtAreaFront.getText();

            String url = "https://dictionary.cambridge.org/dictionary/english/word";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String html = response.body();
            Pattern pagePattern = Pattern.compile(
                    //"<div class=\\\"def ddef_d db\\\">(?:.|\\n)*?</div>"
                    //"<div class=\\\"def ddef_d db\\\">(?:.|\\n)*?([^<]+)</div>"
                    "<div class=\\\"def ddef_d db\\\">(?:\\\\s+.*?|([^<]+))(?:\\\\s+.*?|</a>)?"
            );
            Matcher matcher = pagePattern.matcher(html);
            String meaning= "";
            if (matcher.find()) {
                meaning = matcher.group();
            }
            int a = 1+1;

//                Pattern pattern = Pattern.compile("(?:[^<]+|<(?![/a]|/?a[^>]*>))+?");
//                matcher = pattern.matcher(html);
//
//                StringBuilder text = new StringBuilder();
//                while (matcher.find()) {
//                    text.append(matcher.group());
//                }
//                System.out.println(text);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FrmAddFlashcard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(FrmAddFlashcard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_txtAreaFrontFocusLost

    private String extractText(String html) {
        return html.replaceAll("<[^>]+>", "");
    }

    private void loadJTable(ArrayList<Card> cards) {
        DefaultTableModel tblModel = (DefaultTableModel) tbCard.getModel();
        tblModel.setRowCount(0);
        if (cards == null) {
            return;
        }
        if (cards.isEmpty()) {
            return;
        }
        for (Card c : cards) {
            Object[] data = setDataRowTable(c);
            tblModel.addRow(data);
        }
    }

    private Object[] setDataRowTable(Card c) {
        Object data[] = {};
        if (c instanceof Vocab) {
            Vocab vocab = (Vocab) c;
            data = new String[]{
                vocab.getWord(),
                vocab.getMeaning(),
                "Vocab",
                vocab.getPartOfSpeech()
            };
        } else {
            Flashcard flashcard = (Flashcard) c;
            data = new String[]{
                flashcard.getFront(),
                flashcard.getBack(),
                "Flashcard",
                null
            };
        }
        return data;
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
            java.util.logging.Logger.getLogger(FrmAddFlashcard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmAddFlashcard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmAddFlashcard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmAddFlashcard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmAddFlashcard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSample;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbBoxCollection;
    private javax.swing.JComboBox<String> cbBoxSort;
    private javax.swing.JComboBox<String> cbBoxType;
    private javax.swing.JCheckBox ckBoxAddVocabInSample;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbBack;
    private javax.swing.JLabel lbFront;
    private javax.swing.JLabel lbNotify;
    private javax.swing.JPanel pnCheckboxPOS;
    private javax.swing.JPanel pnPOS;
    private javax.swing.JPanel pnVocab;
    private javax.swing.JTable tbCard;
    private javax.swing.JTextArea txtAreaBack;
    private javax.swing.JTextArea txtAreaFront;
    private javax.swing.JTextField txtFieldIPA;
    // End of variables declaration//GEN-END:variables
}
