package com.babas.views.dialogs;

import com.babas.models.Category;
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

public class DCategory extends JDialog {
    private JTextField txtName;
    private JButton btnHecho;
    private JButton btnSave;
    private JPanel contentPane;
    private Category category;
    private boolean update;

    public DCategory(Category category) {
        super(Utilities.getJFrame(), "Nueva Categoría", true);
        this.category = category;
        update = category.getId() != null;
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
            setTitle("Actualizar Categoría");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void onSave() {
        category.setName(txtName.getText());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(category);
        if (constraintViolationSet.isEmpty()) {
            category.save();
            if (!update) {
                FPrincipal.categories.add(category);
                FPrincipal.categoriesWithAll.add(category);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                category = new Category();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Categoría registrado");
            } else {
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Categoría actualizado");
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
        txtName.setText(category.getName());
    }

    private void onHecho() {
        if (update) {
            category.refresh();
        }
        dispose();
    }

}
