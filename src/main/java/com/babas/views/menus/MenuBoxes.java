package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBoxes {
    private JPanel contentPane;
    private FlatToggleButton btnBoxes;
    private FlatToggleButton btnOpenAndClosesBoxes;
    private TabbedPane tabbedPane;

    public MenuBoxes(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
        btnBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnOpenAndClosesBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getContentPane() {
        contentPane.updateUI();
        btnBoxes.updateUI();
        btnOpenAndClosesBoxes.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
