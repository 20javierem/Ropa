package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Rental;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailRental;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailRental2;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailRentalCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DaddProductToRental;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class TabNewRental {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatSpinner spinnerDiscount;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JButton btnSaleWithTrasnfer;
    private JButton btnSaleWithCash;
    private JLabel lblLogo;
    private JDateChooser jDateFinish;
    private FlatSpinner spinnerWarranty;
    private JLabel lblTotalCurrent;
    private JLabel lblReserve;
    private Rental rental;
    private DetailRentalAbstractModel model;

    public TabNewRental(Rental rental) {
        this.rental = rental;
        init();
        btnAddProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddProducts();
            }
        });
        txtDocument.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchClient();
                }
            }
        });
        btnSaleWithCash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(true);
            }
        });
        btnSaleWithTrasnfer.addActionListener(e -> onSave(false));
        ((JSpinner.NumberEditor) spinnerDiscount.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField textField = (JTextField) e.getSource();
                    textField.selectAll();
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    rental.setDiscount((Double) spinnerDiscount.getValue());
                    loadTotals();
                });
            }
        });
        ((JSpinner.NumberEditor) spinnerWarranty.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField textField = (JTextField) e.getSource();
                    textField.selectAll();
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    rental.setWarranty((Double) spinnerWarranty.getValue());
                    loadTotals();
                });
            }
        });
    }

    private void init() {
        if (rental.getReserve() != null) {
            tabPane.setTitle("Alquiler/Reserva Nro: " + rental.getReserve().getNumberReserve());
            lblReserve.setText(Utilities.moneda.format(rental.getReserve().getAdvance()));
            txtDocument.setText(rental.getReserve().getClient().getDni());
            searchClient();
        } else {
            tabPane.setTitle("Nuevo alquiler");
        }
        load();
        loadTotals();
        tabPane.getActions().addActionListener(e -> {
            model.fireTableDataChanged();
            loadTotals();
        });
        if (!Babas.company.getLogo().isBlank()) {
            if (Utilities.iconCompanyx255x220 != null) {
                lblLogo.setIcon(Utilities.iconCompanyx255x220);
            }
        }
    }

    private void searchClient() {
        Client client = Clients.getByDNI(txtDocument.getText().trim());
        if (client != null) {
            txtNameClient.setText(client.getNames());
            txtPhone.setText(client.getPhone());
            txtMail.setText(client.getMail());
        }
    }

    private void loadAddProducts() {
        if (Babas.boxSession.getId() != null) {
            rental.setBranch(Babas.boxSession.getBox().getBranch());
        } else {
            rental.setBranch(null);
        }
        if (rental.getBranch() != null) {
            DaddProductToRental daddProductToRental = new DaddProductToRental(rental);
            daddProductToRental.setVisible(true);
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe abrir caja para comenzar");
        }
    }

    private void load() {
        System.out.println("detalles: " + rental.getDetailRentals().size());
        model = new DetailRentalAbstractModel(rental.getDetailRentals());
        table.setModel(model);
        DetailRentalCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailRental2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailRental());
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailRental());
        loadTotals();
        model.fireTableDataChanged();
    }

    private void loadTotals() {
        if (Babas.boxSession.getId() == null) {
            rental.getDetailRentals().clear();
            rental.setBranch(null);
        }
        rental.calculateTotals();
        lblSubTotal.setText(Utilities.moneda.format(rental.getTotal()));
        spinnerWarranty.setValue(rental.getWarranty());
        if (rental.getReserve() == null) {
            lblTotal.setText(Utilities.moneda.format(rental.getTotalCurrent() + rental.getDiscount()));
        } else {
            lblTotal.setText(Utilities.moneda.format(rental.getTotalCurrent() + rental.getDiscount() + rental.getReserve().getAdvance()));
        }
        spinnerDiscount.setValue(rental.getDiscount());
        lblTotalCurrent.setText(Utilities.moneda.format(rental.getTotalCurrent()));
        Utilities.getLblCentro().setText("Nuevo alquiler");
    }

    private void onSave(boolean isCash) {
        if (Babas.boxSession.getId() != null) {
            rental.setBranch(Babas.boxSession.getBox().getBranch());
            rental.setCash(isCash);
            rental.setClient(getClient());
            rental.setBoxSession(Babas.boxSession);
            rental.setUser(Babas.user);
            rental.setEnded(jDateFinish.getDate());
            Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(rental);
            if (constraintViolationSet.isEmpty()) {
                boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Comfirmar Alquiler", JOptionPane.YES_NO_OPTION) == 0;
                if (si) {
                    rental.save();
                    FPrincipal.rentalsActives.add(0, rental);
                    Babas.boxSession.getRentals().add(0, rental);
                    Babas.boxSession.calculateTotals();
                    Utilities.getLblIzquierda().setText("Alquiler registrado Nro. " + rental.getNumberRental() + " :" + Utilities.formatoFechaHora.format(rental.getCreated()));
                    Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Alquiler registrado");
                    if (Utilities.propiedades.getPrintTicketRental().equals("always")) {
                        UtilitiesReports.generateTicketRental(rental, true);
                    } else if (Utilities.propiedades.getPrintTicketRental().equals("question")) {
                        si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Imprimir?", "Ticket de alquiler", JOptionPane.YES_NO_OPTION) == 0;
                        if (si) {
                            UtilitiesReports.generateTicketRental(rental, true);
                        }
                    }
                    rental = new Rental();
                    clear();
                    Utilities.getTabbedPane().updateTab();
                }
            } else {
                ProgramValidator.mostrarErrores(constraintViolationSet);
            }

        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe aperturar caja");
        }
    }

    private Client getClient() {
        Client client = null;
        if (!txtDocument.getText().isBlank() && !txtNameClient.getText().isBlank()) {
            client = Clients.getByDNI(txtDocument.getText().trim());
            if (client == null) {
                client = new Client();
                client.setDni(txtDocument.getText().trim());
            }
            client.setNames(txtNameClient.getText().trim());
            client.setMail(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
        }
        return client;
    }

    private void clear() {
        txtMail.setText(null);
        txtDocument.setText(null);
        txtPhone.setText(null);
        txtNameClient.setText(null);
        spinnerWarranty.setValue(0.0);
        spinnerDiscount.setValue(0.0);
        load();
        loadTotals();
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerDiscount = new FlatSpinner();
        spinnerDiscount.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerDiscount.setEditor(Utilities.getEditorPrice(spinnerDiscount));
        spinnerWarranty = new FlatSpinner();
        spinnerWarranty.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerWarranty.setEditor(Utilities.getEditorPrice(spinnerWarranty));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        jDateFinish = new JDateChooser(calendar.getTime());
        jDateFinish.setMinSelectableDate(calendar.getTime());
        jDateFinish.setDateFormatString(Utilities.getFormatoFecha());
    }

}
