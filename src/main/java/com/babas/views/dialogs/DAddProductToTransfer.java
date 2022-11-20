package com.babas.views.dialogs;

import com.babas.models.*;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DAddProductToTransfer extends JDialog {
    private JPanel contentPane;
    private FlatSpinner spinerQuantity;
    private JButton btnAddProduct;
    private JButton btnHecho;
    private FlatTable table;
    private FlatTextField txtSearchProduct;
    private JLabel lblProduct;
    private Branch branchSource;
    private ProductAbstractModel productAbstractModel;
    private StockProductAbstractModel stockProductAbstractModel;
    private Transfer transfer;
    private int pX, pY;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<ProductAbstractModel> modeloOrdenado1;
    private List<RowFilter<ProductAbstractModel, String>> filtros1 = new ArrayList<>();
    private TableRowSorter<StockProductAbstractModel> modeloOrdenado2;
    private List<RowFilter<StockProductAbstractModel, String>> filtros2 = new ArrayList<>();
    private RowFilter filtroand;
    private Product product;

    public DAddProductToTransfer(Transfer transfer, Branch branchSource) {
        super(Utilities.getJFrame(), true);
        this.transfer = transfer;
        this.branchSource = branchSource;
        $$$setupUI$$$();
        init();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
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
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        txtSearchProduct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                loadProduct();
            }
        });
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        ((JButton) txtSearchProduct.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchProduct.setText(null);
                filtrar();
            }
        });
    }

    private void addProduct() {
        DetailTransfer detailTransfer = new DetailTransfer();
        detailTransfer.setTransfer(transfer);
        detailTransfer.setProduct(product);
        detailTransfer.setQuantity((Integer) spinerQuantity.getValue());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(detailTransfer);
        if (constraintViolationSet.isEmpty()) {
            searchProduct(detailTransfer);
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void searchProduct(DetailTransfer detailTransfer) {
        boolean entro = false;
        for (DetailTransfer detailTransfer1 : transfer.getDetailTransfers()) {
            if (Objects.equals(detailTransfer1.getProduct().getId(), detailTransfer.getProduct().getId())) {
                detailTransfer1.setQuantity(detailTransfer1.getQuantity() + detailTransfer.getQuantity());
                entro = true;
                break;
            }
        }
        if (!entro) {
            transfer.getDetailTransfers().add(detailTransfer);
        }
        Utilities.getTabbedPane().updateTab();
        table.clearSelection();
        product = null;
        loadProduct();
    }

    private void loadProduct() {
        if (branchSource == null) {
            if (table.getSelectedRow() != -1) {
                product = productAbstractModel.getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            }
        } else {
            if (table.getSelectedRow() != -1) {
                product = stockProductAbstractModel.getList().get(table.convertRowIndexToModel(table.getSelectedRow())).getProduct();
            }
        }
        if (product != null) {
            lblProduct.setText(product.getStyle().getName() + ", " + product.getSex().getName() + ", " + product.getSize().getName() + ", " + product.getColor().getName());
        } else {
            lblProduct.setText(null);
        }
    }

    public void filtrar() {
        String busqueda;
        busqueda = txtSearchProduct.getText().trim();
        if (branchSource == null) {
            filtros1.clear();
            filtros1.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2, 6, 7));
            listaFiltros.put(0, busqueda);
            listaFiltros.put(1, busqueda);
            listaFiltros.put(2, busqueda);
            listaFiltros.put(3, busqueda);
            listaFiltros.put(4, busqueda);
            filtroand = RowFilter.andFilter(filtros1);
            modeloOrdenado1.setRowFilter(filtroand);
        } else {
            filtros2.clear();
            filtros2.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2, 6, 7));
            listaFiltros.put(0, busqueda);
            listaFiltros.put(1, busqueda);
            listaFiltros.put(2, busqueda);
            listaFiltros.put(3, busqueda);
            listaFiltros.put(4, busqueda);
            filtroand = RowFilter.andFilter(filtros2);
            modeloOrdenado2.setRowFilter(filtroand);
        }

    }

    private void init() {
        setContentPane(contentPane);
        setUndecorated(true);
        if (branchSource == null) {
            loadTable1();
        } else {
            loadTable2();
        }
        pack();
        lblProduct.setText(null);
        setLocationRelativeTo(getOwner());
    }

    private void loadTable1() {
        productAbstractModel = new ProductAbstractModel(FPrincipal.products);
        table.setModel(productAbstractModel);
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table, listaFiltros);
        table.removeColumn(table.getColumn("PRECIO"));
        table.removeColumn(table.getColumn("TOTAL-STOCK"));
        table.removeColumn(table.getColumn("CATEGORÍA"));
        table.removeColumn(table.getColumn("MARCA"));
        table.removeColumn(table.getColumn(""));
        table.removeColumn(table.getColumn(""));
        table.removeColumn(table.getColumn(""));
        modeloOrdenado1 = new TableRowSorter<>(productAbstractModel);
        table.setRowSorter(modeloOrdenado1);
    }

    private void loadTable2() {
        stockProductAbstractModel = new StockProductAbstractModel(branchSource.getStocks());
        table.setModel(stockProductAbstractModel);
        UtilitiesTables.headerNegrita(table);
        StockCellRendered.setCellRenderer(table, listaFiltros);
        modeloOrdenado2 = new TableRowSorter<>(stockProductAbstractModel);
        table.setRowSorter(modeloOrdenado2);
    }

    private void onHecho() {
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinerQuantity = new FlatSpinner();
        spinerQuantity.setModel(new SpinnerNumberModel(1, 1, 100000, 1));
        spinerQuantity.setEditor(Utilities.getEditorPrice(spinerQuantity));
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), 10, 10));
        contentPane.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 5, 5));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtSearchProduct = new FlatTextField();
        txtSearchProduct.setPlaceholderText("Producto...");
        txtSearchProduct.setShowClearButton(true);
        panel1.add(txtSearchProduct, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(500, -1), null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Producto:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblProduct = new JLabel();
        lblProduct.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        panel3.add(lblProduct, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel4.add(btnHecho, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Cantidad:");
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.add(spinerQuantity, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAddProduct = new JButton();
        btnAddProduct.setText("Agregar");
        panel4.add(btnAddProduct, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
