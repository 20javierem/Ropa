package com.babas.views.tabs;

import com.babas.App;
import com.babas.controllers.Clients;
import com.babas.custom.TabPane;
import com.babas.models.Client;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorDetailSale2;
import com.babas.utilitiesTables.tablesCellRendered.DetailSaleCellRendered;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.dialogs.DaddProductToSale;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.FlatIconColors;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Set;

public class TabNewSale {
    private TabPane tabPane;
    private JButton btnAddProducts;
    private JLabel lblSubTotal;
    private JLabel lblTotal;
    private JLabel lblLogo;
    private FlatTextField txtDocument;
    private FlatTextField txtNameClient;
    private FlatTextField txtPhone;
    private FlatTextField txtMail;
    private FlatTable table;
    private JButton btnSaleWithCash;
    private JButton btnSaleWithTrasnfer;
    private FlatSpinner spinnerDiscount;
    private JLabel lblDiscount;
    private JLabel lblReserve;
    private Sale sale;
    private DetailSaleAbstractModel model;

    public TabNewSale() {
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
        btnSaleWithCash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(true);
            }
        });
        btnSaleWithTrasnfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave(false);
            }
        });
        ((JSpinner.NumberEditor) spinnerDiscount.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField textField = (JTextField) e.getSource();
                        textField.selectAll();
                    }
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        sale.setDiscount((Double) spinnerDiscount.getValue());
                        loadTotals();
                    }
                });
            }
        });
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
            sale.setBranch(Babas.boxSession.getBox().getBranch());
        } else {
            sale.setBranch(null);
        }
        if (sale.getBranch() != null) {
            DaddProductToSale daddProductToSale = new DaddProductToSale(sale);
            daddProductToSale.setVisible(true);
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Debe abrir caja para comenzar");
        }
    }

    private void init() {
        tabPane.setTitle("Nueva venta");
        sale = new Sale();
        loadTable();
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                loadTotals();
            }
        });
        if (!Babas.company.getLogo().isBlank()) {
            if (Utilities.iconCompany != null) {
                lblLogo.setIcon(Utilities.iconCompany);
            }
        }
    }

    private void loadTotals() {
        if (Babas.boxSession.getId() == null) {
            sale.getDetailSales().clear();
            sale.setBranch(null);
        }
        sale.calculateTotal();
        lblSubTotal.setText(Utilities.moneda.format(sale.getTotal()));
        lblDiscount.setText(Utilities.moneda.format(sale.getDiscount()));
        lblTotal.setText(Utilities.moneda.format(sale.getTotalCurrent()));
        Utilities.getLblCentro().setText("Nueva venta");
    }

    private void loadTable() {
        model = new DetailSaleAbstractModel(sale.getDetailSales());
        table.setModel(model);
        DetailSaleCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorDetailSale2());
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorDetailSale());
        table.getColumnModel().getColumn(model.getColumnCount() - 4).setCellEditor(new JButtonEditorDetailSale());
    }

    private void onSave(boolean isCash) {
        if (Babas.boxSession.getId() != null) {
            if (getClient()) {
                sale.setBranch(Babas.boxSession.getBox().getBranch());
                sale.setCash(isCash);
                sale.setBoxSession(Babas.boxSession);
                sale.setUser(Babas.user);
                Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(sale);
                if (constraintViolationSet.isEmpty()) {
                    boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Comfirmar Venta", JOptionPane.YES_NO_OPTION) == 0;
                    if (si) {
                        sale.save();
                        Babas.boxSession.getSales().add(0, sale);
                        Babas.boxSession.calculateTotals();
                        Utilities.getLblIzquierda().setText("Venta registrada Nro. " + sale.getNumberSale() + " :" + Utilities.formatoFechaHora.format(sale.getCreated()));
                        Utilities.getLblDerecha().setText("Monto caja: " + Utilities.moneda.format(Babas.boxSession.getAmountToDelivered()));
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Venta registrada");
                        if (Utilities.propiedades.getPrintTicketSale().equals("always")) {
                            UtilitiesReports.generateTicketSale(sale, true);
                        } else if (Utilities.propiedades.getPrintTicketSale().equals("question")) {
                            si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Imprimir?", "Ticket de venta", JOptionPane.YES_NO_OPTION) == 0;
                            if (si) {
                                UtilitiesReports.generateTicketSale(sale, true);
                            }
                        }
                        sale = new Sale();
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
        loadTable();
        loadTotals();
    }

    private boolean getClient() {
        if (!txtDocument.getText().isBlank() && !txtNameClient.getText().isBlank()) {
            Client client = Clients.getByDNI(txtDocument.getText().trim());
            if (client == null) {
                client = new Client();
                client.setDni(txtDocument.getText().trim());
            }
            client.setNames(txtNameClient.getText().trim());
            client.setMail(txtMail.getText().trim());
            client.setPhone(txtPhone.getText().trim());
            client.save();
            sale.setClient(client);
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
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 0), 10, -1));
        tabPane.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), 5, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txtDocument = new FlatTextField();
        txtDocument.setPlaceholderText("DNI...");
        txtDocument.setText("");
        panel5.add(txtDocument, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(95, -1), null, 0, false));
        txtNameClient = new FlatTextField();
        txtNameClient.setPlaceholderText("Cliente...");
        panel5.add(txtNameClient, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, -1), null, 0, false));
        txtPhone = new FlatTextField();
        txtPhone.setPlaceholderText("Celular...");
        panel5.add(txtPhone, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(120, -1), null, 0, false));
        txtMail = new FlatTextField();
        txtMail.setPlaceholderText("Correo...");
        panel5.add(txtMail, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Descuento:");
        panel5.add(label1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel5.add(spinnerDiscount, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel6, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(5, 3, new Insets(0, 20, 0, 20), 5, -1));
        panel7.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Total:");
        panel8.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSubTotal = new JLabel();
        Font lblSubTotalFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblSubTotal.getFont());
        if (lblSubTotalFont != null) lblSubTotal.setFont(lblSubTotalFont);
        lblSubTotal.setText("S/0.00");
        panel8.add(lblSubTotal, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Total Venta:");
        panel8.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(0, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblTotal = new JLabel();
        Font lblTotalFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblTotal.getFont());
        if (lblTotalFont != null) lblTotal.setFont(lblTotalFont);
        lblTotal.setText("S/0.00");
        panel8.add(lblTotal, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel8.add(separator1, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Descuento:");
        panel8.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDiscount = new JLabel();
        Font lblDiscountFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblDiscount.getFont());
        if (lblDiscountFont != null) lblDiscount.setFont(lblDiscountFont);
        lblDiscount.setText("S/0.00");
        panel8.add(lblDiscount, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Adelanto(Reserva):");
        panel8.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblReserve = new JLabel();
        Font lblReserveFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblReserve.getFont());
        if (lblReserveFont != null) lblReserve.setFont(lblReserveFont);
        lblReserve.setText("S/0.00");
        panel8.add(lblReserve, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel7.add(spacer5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnSaleWithTrasnfer = new JButton();
        Font btnSaleWithTrasnferFont = this.$$$getFont$$$(null, -1, 14, btnSaleWithTrasnfer.getFont());
        if (btnSaleWithTrasnferFont != null) btnSaleWithTrasnfer.setFont(btnSaleWithTrasnferFont);
        btnSaleWithTrasnfer.setText("Cancelar con transferencia");
        panel9.add(btnSaleWithTrasnfer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSaleWithCash = new JButton();
        Font btnSaleWithCashFont = this.$$$getFont$$$(null, -1, 14, btnSaleWithCash.getFont());
        if (btnSaleWithCashFont != null) btnSaleWithCash.setFont(btnSaleWithCashFont);
        btnSaleWithCash.setText("Cancelar con efectivo");
        panel9.add(btnSaleWithCash, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        panel7.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(255, 220), new Dimension(255, 220), new Dimension(255, 220), 0, false));
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
