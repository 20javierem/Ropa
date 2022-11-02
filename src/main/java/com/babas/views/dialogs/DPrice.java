package com.babas.views.dialogs;

import com.babas.models.Price;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class DPrice extends JDialog {
    private JPanel contentPane;
    private JButton btnHecho;
    private FlatSpinner spinnerPrice;
    private JCheckBox ckDefault;
    private JButton btnSave;
    private Price price;
    private boolean update;

    public DPrice(Price price) {
        super(Utilities.getJFrame(), "Nuevo Precio", true);
        this.price = price;
        update = price.getId() != null;
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
            setTitle("Actualizar Precio");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(Utilities.getJFrame());
    }

    private void onSave() {
        price.setPrice((Double) spinnerPrice.getValue());
        if (price.getPresentation().getPrices().size() < 2) {
            price.setDefault(true);
        } else {
            price.setDefault(ckDefault.isSelected());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(price);
        if (constraintViolationSet.isEmpty()) {
            if (price.getPresentation().getId() != null) {
                price.save();
                if (price.isDefault() && price.getPresentation().getPriceDefault() != null) {
                    price.getPresentation().getPriceDefault().setDefault(false);
                    price.getPresentation().getPriceDefault().save();
                    price.getPresentation().setPriceDefault(price);
                    price.setDefault(true);
                    price.save();
                }
            }
            if (!update) {
                price.getPresentation().getPrices().add(price);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                price = new Price(price.getPresentation());
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Precio registrado");
            } else {
                if (price.isDefault()) {
                    price.getPresentation().getPriceDefault().setDefault(false);
                    price.getPresentation().getPriceDefault().save();
                    price.getPresentation().setPriceDefault(price);
                    price.setDefault(true);
                    price.save();
                }
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Precio actualizado");
                onHecho();
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void clear() {
        spinnerPrice.setValue(1.0);
    }

    private void load() {
        spinnerPrice.setValue(price.getPrice());
        ckDefault.setSelected(price.isDefault());
        ckDefault.setEnabled(!price.isDefault());
    }

    private void onHecho() {
        if (update) {
            price.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerPrice = new FlatSpinner();
        spinnerPrice.setModel(new SpinnerNumberModel(1.00, 0.01, 100000.00, 0.50));
        spinnerPrice.setEditor(Utilities.getEditorPrice(spinnerPrice));
    }

}