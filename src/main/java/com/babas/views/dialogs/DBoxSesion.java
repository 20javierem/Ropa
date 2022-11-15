package com.babas.views.dialogs;

import com.babas.controllers.BoxSessions;
import com.babas.models.Box;
import com.babas.models.BoxSession;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DBoxSesion extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbBox;
    private FlatSpinner spinnerAmountInitial;
    private FlatSpinner spinnerAmountToDeliver;
    private FlatSpinner spinnerAmountDelivered;
    private JLabel lblDateStart;
    private JComboBox cbbUser;
    private BoxSession boxSession;
    private boolean update;

    public DBoxSesion() {
        super(Utilities.getJFrame(), "Apertura de caja", true);
        boxSession = Babas.boxSession;
        $$$setupUI$$$();
        update = boxSession.getId() != null;
        init();
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
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        cbbUser.addItem(Babas.user.getUserName());
        if (update) {
            load();
            btnSave.setText("Cerrar caja");
        } else {
            boxSession.setUser(Babas.user);
            spinnerAmountDelivered.setEnabled(false);
            Vector<Box> boxes = new Vector<>();
            Babas.user.getBranchs().forEach(branch -> {
                branch.getBoxs().forEach(box -> {
                    if (box.isActive()) {
                        boxes.add(box);
                    }
                });
            });
            cbbBox.setModel(new DefaultComboBoxModel(boxes));
            cbbBox.setRenderer(new Box.ListCellRenderer());
        }
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void load() {
        cbbBox.addItem(boxSession.getBox().getName() + " / " + boxSession.getBox().getBranch().getName());
        spinnerAmountInitial.setValue(boxSession.getAmountInitial());
        spinnerAmountToDeliver.setValue(boxSession.getAmountToDelivered());
        spinnerAmountDelivered.setValue(boxSession.getAmountDelivered());
        lblDateStart.setText(Utilities.formatoFechaHora.format(boxSession.getCreated()));
        spinnerAmountInitial.setEnabled(false);
    }

    private void onSave() {
        if (!update) {
            boxSession.setAmountInitial((Double) spinnerAmountInitial.getValue());
            boxSession.setUser(Babas.user);
            boxSession.setBox((Box) cbbBox.getSelectedItem());
        } else {
            boxSession.setAmountDelivered((Double) spinnerAmountDelivered.getValue());
            boxSession.setUpdated(new Date());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(boxSession);
        if (constraintViolationSet.isEmpty()) {
            int index = 0;
            if (!update) {
                BoxSession boxSession1 = BoxSessions.getByBox(boxSession.getBox());
                if (boxSession1 != null) {
                    if (!Objects.equals(boxSession1.getUser().getId(), boxSession.getUser().getId())) {
                        index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Ya se abrió esta caja, ¿Desea cerrar la caja " + boxSession.getBox().getName() + "?", "Caja abierta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si, cerrar caja", "Cancelar"}, "Si, continar sesión");
                    } else {
                        index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Tiene una sesion abierta en esta caja, desea continuar la sessión", "Caja abierta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si, continar sesión", "No, iniciar una nueva sesión"}, "Si, continar sesión");
                        if (index == 0) {
                            Babas.boxSession = boxSession1;
                            Babas.boxSession.calculateTotals();
                            index = 1;
                            Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(boxSession1.getAmountToDelivered()));
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Caja recuperada");
                            onHecho();
                        } else if (index == 1) {
                            boxSession1.setAmountDelivered(boxSession1.getAmountToDelivered());
                            boxSession1.setUpdated(new Date());
                            boxSession1.save();
                            index = 0;
                        }
                    }
                }
            }
            if (index == 0) {
                boxSession.save();
                if (update) {
                    Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(0.00));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Caja cerrada");
                    Babas.boxSession = new BoxSession();
                } else {
                    boxSession.calculateTotals();
                    Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(boxSession.getAmountToDelivered()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Caja aperturada");
                }
                onHecho();
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho() {
        if (update) {
            boxSession.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerAmountInitial = new FlatSpinner();
        spinnerAmountInitial.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountInitial.setEditor(Utilities.getEditorPrice(spinnerAmountInitial));
        spinnerAmountToDeliver = new FlatSpinner();
        spinnerAmountToDeliver.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountToDeliver.setEditor(Utilities.getEditorPrice(spinnerAmountToDeliver));
        spinnerAmountDelivered = new FlatSpinner();
        spinnerAmountDelivered.setModel(new SpinnerNumberModel(0.0, 0.0, 100000, 1.0));
        spinnerAmountDelivered.setEditor(Utilities.getEditorPrice(spinnerAmountDelivered));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, 10));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), 5, 10));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Usuario:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Caja:");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBox = new JComboBox();
        panel1.add(cbbBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(210, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Montio inicial:");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Monto a entregar:");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Monto entregado:");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(spinnerAmountInitial, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        spinnerAmountToDeliver.setEnabled(false);
        panel1.add(spinnerAmountToDeliver, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        panel1.add(spinnerAmountDelivered, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Fecha/hora de inicio:");
        panel1.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDateStart = new JLabel();
        lblDateStart.setText("");
        panel1.add(lblDateStart, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbUser = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        cbbUser.setModel(defaultComboBoxModel1);
        panel1.add(cbbUser, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(210, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Cancelar");
        panel2.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("Aperturar caja");
        panel2.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
