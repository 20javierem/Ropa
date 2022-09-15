package com.babas.views.dialogs;

import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class DSettings extends JDialog{
    private JTabbedPane tabbedPane1;
    private JButton hechoButton;
    private JButton btnSave;
    private JPanel contentPane;
    private FlatComboBox cbbThemes;
    private FlatComboBox cbbFontSize;
    private JButton btnApply;
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
        Utilities.propiedades.guardar();
        changeTheme();
        Utilities.updateUI();
        pack();
    }
    private void changeTheme(){
        Utilities.propiedades.setTema(String.valueOf(cbbThemes.getSelectedItem()));
        Utilities.propiedades.guardar();
        Utilities.loadTheme();
        Utilities.updateUI();
    }

    private void save(){
        onDispose();
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ÉXITO","Cambios guardados");
    }

    private void onDispose(){
        dispose();
    }

    private void onCancel(){
        Utilities.loadTheme();
        Utilities.updateUI();
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
    }
}
