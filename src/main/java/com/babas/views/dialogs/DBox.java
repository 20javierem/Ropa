package com.babas.views.dialogs;

import com.babas.models.Box;
import com.babas.models.Branch;
import com.babas.models.Brand;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class DBox extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JComboBox cbbBranch;
    private FlatTextField txtName;
    private JButton btnSave;
    private JCheckBox ckActive;
    private Box box;
    private boolean update;
    private int pX, pY;

    public DBox(Box box) {
        super(Utilities.getJFrame(), true);
        this.box = box;
        update = box.getId() != null;
        init();
        contentPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
    }

    private void init() {
        setUndecorated(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if (update) {
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        } else {
            loadCombos();
        }
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadCombos() {
        cbbBranch.setModel(new DefaultComboBoxModel(FPrincipal.branchs));
        cbbBranch.setRenderer(new Branch.ListCellRenderer());
        if (box.getBranch() != null) {
            cbbBranch.setSelectedItem(box.getBranch());
        }
    }

    private void onSave() {
        box.setName(txtName.getText().trim());
        if (!update) {
            box.setBranch((Branch) cbbBranch.getSelectedItem());
        }
        box.setActive(ckActive.isSelected());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(box);
        if (constraintViolationSet.isEmpty()) {
            box.save();
            if (!update) {
                box.getBranch().getBoxs().add(box);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                box = new Box();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Caja registrada");
            } else {
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Caja actualizada");
                onHecho();
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho() {
        if (update) {
            box.refresh();
        }
        dispose();
    }

    private void clear() {
        txtName.setText(null);
    }

    private void load() {
        cbbBranch.addItem(box.getBranch().getName());
        txtName.setText(box.getName());
        ckActive.setSelected(box.isActive());
    }

}
