package com.babas.views.tabs;

import com.babas.controllers.Clients;
import com.babas.controllers.Rentals;
import com.babas.controllers.Sales;
import com.babas.custom.TabPane;
import com.babas.custom.Task;
import com.babas.models.Client;
import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.modelsFacture.ApiClient;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.DetailRentalCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class TabFinishRental {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private JDateChooser jDateFinish;
    private FlatSpinner spinnerPenalty;
    private JLabel lblSubTotal;
    private JLabel lblDiscount;
    private JLabel lblWarranty;
    private JLabel lblTotalWithDiscount;
    private JLabel lblTotalCurrent;
    private JButton btnFinishRental;
    private JLabel lblLogo;
    private JLabel lblTodelivery;
    private JTextArea txtObservation;
    private JLabel lblReserve;
    private JLabel lblWarranty2;
    private JLabel lblSubtotal2;
    private Rental rental;
    private DetailRentalAbstractModel model;

    public TabFinishRental(Rental rental) {
        this.rental = rental;
        $$$setupUI$$$();
        init();
        ((JSpinner.NumberEditor) spinnerPenalty.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
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
                    rental.setPenalty((Double) spinnerPenalty.getValue());
                    loadTotals();
                });
            }
        });
        btnFinishRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
    }

    private void init() {
        tabPane.setTitle("Finalizaci??n alquiler Nro. " + rental.getId());
        if (Babas.company.getLogo() != null) {
            if (Utilities.iconCompanyx255x220 != null) {
                lblLogo.setIcon(Utilities.iconCompanyx255x220);
            }
        }
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                loadTotals();
            }
        });
        load();
        loadTotals();
    }

    private void onSave() {
        rental.setClient(getClient());
        if (Babas.boxSession.getId() != null) {
            if (jDateFinish.getDate() != null) {
                boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "??Est?? seguro?", "Confirmar Entrega", JOptionPane.YES_NO_OPTION) == 0;
                if (si) {
                    rental.refresh();
                    if (rental.isActive() == 0) {
                        rental.setTypeVoucher("77");
                        JPanel jPanel = new JPanel();
                        jPanel.add(new JLabel("Seleccione el tipo de comprobante: "));
                        JComboBox comboBox = new JComboBox();
                        comboBox.addItem("NOTA");
                        Babas.company.refresh();
                        if (Babas.company.isValidToken()) {
                            comboBox.addItem("BOLETA");
                            comboBox.addItem("FACTURA");
                        }
                        jPanel.add(comboBox);
                        int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), jPanel, "Confirmar Entrega", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Confirmar", "Cancelar"}, "Confirmar");
                        if (option == JOptionPane.OK_OPTION) {
                            if (comboBox.getSelectedIndex() != 0) {
                                rental.setTypeVoucher(comboBox.getSelectedIndex() == 1 ? "03" : "01");
                            }
                            if (rental.isValidClient()) {
                                rental.setPenalty((Double) spinnerPenalty.getValue());
                                rental.calculateTotals();
                                rental.setDelivery(jDateFinish.getDate());
                                rental.setActive(1);
                                rental.setObservation(txtObservation.getText().trim());
                                rental.setNumbersVoucher();
                                rental.updateStocks();
                                rental.save();
                                if (Sales.getOnWait().isEmpty() && Rentals.getOnWait().isEmpty()) {
                                    loadProgressBar();
                                } else {
                                    Toolkit.getDefaultToolkit().beep();
                                    endRental();
                                }
                            } else {
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El cliente no es v??lido");
                            }
                        }
                    } else if (rental.isActive() == 1) {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El alquiler ya est?? finalizado");
                        FPrincipal.rentalsActives.remove(rental);
                        btnFinishRental.setVisible(false);
                        jDateFinish.setEnabled(false);
                        spinnerPenalty.setEnabled(false);
                        txtObservation.setEnabled(false);
                        txtNameClient.setEnabled(false);
                        txtDocument.setEnabled(false);
                        txtMail.setEnabled(false);
                        txtPhone.setEnabled(false);
                    } else {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "El alquiler est?? cancelado");
                        FPrincipal.rentalsActives.remove(rental);
                        btnFinishRental.setVisible(false);
                        jDateFinish.setEnabled(false);
                        spinnerPenalty.setEnabled(false);
                        txtObservation.setEnabled(false);
                        txtNameClient.setEnabled(false);
                        txtDocument.setEnabled(false);
                        txtMail.setEnabled(false);
                        txtPhone.setEnabled(false);
                    }
                }
            } else {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe introducir fecha de entrega");
            }
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe aperturar caja");
        }
    }

    private void loadProgressBar() {
        Task task = new Task() {
            @Override
            protected Void doInBackground() {
                tabPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Utilities.getLblIzquierda().setVisible(false);
                Utilities.getProgressBar().setVisible(true);
                Thread thread = new Thread(() -> {
                    try {
                        for (int i = 0; i <= 100; i++) {
                            Utilities.getProgressBar().setValue(i);
                            sleep(150);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                rental.setStatusSunat(ApiClient.sendComprobante(ApiClient.getComprobanteOfRental(rental), true));
                tabPane.setCursor(null);
                Utilities.getProgressBar().setValue(100);
                Toolkit.getDefaultToolkit().beep();
                endRental();
                Utilities.getLblIzquierda().setVisible(true);
                Utilities.getProgressBar().setVisible(false);
                return null;
            }
        };
        task.execute();
    }

    private void endRental() {
        Movement movement = new Movement();
        movement.setEntrance(rental.getPenalty() > rental.getWarranty());
        movement.setAmount(rental.getPenalty() - rental.getWarranty());
        movement.setBoxSesion(Babas.boxSession);
        movement.setDescription("ALQUILER FINALIZADO: " + rental.getNumberRental());
        movement.save();
        movement.getBoxSesion().getMovements().add(0, movement);
        movement.getBoxSesion().calculateTotals();
        Utilities.getLblIzquierda().setText("Alquiler finalizado: " + rental.getSerie() + "-" + rental.getCorrelativo() + " :" + Utilities.formatoFechaHora.format(rental.getUpdated()));
        Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "??XITO", "Alquiler finalizado");
        FPrincipal.rentalsActives.remove(rental);
        btnFinishRental.setVisible(false);
        jDateFinish.setEnabled(false);
        spinnerPenalty.setEnabled(false);
        txtObservation.setEnabled(false);
        txtNameClient.setEnabled(false);
        txtDocument.setEnabled(false);
        txtMail.setEnabled(false);
        txtPhone.setEnabled(false);
        rental.save();
        if (Utilities.propiedades.getPrintTicketRentalFinish().equals("always")) {
            int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
            if (index == 0) {
                UtilitiesReports.generateComprobanteOfRental(true, rental, true);
            } else if (index == 1) {
                UtilitiesReports.generateComprobanteOfRental(false, rental, true);
            }
        } else if (Utilities.propiedades.getPrintTicketRentalFinish().equals("question")) {
            boolean option = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "??Imprimir?", "Ticket de alquiler", JOptionPane.YES_NO_OPTION) == 0;
            if (option) {
                int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                if (index == 0) {
                    UtilitiesReports.generateComprobanteOfRental(true, rental, true);
                } else if (index == 1) {
                    UtilitiesReports.generateComprobanteOfRental(false, rental, true);
                }
            }
        }
    }

    private Client getClient() {
        Client client = null;
        if (!txtDocument.getText().isBlank() && !txtNameClient.getText().isBlank()) {
            client = Clients.getByDNI(txtDocument.getText().trim(), false);
            if (client == null) {
                client = new Client();
                client.setDni(txtDocument.getText().trim());
                FPrincipal.clients.add(client);
            }
            client.setNames(txtNameClient.getText().trim());
            client.setAddres(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
        }
        return client;
    }

    private void load() {
        model = new DetailRentalAbstractModel(rental.getDetailRentals());
        table.setModel(model);
        DetailRentalCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        txtDocument.setText(rental.getClient().getDni());
        txtNameClient.setText(rental.getClient().getNames());
        txtPhone.setText(rental.getClient().getPhone());
        txtMail.setText(rental.getClient().getAddres());
        txtObservation.setText(rental.getObservation());
        table.removeColumn(table.getColumn(""));
        if (rental.isActive() != 0) {
            spinnerPenalty.setValue(rental.getPenalty());
            jDateFinish.setDate(rental.getDelivery());
            btnFinishRental.setVisible(false);
            jDateFinish.setEnabled(false);
            spinnerPenalty.setEnabled(false);
        }
    }

    private void loadTotals() {
        rental.calculateTotals();

        lblSubTotal.setText(Utilities.moneda.format(rental.getTotal()));
        lblDiscount.setText(Utilities.moneda.format(rental.getDiscount()));
        lblTotalWithDiscount.setText(Utilities.moneda.format(rental.getTotalWithDiscount()));

        if (rental.getReserve() == null) {
            lblReserve.setText(Utilities.moneda.format(0.0));
        } else {
            lblReserve.setText(Utilities.moneda.format(rental.getReserve().getAdvance()));
        }

        lblSubtotal2.setText(Utilities.moneda.format(rental.getTotalCurrent() - rental.getWarranty()));
        lblWarranty.setText(Utilities.moneda.format(rental.getWarranty()));

        lblTotalCurrent.setText(Utilities.moneda.format(rental.getTotalCurrent()));
        lblWarranty2.setText(Utilities.moneda.format(rental.getWarranty()));
        lblTodelivery.setText(Utilities.moneda.format(rental.getWarranty() - rental.getPenalty()));

        Utilities.getLblCentro().setText("Finalizar alquiler");
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerPenalty = new FlatSpinner();
        spinnerPenalty.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerPenalty.setEditor(Utilities.getEditorPrice(spinnerPenalty));
        jDateFinish = new JDateChooser(new Date());
        jDateFinish.setDateFormatString(Utilities.getFormatoFecha());
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane = new TabPane();
        tabPane.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 30, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        table.setEnabled(false);
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 10, -1));
        tabPane.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), 5, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txtDocument = new FlatTextField();
        txtDocument.setPlaceholderText("DNI...");
        txtDocument.setText("");
        panel4.add(txtDocument, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(120, -1), null, 0, false));
        txtNameClient = new FlatTextField();
        txtNameClient.setPlaceholderText("Cliente...");
        panel4.add(txtNameClient, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, -1), null, 0, false));
        txtPhone = new FlatTextField();
        txtPhone.setPlaceholderText("Celular...");
        panel4.add(txtPhone, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(120, -1), null, 0, false));
        txtMail = new FlatTextField();
        txtMail.setPlaceholderText("Direcci??n...");
        panel4.add(txtMail, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Fecha de entrega:");
        panel4.add(label1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.add(jDateFinish, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 1, new Insets(20, 20, 20, 20), 5, 20));
        panel6.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, 0));
        panel7.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Total Alquiler:");
        panel8.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel8.add(spacer2, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblSubTotal = new JLabel();
        Font lblSubTotalFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblSubTotal.getFont());
        if (lblSubTotalFont != null) lblSubTotal.setFont(lblSubTotalFont);
        lblSubTotal.setText("S/0.00");
        panel8.add(lblSubTotal, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel8.add(separator1, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Subtotal:");
        panel8.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalWithDiscount = new JLabel();
        Font lblTotalWithDiscountFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblTotalWithDiscount.getFont());
        if (lblTotalWithDiscountFont != null) lblTotalWithDiscount.setFont(lblTotalWithDiscountFont);
        lblTotalWithDiscount.setText("S/0.00");
        panel8.add(lblTotalWithDiscount, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        panel8.add(separator2, new GridConstraints(8, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(3, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Adelanto(Reserva):");
        panel8.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblReserve = new JLabel();
        Font lblReserveFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblReserve.getFont());
        if (lblReserveFont != null) lblReserve.setFont(lblReserveFont);
        lblReserve.setText("S/0.00");
        panel8.add(lblReserve, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Descuento:");
        panel8.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDiscount = new JLabel();
        Font lblDiscountFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblDiscount.getFont());
        if (lblDiscountFont != null) lblDiscount.setFont(lblDiscountFont);
        lblDiscount.setText("S/0.00");
        panel8.add(lblDiscount, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblWarranty = new JLabel();
        Font lblWarrantyFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblWarranty.getFont());
        if (lblWarrantyFont != null) lblWarranty.setFont(lblWarrantyFont);
        lblWarranty.setText("S/0.00");
        panel8.add(lblWarranty, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 14, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Total:");
        panel8.add(label6, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalCurrent = new JLabel();
        Font lblTotalCurrentFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblTotalCurrent.getFont());
        if (lblTotalCurrentFont != null) lblTotalCurrent.setFont(lblTotalCurrentFont);
        lblTotalCurrent.setText("S/0.00");
        panel8.add(lblTotalCurrent, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel8.add(spacer4, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, 14, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Garant??a:");
        panel8.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        panel8.add(separator3, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, 14, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Resumen:");
        panel8.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSubtotal2 = new JLabel();
        Font lblSubtotal2Font = this.$$$getFont$$$(null, Font.BOLD, 14, lblSubtotal2.getFont());
        if (lblSubtotal2Font != null) lblSubtotal2.setFont(lblSubtotal2Font);
        lblSubtotal2.setText("S/0.00");
        panel8.add(lblSubtotal2, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel8.add(spacer5, new GridConstraints(6, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, 0));
        panel7.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, 14, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Total Dev.:");
        panel9.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTodelivery = new JLabel();
        Font lblTodeliveryFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblTodelivery.getFont());
        if (lblTodeliveryFont != null) lblTodelivery.setFont(lblTodeliveryFont);
        lblTodelivery.setText("S/0.00");
        panel9.add(lblTodelivery, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel9.add(spacer6, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JSeparator separator4 = new JSeparator();
        panel9.add(separator4, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 14, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Multa:");
        panel9.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Font spinnerPenaltyFont = this.$$$getFont$$$(null, -1, 14, spinnerPenalty.getFont());
        if (spinnerPenaltyFont != null) spinnerPenalty.setFont(spinnerPenaltyFont);
        panel9.add(spinnerPenalty, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 14, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Garant??a:");
        panel9.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblWarranty2 = new JLabel();
        Font lblWarranty2Font = this.$$$getFont$$$(null, Font.BOLD, 14, lblWarranty2.getFont());
        if (lblWarranty2Font != null) lblWarranty2.setFont(lblWarranty2Font);
        lblWarranty2.setText("S/0.00");
        panel9.add(lblWarranty2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel9.add(spacer7, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel6.add(spacer8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel6.add(spacer9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel10, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnFinishRental = new JButton();
        Font btnFinishRentalFont = this.$$$getFont$$$(null, -1, 14, btnFinishRental.getFont());
        if (btnFinishRentalFont != null) btnFinishRental.setFont(btnFinishRentalFont);
        btnFinishRental.setText("Finalizar alquiler");
        panel10.add(btnFinishRental, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        panel6.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 220), new Dimension(255, 220), new Dimension(255, 220), 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        txtObservation = new JTextArea();
        txtObservation.setLineWrap(true);
        txtObservation.setWrapStyleWord(true);
        scrollPane2.setViewportView(txtObservation);
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, 14, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Observaci??n:");
        panel6.add(label12, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

}
