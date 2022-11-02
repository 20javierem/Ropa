package com.babas.views.frames;

import com.babas.models.Product;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class Diseño {
    private JPanel contentPane;
    private JLabel lblName;
    private JCheckBox ckActive;

    public Diseño(Product product) {
        lblName.setText(product.getStyle().getName());
        ckActive.setSelected(product.isActive());
    }

    public JPanel getContentPane() {
        return contentPane;
    }

}
