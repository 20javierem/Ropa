package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Product;
import com.babas.utilities.Excel;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.babas.views.dialogs.*;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabProducts {
    private TabPane tabPane;
    private FlatTable table;
    private FlatTextField txtSearch;
    private JButton btnCategorys;
    private JButton btnSizes;
    private JButton btnColors;
    private JButton btnNewStyle;
    private JButton btnAllSexs;
    private JButton btnBrands;
    private JButton btnStades;
    private JButton btnDimentions;
    private JButton btnExport;
    private JButton btnImport;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<ProductAbstractModel> modeloOrdenado;
    private List<RowFilter<ProductAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private ProductAbstractModel model;

    public TabProducts() {
        init();
        btnSizes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadColors();
            }
        });
        btnCategorys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCategorys();
            }
        });
        btnSizes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSizes();
            }
        });
        btnNewStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewProduct();
            }
        });
        btnAllSexs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSexs();
            }
        });
        btnBrands.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBrands();
            }
        });
        btnDimentions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDimentions();
            }
        });
        btnStades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStades();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
        btnImport.setActionCommand("import");
        btnImport.addActionListener(this::fileExcelData);
        btnExport.setActionCommand("export");
        btnExport.addActionListener(this::fileExcelData);
    }

    private void fileExcelData(ActionEvent event) {
        tabPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        JButton button = (JButton) event.getSource();
        if ("import".equals(button.getActionCommand())) {
            Excel excel = new Excel();
            if (excel.initialize()) {
                excel.loadData();
            }
        } else {
            //export
        }
        tabPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void loadDimentions() {
        DAllDimentions dAllDimentions = new DAllDimentions();
        dAllDimentions.setVisible(true);
    }

    private void loadStades() {
        DAllStades dAllStades = new DAllStades();
        dAllStades.setVisible(true);
    }

    private void loadBrands() {
        DAllBrands dAllBrands = new DAllBrands();
        dAllBrands.setVisible(true);
    }

    private void loadSexs() {
        DAllSexs dAllSexs = new DAllSexs();
        dAllSexs.setVisible(true);
    }

    private void loadNewProduct() {
        DProduct dProduct = new DProduct(new Product());
        dProduct.setVisible(true);
    }

    private void loadColors() {
        DAllColors dAllColors = new DAllColors();
        dAllColors.setVisible(true);
    }

    private void loadSizes() {
        DAllSizes dAllSizes = new DAllSizes();
        dAllSizes.setVisible(true);
    }

    private void loadCategorys() {
        DAllCategorys dAllCategorys = new DAllCategorys();
        dAllCategorys.setVisible(true);
    }

    private void init() {
        tabPane.setTitle("Productos");
        loadIcons();
        getTabPane().getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                filter();
            }
        });
        loadTable();
    }

    private void loadIcons() {
        txtSearch.setLeadingIcon(new FlatSearchIcon());
    }

    private void loadTable() {
        model = new ProductAbstractModel(FPrincipal.products);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table, listaFiltros);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorProduct("delete"));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorProduct("edit"));
        table.removeColumn(table.getColumn(""));
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void filter() {
        String busqueda = txtSearch.getText().trim();
        filtros.clear();
        filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0, 1, 2, 3, 4, 5, 6, 7, 8));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        listaFiltros.put(5, busqueda);
        listaFiltros.put(6, busqueda);
        listaFiltros.put(7, busqueda);
        listaFiltros.put(8, busqueda);
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
        if (model.getList().size() == table.getRowCount()) {
            Utilities.getLblCentro().setText("Productos: " + model.getList().size());
        } else {
            Utilities.getLblCentro().setText("Productos filtrados: " + table.getRowCount());
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
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
        tabPane.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), 5, 5));
        panel1.add(tabPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Productos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Buscar", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtSearch = new FlatTextField();
        txtSearch.setPlaceholderText("Producto...");
        txtSearch.setShowClearButton(true);
        txtSearch.setText("");
        panel3.add(txtSearch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabPane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(null, "Opciones", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnCategorys = new JButton();
        btnCategorys.setText("Categorias");
        panel5.add(btnCategorys, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnColors = new JButton();
        btnColors.setText("Colores");
        panel5.add(btnColors, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSizes = new JButton();
        btnSizes.setText("Tallas");
        panel5.add(btnSizes, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNewStyle = new JButton();
        btnNewStyle.setText("Nuevo producto");
        panel5.add(btnNewStyle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnAllSexs = new JButton();
        btnAllSexs.setText("Géneros");
        panel5.add(btnAllSexs, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBrands = new JButton();
        btnBrands.setText("Marcas");
        panel5.add(btnBrands, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnStades = new JButton();
        btnStades.setText("Estados");
        panel5.add(btnStades, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDimentions = new JButton();
        btnDimentions.setText("Dimensiones");
        panel5.add(btnDimentions, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(null, "Exportar", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnExport = new JButton();
        btnExport.setText("Exportar");
        panel6.add(btnExport, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnImport = new JButton();
        btnImport.setText("Importar");
        panel6.add(btnImport, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}
