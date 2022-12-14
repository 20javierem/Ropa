package com.babas.views.dialogs;

import com.babas.models.*;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.babas.validators.ProgramValidator;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import jakarta.validation.ConstraintViolation;
import org.apache.xmlbeans.impl.common.NameUtil;

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

public class DaddProductToSale extends JDialog {
    private JPanel contentPane;
    private FlatTextField txtSearchProduct;
    private FlatTable table;
    private JLabel lblProduct;
    private JButton btnHecho;
    private FlatSpinner spinerQuantity;
    private JButton btnAddProduct;
    private JComboBox cbbPresentation;
    private JComboBox cbbPrice;
    private Sale sale;
    private StockProductAbstractModel model;
    private int pX, pY;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<StockProductAbstractModel> modeloOrdenado;
    private List<RowFilter<StockProductAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Product product;

    public DaddProductToSale(Sale sale) {
        super(Utilities.getJFrame(), true);
        this.sale = sale;
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
        txtSearchProduct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
        });
        ((JButton) txtSearchProduct.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchProduct.setText(null);
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
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        cbbPresentation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPrices();
            }
        });
    }

    private void loadPrices() {
        Presentation presentation = (Presentation) cbbPresentation.getSelectedItem();
        cbbPrice.removeAllItems();
        presentation.getPrices().forEach(price -> cbbPrice.addItem(price.getPrice()));
        cbbPrice.setSelectedItem(presentation.getPriceDefault().getPrice());
    }

    private void init() {
        setUndecorated(true);
        setContentPane(contentPane);
        loadTables();
        pack();
        lblProduct.setText(null);
        setLocationRelativeTo(getOwner());
    }

    public void filtrar() {
        String busqueda = txtSearchProduct.getText().trim();
        filtros.clear();
        filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }

    private void loadTables() {
        model = new StockProductAbstractModel(sale.getBranch().getStocks());
        table.setModel(model);
        StockCellRendered.setCellRenderer(table, listaFiltros);
        UtilitiesTables.headerNegrita(table);
        table.removeColumn(table.getColumn("TOTAL"));
        table.removeColumn(table.getColumn("SUCURSAL"));
        table.removeColumn(table.getColumn(""));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void loadProduct() {
        if (table.getSelectedRow() != -1) {
            product = model.getList().get(table.convertRowIndexToModel(table.getSelectedRow())).getProduct();
        }
        if (product != null) {
            lblProduct.setText(product.getStyle().getName() + ", " + product.getSex().getName() + ", " + product.getSize().getName() + ", " + product.getColor().getName());
            cbbPresentation.setModel(new DefaultComboBoxModel(new Vector(product.getPresentations())));
            cbbPresentation.setRenderer(new Presentation.ListCellRenderer());
            cbbPresentation.setSelectedItem(product.getPresentationDefault());
            loadPrices();
        } else {
            lblProduct.setText(null);
            cbbPresentation.setModel(new DefaultComboBoxModel());
            cbbPresentation.setRenderer(new DefaultListCellRenderer());
            cbbPrice.removeAllItems();
        }
    }

    private void addProduct() {
        DetailSale detailSale = new DetailSale();
        detailSale.setSale(sale);
        detailSale.setProduct(product);
        detailSale.setPresentation((Presentation) cbbPresentation.getSelectedItem());
        detailSale.setQuantity((Integer) spinerQuantity.getValue());
        if (!((JTextField) cbbPrice.getEditor().getEditorComponent()).getText().isEmpty()) {
            detailSale.setPrice(Double.valueOf(((JTextField) cbbPrice.getEditor().getEditorComponent()).getText()));
        } else {
            if (detailSale.getPresentation() != null) {
                detailSale.setPrice(detailSale.getPresentation().getPriceDefault().getPrice());
            } else {
                detailSale.setPrice(0.0);
            }
        }
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(detailSale);
        if (constraintViolationSet.isEmpty()) {
            searchProduct(detailSale);
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void searchProduct(DetailSale detailSale) {
        boolean entro = false;
        for (DetailSale detailSale1 : sale.getDetailSales()) {
            if (Objects.equals(detailSale1.getProduct().getId(), detailSale.getProduct().getId()) && Objects.equals(detailSale.getPresentation().getId(), detailSale1.getProduct().getId())) {
                detailSale1.setQuantity(detailSale1.getQuantity() + detailSale.getQuantity());
                entro = true;
                break;
            }
        }
        if (!entro) {
            sale.getDetailSales().add(detailSale);
        }
        Utilities.getTabbedPane().updateTab();
        table.clearSelection();
        product = null;
        loadProduct();
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
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
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
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
        panel4.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Cantidad:");
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        panel4.add(spinerQuantity, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Presentaci??n:");
        panel4.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbPresentation = new JComboBox();
        panel4.add(cbbPresentation, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Precio:");
        panel4.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbPrice = new JComboBox();
        cbbPrice.setEditable(true);
        panel4.add(cbbPrice, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAddProduct = new JButton();
        btnAddProduct.setText("Agregar");
        panel4.add(btnAddProduct, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel4.add(btnHecho, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
