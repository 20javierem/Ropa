package com.babas.views.dialogs;

import com.babas.models.Brand;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class DBrand extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JTextField txtName;
    private JButton btnSave;
    private Brand brand;
    private boolean update;

    public DBrand(Brand brand) {
        super(Utilities.getJFrame(), "Nueva Marca", true);
        this.brand = brand;
        update = brand.getId() != null;
        init();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if (update) {
            setTitle("Actualizar Marca");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void onSave() {
        brand.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(brand);
        if (constraintViolationSet.isEmpty()) {
            brand.save();
            if (!update) {
                FPrincipal.brands.add(brand);
                FPrincipal.brandsWithAll.add(brand);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                brand = new Brand();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Marca registrada");
            } else {
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Marca actualizada");
                onHecho();
            }

        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void clear() {
        txtName.setText(null);
    }

    private void load() {
        txtName.setText(brand.getName());
    }

    private void onHecho() {
        if (update) {
            brand.refresh();
        }
        dispose();
    }

}
