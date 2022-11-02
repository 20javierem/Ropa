package com.babas.views.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

public class DesingTxtTable {
    private JPanel contentPane;
    private JLabel string0;
    private JLabel string1;
    private JLabel string2;
    private JPanel pane;

    public DesingTxtTable(Component component) {
        contentPane.setBorder(null);
        contentPane.setBackground(component.getBackground());
        this.string0.setForeground(component.getForeground());
        this.string0.setBackground(component.getBackground());
        this.string1.setBackground(new JTextField().getSelectionColor());
        this.string1.setForeground(new JTextField().getSelectedTextColor());
        this.string2.setForeground(component.getForeground());
        this.string2.setBackground(component.getBackground());
    }

    public void setHorizontalAlignment(int aligment) {
        switch (aligment) {
            case 0:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) contentPane.getLayout();
                gridLayoutManager.addLayoutComponent(pane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
                break;
            case 4:
                gridLayoutManager = (GridLayoutManager) contentPane.getLayout();
                gridLayoutManager.addLayoutComponent(pane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
                break;
            default:
                break;
        }
    }

    public void setString0(String string0) {
        this.string0.setText(string0);
    }

    public void setString1(String string1) {
        this.string1.setText(string1);
    }

    public void setString2(String string2) {
        this.string2.setText(string2);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

}
