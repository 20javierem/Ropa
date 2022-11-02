package com.babas.views.dialogs;

import com.babas.models.Sex;
import com.babas.models.Size;
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
import java.util.Date;
import java.util.Set;

public class DSize extends JDialog {
    private JTextField txtName;
    private JButton btnHecho;
    private JButton btnSave;
    private JPanel contentPane;
    private Size size;
    private boolean update;

    public DSize(Size size) {
        super(Utilities.getJFrame(), "Nueva Talla", true);
        this.size = size;
        update = size.getId() != null;
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
            setTitle("Actualizar Talla");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void onSave() {
        size.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(size);
        if (constraintViolationSet.isEmpty()) {
            size.save();
            if (!update) {
                FPrincipal.sizes.add(size);
                FPrincipal.sizesWithAll.add(size);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                size = new Size();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Talla registrada");
            } else {
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Talla actualizada");
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
        txtName.setText(size.getName());
    }

    private void onHecho() {
        if (update) {
            size.refresh();
        }
        dispose();
    }

}
