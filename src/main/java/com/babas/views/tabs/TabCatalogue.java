package com.babas.views.tabs;

import com.babas.App;
import com.babas.custom.TabPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.ModelProduct;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.WrapLayout;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.util.*;
import java.util.List;

public class TabCatalogue {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtSearch;
    private JButton btnClearFilters;
    private JComboBox cbbBrand;
    private JComboBox cbbSex;
    private JComboBox cbbCategory;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private JScrollPane scrollPane;
    private JButton btnPrevius;
    private JButton btnNext;
    private JPanel panelProducts;
    private JPanel pane;
    private JPanel pane2;
    private ProductAbstractModel model;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private List<RowFilter<ProductAbstractModel, String>> filtros = new ArrayList<>();
    private int position = 0;
    private List<Product> productsFilters;
    private Product product;
    private String search = "";

    public TabCatalogue() {
        init();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });

        cbbBrand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbSex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        cbbColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        ((JButton) txtSearch.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText(null);
                filter();
            }
        });
        btnClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
            }
        });
    }

    private void clearFilters() {
        txtSearch.setText(null);
        cbbSex.setSelectedIndex(0);
        cbbCategory.setSelectedIndex(0);
        cbbBrand.setSelectedIndex(0);
        cbbSize.setSelectedIndex(0);
        cbbColor.setSelectedIndex(0);
        filter();
    }

    private void init() {
        tabPane.setTitle("Catálogo");
        panelProducts.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
        scrollPane.getVerticalScrollBar().setUnitIncrement(35);
        loadCombos();
        loadTable();
        reloadCards();
        getTabPane().getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                filter();
            }
        });
        btnPrevius.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/arrowCollapse.svg")));
        btnNext.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/arrowExpand.svg")));
    }

    private void loadCombos() {
        cbbBrand.setModel(new DefaultComboBoxModel(FPrincipal.brandsWithAll));
        cbbBrand.setRenderer(new Brand.ListCellRenderer());
        cbbCategory.setModel(new DefaultComboBoxModel(FPrincipal.categoriesWithAll));
        cbbCategory.setRenderer(new Category.ListCellRenderer());
        cbbColor.setModel(new DefaultComboBoxModel(FPrincipal.colorsWithAll));
        cbbColor.setRenderer(new Color.ListCellRenderer());
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexsWithAll));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbSize.setModel(new DefaultComboBoxModel(FPrincipal.sizesWithAll));
        cbbSize.setRenderer(new Size.ListCellRenderer());
    }

    private void loadTableFilter() {
        productsFilters = new ArrayList<>(new ArrayList<>(FPrincipal.products));
        if (product.getStyle().getCategory() != null) {
            productsFilters.removeIf(product1 -> !product1.getStyle().getCategory().getId().equals(product.getStyle().getCategory().getId()));
        }
        if (product.getSize() != null) {
            productsFilters.removeIf(product1 -> !product1.getSize().getId().equals(product.getSize().getId()));
        }
        if (product.getColor() != null) {
            productsFilters.removeIf(product1 -> !product1.getColor().getId().equals(product.getColor().getId()));
        }
        if (product.getSex() != null) {
            productsFilters.removeIf(product1 -> !product1.getSex().getId().equals(product.getSex().getId()));
        }
        if (product.getBrand() != null) {
            productsFilters.removeIf(product1 -> !product1.getBrand().getId().equals(product.getBrand().getId()));
        }
        if (!search.isBlank()) {
            productsFilters.removeIf(product1 -> {
                if (product1.getBarcode().toString().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getStyle().getName().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getSex().getName().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getStyle().getCategory().getName().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getBrand().getName().toLowerCase().contains(search.toLowerCase()) ||
                        Utilities.moneda.format(product1.getPresentationDefault().getPriceDefault().getPrice()).toLowerCase().contains(search.toLowerCase()) ||
                        product1.getSize().getName().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getColor().getName().toLowerCase().contains(search.toLowerCase()) ||
                        product1.getStockTotal().toString().contains(search.toLowerCase())
                ) {
                    return false;
                } else {
                    return true;
                }
            });
        }
        reloadTable();
        reloadCards();
    }

    private void loadTable() {
        model = new ProductAbstractModel(new ArrayList<>());
        filter();
        table.setModel(model);
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.removeColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1));
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new JButtonEditorProduct("images"));
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table, listaFiltros);
    }

    private void reloadCards() {
        panelProducts.removeAll();
        for (Product product : model.getList()) {
            ModelProduct modelProduct = new ModelProduct(product);
            panelProducts.add(modelProduct.getContentPane());
        }
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
        panelProducts.repaint();
        panelProducts.revalidate();
    }

    private void reloadTable() {
        model.setList(getProductsRows());
        model.fireTableDataChanged();
    }

    private List<Product> getProductsRows() {
        if (productsFilters.size() > 102) {
            if (btnPrevius.getActionListeners().length > 0) {
                btnPrevius.removeActionListener(btnPrevius.getActionListeners()[0]);
            }
            if (btnNext.getActionListeners().length > 0) {
                btnNext.removeActionListener(btnNext.getActionListeners()[0]);
            }
            btnNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (position < productsFilters.size() - 1) {
                        if (position + 102 <= productsFilters.size() - 1) {
                            position = position + 102;
                        }
                    }
                    reloadTable();
                    reloadCards();
                }
            });
            btnPrevius.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (position > 0) {
                        position = position - 102;
                    }
                    reloadTable();
                    reloadCards();
                }
            });
            if ((position + 102) > productsFilters.size()) {
                return productsFilters.subList(position, productsFilters.size() - 1);
            } else {
                return productsFilters.subList(position, position + 102);
            }
        } else {
            position = 0;
            return productsFilters;
        }
    }

    private void filter() {
        product = new Product();
        product.setStyle(new Style());
        search = txtSearch.getText().trim();
        filtros.add(RowFilter.regexFilter("(?i)" + search, 0, 1, 2, 3, 4, 5, 6, 7, 8));
        listaFiltros.put(0, search);
        listaFiltros.put(1, search);
        listaFiltros.put(2, search);
        listaFiltros.put(3, search);
        listaFiltros.put(4, search);
        listaFiltros.put(5, search);
        listaFiltros.put(6, search);
        listaFiltros.put(7, search);
        listaFiltros.put(8, search);

        if (((Sex) cbbSex.getSelectedItem()).getId() != null) {
            Sex sex = (Sex) cbbSex.getSelectedItem();
            product.setSex(sex);
        }
        if (((Category) cbbCategory.getSelectedItem()).getId() != null) {
            Category category = (Category) cbbCategory.getSelectedItem();
            product.getStyle().setCategory(category);
        }
        if (((Brand) cbbBrand.getSelectedItem()).getId() != null) {
            Brand brand = (Brand) cbbBrand.getSelectedItem();
            product.setBrand(brand);
        }
        if (((Size) cbbSize.getSelectedItem()).getId() != null) {
            Size size = (Size) cbbSize.getSelectedItem();
            product.setSize(size);
        }
        if (((Color) cbbColor.getSelectedItem()).getId() != null) {
            Color color = (Color) cbbColor.getSelectedItem();
            product.setColor(color);
        }
        loadTableFilter();
        if (FPrincipal.products.size() == productsFilters.size()) {
            Utilities.getLblCentro().setText("Productos registrados: " + FPrincipal.products.size());
        } else {
            Utilities.getLblCentro().setText("Productos filtrados: " + productsFilters.size());
        }
    }

    public TabPane getTabPane() {
        return tabPane;
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane = new TabPane();
        tabPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        pane2 = new JPanel();
        pane2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 10, 0, 10), -1, -1));
        panel2.add(pane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnPrevius = new JButton();
        btnPrevius.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/previous.png")));
        btnPrevius.setText("");
        pane2.add(btnPrevius, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNext = new JButton();
        btnNext.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/next.png")));
        btnNext.setText("");
        pane2.add(btnNext, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnClearFilters = new JButton();
        btnClearFilters.setText("Limpiar filtros");
        panel3.add(btnClearFilters, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtSearch = new FlatTextField();
        txtSearch.setPlaceholderText("Producto...");
        txtSearch.setShowClearButton(true);
        txtSearch.setText("");
        panel3.add(txtSearch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pane = new JPanel();
        pane.setLayout(new GridLayoutManager(1, 11, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(pane, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Género:");
        pane.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbSex = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("--Seleccione--");
        cbbSex.setModel(defaultComboBoxModel1);
        pane.add(cbbSex, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Categoría:");
        pane.add(label2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbCategory = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("--Seleccione--");
        cbbCategory.setModel(defaultComboBoxModel2);
        pane.add(cbbCategory, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Marca:");
        pane.add(label3, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBrand = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("--Seleccione--");
        cbbBrand.setModel(defaultComboBoxModel3);
        pane.add(cbbBrand, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Talla:");
        pane.add(label4, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbSize = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("--Seleccione--");
        cbbSize.setModel(defaultComboBoxModel4);
        pane.add(cbbSize, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Color:");
        pane.add(label5, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbColor = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("--Seleccione--");
        cbbColor.setModel(defaultComboBoxModel5);
        pane.add(cbbColor, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, -1), new Dimension(250, -1), new Dimension(250, -1), 0, false));
        final Spacer spacer3 = new Spacer();
        pane.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final FlatTabbedPane flatTabbedPane1 = new FlatTabbedPane();
        tabPane.add(flatTabbedPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        flatTabbedPane1.addTab("", new ImageIcon(getClass().getResource("/com/babas/icons/x24/catalogue2.png")), scrollPane1);
        table = new FlatTable();
        table.setRowHeight(25);
        scrollPane1.setViewportView(table);
        scrollPane = new JScrollPane();
        flatTabbedPane1.addTab("", new ImageIcon(getClass().getResource("/com/babas/icons/x24/catalogue.png")), scrollPane);
        panelProducts = new JPanel();
        panelProducts.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane.setViewportView(panelProducts);
        flatTabbedPane1.setLeadingComponent(pane2);
        flatTabbedPane1.setTrailingComponent(pane);
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
