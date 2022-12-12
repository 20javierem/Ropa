package com.babas.views.dialogs;

import com.babas.controllers.Clients;
import com.babas.models.Client;
import com.babas.models.Sex;
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
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Set;

public class DClient extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private JButton btnSave;
    private FlatTextField txtDocument;
    private FlatTextField txtNames;
    private FlatTextField txtAdress;
    private FlatTextField txtPhone;
    private Client client;
    private boolean update;
    private boolean fprincipal;

    public DClient(boolean fprincipal, Client client) {
        super(Utilities.getJFrame(), "Nuevo Cliente", true);
        this.client = client;
        this.fprincipal = fprincipal;
        update = client.getId() != null;
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
            setTitle("Actualizar Cliente");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        if (fprincipal) {
            setTitle("Cliente");
            txtDocument.setEnabled(false);
            txtNames.setEnabled(false);
            txtAdress.setEnabled(false);
            txtPhone.setEnabled(false);
            btnSave.setVisible(false);
            btnHecho.setText("Hecho");
            getRootPane().setDefaultButton(btnHecho);
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void onSave() {
        client.setDni(txtDocument.getText().trim());
        client.setNames(txtNames.getText().trim());
        client.setAddres(txtAdress.getText().trim());
        client.setPhone(txtPhone.getText().trim());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(client);
        if (constraintViolationSet.isEmpty()) {
            if (!update) {
                if (Clients.getByDNI(client.getDni()) == null) {
                    client.save();
                    FPrincipal.clients.add(client);
                    Utilities.updateDialog();
                    if (Utilities.getTabbedPane() != null) {
                        Utilities.getTabbedPane().updateTab();
                    } else {
                        onHecho();
                    }
                    client = new Client();
                    clear();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Cliente registrado");
                } else {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El documento ya está registrado");
                }
            } else {
                Client client1 = Clients.getByDNI(client.getDni());
                if (client1 == null || Objects.equals(client1.getId(), client.getId())) {
                    client.save();
                    Utilities.updateDialog();
                    Utilities.getTabbedPane().updateTab();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Cliente actualizado");
                    onHecho();
                } else {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El documento ya está registrado");

                }

            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void clear() {
        txtDocument.setText(null);
        txtNames.setText(null);
        txtAdress.setText(null);
        txtPhone.setText(null);
    }

    private void load() {
        txtDocument.setText(client.getDni());
        txtNames.setText(client.getNames());
        txtAdress.setText(client.getAddres());
        txtPhone.setText(client.getPhone());
    }

    private void onHecho() {
        if (update) {
            client.refresh();
        }
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
        contentPane.setPreferredSize(new Dimension(450, 242));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Dni/Ruc:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Cliente:");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Dirección:");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("N° celular:");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDocument = new FlatTextField();
        txtDocument.setPlaceholderText("Obligatorio");
        panel1.add(txtDocument, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtNames = new FlatTextField();
        txtNames.setPlaceholderText("Obligatorio");
        panel1.add(txtNames, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtAdress = new FlatTextField();
        panel1.add(txtAdress, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtPhone = new FlatTextField();
        panel1.add(txtPhone, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel2.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("Registrar");
        panel2.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}