package com.babas.views.dialogs;

import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class DSettings extends JDialog{
    private JTabbedPane tabbedPane1;
    private JButton hechoButton;
    private JButton btnSave;
    private JPanel contentPane;
    private FlatComboBox cbbThemes;
    private FlatComboBox cbbFontSize;
    private JButton btnApply;
    private JRadioButton rbSaleAlways;
    private JRadioButton rbReserveAlways;
    private JRadioButton rbRentalAlways;
    private JRadioButton rbSaleNever;
    private JRadioButton rbReserveNever;
    private JRadioButton rbRentalNever;
    private JRadioButton rbSaleQuestion;
    private JRadioButton rbReserveQuestion;
    private JRadioButton rbRentalQuestion;
    private FPrincipal fPrincipal;

    public DSettings(FPrincipal fPrincipal){
        super(Utilities.getJFrame(),"Configuraciones",true);
        this.fPrincipal=fPrincipal;
        initComponents();
        cbbThemes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTheme();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        hechoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        ((JTextField)cbbFontSize.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verify();
            }
        });
    }

    private void verify(){
        String newSize=((JTextField) cbbFontSize.getEditor().getEditorComponent()).getText();
        btnApply.setEnabled(!String.valueOf(Utilities.propiedades.getFont().getSize()).equals(newSize));
    }

    private void apply(){
        Utilities.propiedades.setFont(String.valueOf(cbbFontSize.getSelectedItem()));
        Utilities.propiedades.save();
        changeTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        pack();
    }
    private void changeTheme(){
        Utilities.propiedades.setTema(String.valueOf(cbbThemes.getSelectedItem()));
        Utilities.propiedades.save();
        Utilities.loadTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        ((JTextField)cbbFontSize.getEditor().getEditorComponent()).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verify();
            }
        });
    }

    private void save(){
        if(rbSaleAlways.isSelected()){
            Utilities.propiedades.setPrintTicketSale("always");
        }else if(rbSaleNever.isSelected()){
            Utilities.propiedades.setPrintTicketSale("never");
        }else{
            Utilities.propiedades.setPrintTicketSale("question");
        }

        if(rbReserveAlways.isSelected()){
            Utilities.propiedades.setPrintTicketReserve("always");
        }else if(rbReserveNever.isSelected()){
            Utilities.propiedades.setPrintTicketReserve("never");
        }else{
            Utilities.propiedades.setPrintTicketReserve("question");
        }

        if(rbRentalAlways.isSelected()){
            Utilities.propiedades.setPrintTicketRental("always");
        }else if(rbRentalNever.isSelected()){
            Utilities.propiedades.setPrintTicketRental("never");
        }else{
            Utilities.propiedades.setPrintTicketRental("question");
        }

        Utilities.propiedades.save();
        onDispose();
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ÉXITO","Cambios guardados");
    }

    private void onDispose(){
        dispose();
    }
    private void onCancel(){
        Utilities.loadTheme();
        Utilities.updateUI(true);
        Utilities.updateComponents(Utilities.getJFrame().getRootPane());
        Utilities.updateComponents(getRootPane());
        Utilities.updateUI(false);
        onDispose();
    }
    private void initComponents(){
        setContentPane(contentPane);
        pack();
        getRootPane().setDefaultButton(btnSave);
        loadSetings();
        setLocationRelativeTo(null);
    }
    private void loadSetings(){
        cbbThemes.setSelectedItem(Utilities.propiedades.getTema());
        cbbFontSize.setSelectedItem(Utilities.propiedades.getFont().getSize());
        if(Utilities.propiedades.getPrintTicketSale().equals("always")){
            rbSaleAlways.setSelected(true);
        }else if(Utilities.propiedades.getPrintTicketSale().equals("never")){
            rbSaleNever.setSelected(true);
        }else{
            rbSaleQuestion.setSelected(true);
        }

        if(Utilities.propiedades.getPrintTicketReserve().equals("always")){
            rbReserveAlways.setSelected(true);
        }else if(Utilities.propiedades.getPrintTicketReserve().equals("never")){
            rbReserveNever.setSelected(true);
        }else{
            rbReserveQuestion.setSelected(true);
        }

        if(Utilities.propiedades.getPrintTicketRental().equals("always")){
            rbRentalAlways.setSelected(true);
        }else if(Utilities.propiedades.getPrintTicketRental().equals("never")){
            rbRentalNever.setSelected(true);
        }else{
            rbRentalQuestion.setSelected(true);
        }
    }
}
