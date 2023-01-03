package com.babas.views.dialogs;

import com.babas.controllers.Branchs;
import com.babas.controllers.Users;
import com.babas.models.Branch;
import com.babas.models.Brand;
import com.babas.models.DetailQuotation;
import com.babas.models.User;
import com.babas.modelsFacture.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.DetailQuotationAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;

public class DCompany extends JDialog {
    private JPanel contentPane;
    private FlatButton btnHecho;
    private FlatButton btnSave;
    private FlatTextField txtBusinessName;
    private JTextArea txtDetails;
    private FlatTextField txtRuc;
    private FlatTextField txtTradeName;
    private FlatTextField txtFiscalAdress;
    private JLabel lblLogo;
    private JComboBox cbbUsers;
    private JComboBox cbbBranchs;
    private FlatSpinner spinnerUser;
    private FlatSpinner spinnerBranch;
    private JComboBox cbbCompany;
    private JComboBox cbbProduct;
    private FlatSpinner spinnerProduct;
    private FlatButton btnSaveIdProduct;
    private FlatButton btnSaveIdUser;
    private FlatButton btnSaveIdBranch;
    private FlatTextField txtWebSite;
    private JTabbedPane tabPane;
    private FlatButton btnSaveToken;
    private FlatTextField txtTokenFacture;
    private FlatTextField txtTokenDniRuc;
    private FlatButton btnSaveTokenDniRuc;

    public DCompany() {
        super(Utilities.getJFrame(), "Actualizar datos de la empresa", true);
        $$$setupUI$$$();
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
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadCrop();
            }
        });
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
        btnSaveIdProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sabeIdProduct();
            }
        });
        btnSaveIdBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveIdBranch();
            }
        });
        btnSaveIdUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveIdUser();
            }
        });
        btnSaveToken.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTokenFacture();
            }
        });
        cbbUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserId();
            }
        });
        cbbBranchs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBranchId();
            }
        });
        btnSaveTokenDniRuc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTokenDniRuc();
            }
        });
    }

    private void loadBranchId() {
        Branch branch = (Branch) cbbBranchs.getSelectedItem();
        if (branch != null) {
            spinnerBranch.setValue(branch.getIdFact());
        }
    }

    private void loadUserId() {
        User user = (User) cbbUsers.getSelectedItem();
        if (user != null) {
            spinnerUser.setValue(user.getIdFact());
        }
    }

    private void saveTokenFacture() {
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cambiar token", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            Babas.company.setToken(txtTokenFacture.getText().trim());
            Comprobante comprobante = new Comprobante();
            Contribuyente contribuyente = new Contribuyente();
            contribuyente.setToken_contribuyente(Babas.company.getToken());
            contribuyente.setId_usuario_vendedor(Users.get(1).getIdFact());
            comprobante.setContribuyente(contribuyente);
            Cabecera_comprobante cabecera_comprobante = new Cabecera_comprobante();
            cabecera_comprobante.setIdsucursal(Branchs.get(1).getIdFact());
            cabecera_comprobante.setTipo_documento("77");
            cabecera_comprobante.setFecha_comprobante(Utilities.formatoFecha.format(new Date()));
            cabecera_comprobante.setDescuento_monto(0.0);
            comprobante.setCabecera_comprobante(cabecera_comprobante);
            comprobante.setCliente(new Cliente());
            Detalle detalle = new Detalle();
            detalle.setCantidad(1);
            detalle.setCodigo("P001");
            detalle.setDescripcion("Producto de Ejemplo");
            detalle.setPrecio_venta(0.1);
            detalle.setIdproducto(Babas.company.getIdProduct());
            comprobante.getDetalle().add(detalle);
            if (ApiClient.sendComprobante(comprobante, false)) {
                Babas.company.setValidToken(true);
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "Éxito", "Token Registrado");
            } else {
                Babas.company.setValidToken(false);
            }
            Babas.company.save();
        }
    }

    private void saveIdUser() {
        User user = (User) cbbUsers.getSelectedItem();
        user.setIdFact(Integer.valueOf(String.valueOf(spinnerUser.getValue())));
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cambiar id", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            user.save();
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "Éxito", "Cambios guardados");
        }

    }

    private void saveIdBranch() {
        Branch branch = (Branch) cbbBranchs.getSelectedItem();
        branch.setIdFact(Integer.valueOf(String.valueOf(spinnerBranch.getValue())));
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cambiar id", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            branch.save();
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "Éxito", "Cambios guardados");
        }
    }

    private void saveTokenDniRuc() {
        Babas.company.setTokenConsults(txtTokenDniRuc.getText().trim());
        Babas.company.save();
        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "Éxito", "Cambios guardados");
    }

    private void sabeIdProduct() {
        Babas.company.setIdProduct(Integer.valueOf(String.valueOf(spinnerProduct.getValue())));
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cambiar id", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            Babas.company.save();
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "Éxito", "Cambios guardados");
        }
    }

    private void onSave() {
        Babas.company.setBusinessName(txtBusinessName.getText().trim());
        Babas.company.setDirectionPrincipal(txtFiscalAdress.getText().trim());
        Babas.company.setRuc(txtRuc.getText().trim());
        Babas.company.setTradeName(txtTradeName.getText().trim());
        Babas.company.setDetails(txtDetails.getText().trim());
        Babas.company.setWebSite(txtWebSite.getText().trim());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(Babas.company);
        if (constraintViolationSet.isEmpty()) {
            Babas.company.save();
            if (Babas.company.getId() == null) {
                if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Datos registrados");
                }
            } else {
                if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Datos actualizados");
                }
            }
            onHecho();
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void loadCrop() {
        DCrop dCrop = new DCrop("Cambiar Logo");
        dCrop.setVisible(true);
        BufferedImage bufferedImage = DCrop.imageSelectedx400;
        if (bufferedImage != null) {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", os);
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                if (Utilities.newImage(inputStream, "logoCompany.png", true)) {
                    if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ÉXITO", "Imagen guardada");
                    }
                    Babas.company.setLogo("logoCompany.png");
                    Image logo = Utilities.getImage("logoCompany.png", true);
                    if (logo != null) {
                        Utilities.iconCompanyx255x220 = new ImageIcon(DCrop.getImage(bufferedImage, 255.00, 200.00));
                        Utilities.iconCompanyx420x420 = new ImageIcon(DCrop.getImage(bufferedImage, 420.00, 420.00));
                        lblLogo.setIcon(Utilities.iconCompanyx420x420);

                    } else {
                        if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Ocurrió un error");
                        }
                    }
                } else {
                    if (Utilities.getJFrame() != null && Utilities.getJFrame() instanceof FPrincipal) {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Ocurrió un error");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if (Babas.company.getId() != null) {
            load();
        } else {
            tabPane.removeTabAt(tabPane.indexOfTab("Facturador"));
            btnSave.setText("Registrar");
            btnHecho.setText("Cancelar");
        }
        lblLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadBranchId();
        loadUserId();
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void load() {
        txtRuc.setText(Babas.company.getRuc());
        txtBusinessName.setText(Babas.company.getBusinessName());
        txtTradeName.setText(Babas.company.getTradeName());
        txtFiscalAdress.setText(Babas.company.getDirectionPrincipal());
        txtDetails.setText(Babas.company.getDetails());
        txtWebSite.setText(Babas.company.getWebSite());
        txtTokenFacture.setText(Babas.company.getToken());
        spinnerProduct.setValue(Babas.company.getIdProduct());
        if (Utilities.iconCompanyx420x420 != null) {
            lblLogo.setIcon(Utilities.iconCompanyx420x420);
        }
        cbbBranchs.setModel(new DefaultComboBoxModel(FPrincipal.branchs));
        cbbBranchs.setRenderer(new Branch.ListCellRenderer());
        cbbUsers.setModel(new DefaultComboBoxModel(FPrincipal.users));
        cbbUsers.setRenderer(new User.ListCellRenderer());
        cbbProduct.addItem("PRODUCTO");
        txtTokenDniRuc.setText(Babas.company.getTokenConsults());
    }

    private void onHecho() {
        if (Babas.company.getId() != null) {
            Babas.company.refresh();
        }
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerBranch = new FlatSpinner();
        spinnerBranch.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerBranch.setEditor(Utilities.getEditorPrice(spinnerBranch));
        spinnerProduct = new FlatSpinner();
        spinnerProduct.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerProduct.setEditor(Utilities.getEditorPrice(spinnerProduct));
        spinnerUser = new FlatSpinner();
        spinnerUser.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        spinnerUser.setEditor(Utilities.getEditorPrice(spinnerUser));
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, 10));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 10, 10), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new FlatButton();
        btnHecho.setText("Hecho");
        panel1.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new FlatButton();
        btnSave.setText("Guardar");
        panel1.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabPane = new JTabbedPane();
        contentPane.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.addTab("Compañia", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(8, 3, new Insets(10, 10, 10, 10), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("RUC:");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre de la empresa:");
        panel3.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Nombre comercial:");
        panel3.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Dirección principal:");
        panel3.add(label4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtBusinessName = new FlatTextField();
        txtBusinessName.setPlaceholderText("Obligatorio");
        panel3.add(txtBusinessName, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        txtTradeName = new FlatTextField();
        txtTradeName.setPlaceholderText("Obligatorio");
        panel3.add(txtTradeName, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        txtFiscalAdress = new FlatTextField();
        txtFiscalAdress.setPlaceholderText("Obligatorio");
        panel3.add(txtFiscalAdress, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 75), new Dimension(-1, 75), 0, false));
        txtDetails = new JTextArea();
        txtDetails.setLineWrap(true);
        txtDetails.setRows(2);
        txtDetails.setWrapStyleWord(true);
        scrollPane1.setViewportView(txtDetails);
        txtRuc = new FlatTextField();
        txtRuc.setPlaceholderText("Obligatorio");
        panel3.add(txtRuc, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(0);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/com/babas/images/lojoJmoreno (1).png")));
        lblLogo.setText("");
        lblLogo.setToolTipText("Cambiar logo de la empresa");
        panel4.add(lblLogo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(420, 420), new Dimension(420, 420), new Dimension(420, 420), 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Página Web:");
        panel3.add(label5, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtWebSite = new FlatTextField();
        panel3.add(txtWebSite, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Detalles");
        panel3.add(label6, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, 5));
        tabPane.addTab("Facturador", panel5);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Producto:");
        panel6.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbProduct = new JComboBox();
        panel6.add(cbbProduct, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Id facturador:");
        panel6.add(label8, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.add(spinnerProduct, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        btnSaveIdProduct = new FlatButton();
        btnSaveIdProduct.setText("Guardar");
        panel6.add(btnSaveIdProduct, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Sucursal:");
        panel6.add(label9, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBranchs = new JComboBox();
        panel6.add(cbbBranchs, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Id facturador:");
        panel6.add(label10, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.add(spinnerBranch, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        btnSaveIdBranch = new FlatButton();
        btnSaveIdBranch.setText("Guardar");
        panel6.add(btnSaveIdBranch, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Usuario:");
        panel6.add(label11, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbUsers = new JComboBox();
        panel6.add(cbbUsers, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Id facturador:");
        panel6.add(label12, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.add(spinnerUser, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        btnSaveIdUser = new FlatButton();
        btnSaveIdUser.setText("Guardar");
        panel6.add(btnSaveIdUser, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Token facturador:");
        panel7.add(label13, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTokenFacture = new FlatTextField();
        txtTokenFacture.setPlaceholderText("Obligatorio");
        panel7.add(txtTokenFacture, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        btnSaveToken = new FlatButton();
        btnSaveToken.setText("Guardar");
        panel7.add(btnSaveToken, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Token Dni/Ruc");
        panel7.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTokenDniRuc = new FlatTextField();
        txtTokenDniRuc.setPlaceholderText("Obligatorio");
        panel7.add(txtTokenDniRuc, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(210, -1), null, 0, false));
        btnSaveTokenDniRuc = new FlatButton();
        btnSaveTokenDniRuc.setText("Guardar");
        panel7.add(btnSaveTokenDniRuc, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
