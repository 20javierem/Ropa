package com.babas.views.tabs;

import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Quotation;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailQuotation;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailQuotation2;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailQuotationCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.DetailSaleCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailQuotationAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DAddProductToTransfer;
import com.babas.views.dialogs.DaddProductToSale;
import com.babas.views.dialogs.DaddProductsToQuotation;
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

public class TabNewQuotation {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private FlatTable table;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private FlatSpinner spinnerDiscount;
    private JButton btnConfirm;
    private JTextArea txtObservation;
    private JLabel lblLogo;
    private JDateChooser jDateFinish;
    private Quotation quotation;
    private DetailQuotationAbstractModel model;

    public TabNewQuotation(Quotation quotation) {
        this.quotation = quotation;
        $$$setupUI$$$();
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
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
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
                    TabNewQuotation.this.quotation.setDiscount((Double) spinnerDiscount.getValue());
                    loadTotals();
                });
            }
        });
    }

    private void searchClient() {
        Client client = Clients.getByDNI(txtDocument.getText().trim());
        if (client != null) {
            txtNameClient.setText(client.getNames());
            txtPhone.setText(client.getPhone());
            txtMail.setText(client.getAddres());
        }
    }

    private void loadAddProducts() {
        if (Babas.boxSession.getId() != null) {
            quotation.setBranch(Babas.boxSession.getBox().getBranch());
        } else {
            quotation.setBranch(null);
        }
        if (quotation.getBranch() != null) {
            DaddProductsToQuotation dAddProductToTransfer = new DaddProductsToQuotation(quotation);
            dAddProductToTransfer.setVisible(true);
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe abrir caja para comenzar");
        }

    }

    private void init() {
        tabPane.setTitle("Nueva cotización");
        load();
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                loadTotals();
            }
        });
        if (Babas.company.getLogo() != null) {
            if (Utilities.iconCompanyx255x220 != null) {
                lblLogo.setIcon(Utilities.iconCompanyx255x220);
            }
        }
    }

    public Quotation getQuotation() {
        return quotation;
    }

    private void loadTotals() {
        if (Babas.boxSession.getId() == null) {
            quotation.getDetailQuotations().clear();
            quotation.setBranch(null);
        }
        quotation.calculateTotals();
        lblSubTotal.setText(Utilities.moneda.format(quotation.getTotal()));
        spinnerDiscount.setValue(quotation.getDiscount());
        lblTotal.setText(Utilities.moneda.format(quotation.getTotalCurrent()));
        Utilities.getLblCentro().setText("Nueva Cotización");
    }

    private void load() {
        model = new DetailQuotationAbstractModel(quotation.getDetailQuotations());
        table.setModel(model);
        DetailQuotationCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailQuotation2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailQuotation());
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailQuotation());
        txtObservation.setText(quotation.getObservation());
        loadTotals();
    }

    private void onSave() {
        if (Babas.boxSession.getId() != null) {
            if (getClient()) {
                quotation.setBranch(Babas.boxSession.getBox().getBranch());
                quotation.setUser(Babas.user);
                quotation.setObservation(txtObservation.getText().trim());
                quotation.setEnded(jDateFinish.getDate());
                Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(quotation);
                if (constraintViolationSet.isEmpty()) {
                    boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Comfirmar Cotización", JOptionPane.YES_NO_OPTION) == 0;
                    if (si) {
                        quotation.save();
                        Utilities.getLblIzquierda().setText("Cotización registrada Nro. " + quotation.getId() + " : " + Utilities.formatoFechaHora.format(quotation.getCreated()));
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Cotización registrada");
                        si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Imprimir?", "Ticket de Cotización", JOptionPane.YES_NO_OPTION) == 0;
                        if (si) {
                            int index = JOptionPane.showOptionDialog(Utilities.getJFrame(), "Seleccione el formato a ver", "Ver ticket", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"A4", "Ticket", "Cancelar"}, "A4");
                            if (index == 0) {
                                UtilitiesReports.generateTicketQuotation(true, quotation, true);
                            } else if (index == 1) {
                                UtilitiesReports.generateTicketQuotation(false, quotation, true);
                            }
                        }
                        quotation = new Quotation();
                        clear();
                        Utilities.getTabbedPane().updateTab();
                    }
                } else {
                    ProgramValidator.mostrarErrores(constraintViolationSet);
                }
            } else {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe introducir NOMBRE y DNI");
            }
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe aperturar caja");
        }
    }

    private void clear() {
        txtMail.setText(null);
        txtDocument.setText(null);
        txtPhone.setText(null);
        txtNameClient.setText(null);
        txtObservation.setText(null);
        load();
    }

    private boolean getClient() {
        if (!txtDocument.getText().isBlank() && !txtNameClient.getText().isBlank()) {
            Client client = Clients.getByDNI(txtDocument.getText().trim());
            if (client == null) {
                client = new Client();
                client.setDni(txtDocument.getText().trim());
                FPrincipal.clients.add(client);
            }
            client.setNames(txtNameClient.getText().trim());
            client.setAddres(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
            quotation.setClient(client);
            return true;
        }
        return txtDocument.getText().trim().isBlank() && txtNameClient.getText().isBlank();
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerDiscount = new FlatSpinner();
        spinnerDiscount.setModel(new SpinnerNumberModel(0.00, 0.00, 100000.00, 0.50));
        spinnerDiscount.setEditor(Utilities.getEditorPrice(spinnerDiscount));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        jDateFinish = new JDateChooser(calendar.getTime());
        jDateFinish.setMinSelectableDate(calendar.getTime());
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
        tabPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddProducts = new JButton();
        btnAddProducts.setText("Añadir Productos");
        panel2.add(btnAddProducts, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 10, -1));
        tabPane.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), 5, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txtNameClient = new FlatTextField();
        txtNameClient.setPlaceholderText("Cliente...");
        panel5.add(txtNameClient, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, -1), null, 0, false));
        txtPhone = new FlatTextField();
        txtPhone.setPlaceholderText("Celular...");
        panel5.add(txtPhone, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(120, -1), null, 0, false));
        txtMail = new FlatTextField();
        txtMail.setPlaceholderText("Dirección...");
        panel5.add(txtMail, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Válido hasta:");
        panel5.add(label1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel5.add(jDateFinish, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        txtDocument = new FlatTextField();
        txtDocument.setPlaceholderText("DNI/RUC...");
        txtDocument.setText("");
        panel5.add(txtDocument, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(120, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel6, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(4, 3, new Insets(0, 20, 0, 20), 5, 0));
        panel7.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Total:");
        panel8.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSubTotal = new JLabel();
        Font lblSubTotalFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblSubTotal.getFont());
        if (lblSubTotalFont != null) lblSubTotal.setFont(lblSubTotalFont);
        lblSubTotal.setText("S/0.00");
        panel8.add(lblSubTotal, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(42, 22), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Total Venta:");
        panel8.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 22), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblTotal = new JLabel();
        Font lblTotalFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblTotal.getFont());
        if (lblTotalFont != null) lblTotal.setFont(lblTotalFont);
        lblTotal.setText("S/0.00");
        panel8.add(lblTotal, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel8.add(separator1, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Descuento:");
        panel8.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel8.add(spinnerDiscount, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel7.add(spacer5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnConfirm = new JButton();
        Font btnConfirmFont = this.$$$getFont$$$(null, -1, 14, btnConfirm.getFont());
        if (btnConfirmFont != null) btnConfirm.setFont(btnConfirmFont);
        btnConfirm.setText("Registrar");
        panel9.add(btnConfirm, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Opciones de pago:");
        panel9.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel9.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        txtObservation = new JTextArea();
        txtObservation.setLineWrap(true);
        txtObservation.setWrapStyleWord(true);
        scrollPane2.setViewportView(txtObservation);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 14, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Observación:");
        panel9.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        panel7.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 200), new Dimension(255, 200), new Dimension(255, 200), 0, false));
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