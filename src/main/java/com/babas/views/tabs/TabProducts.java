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

import javax.swing.*;
import javax.swing.table.TableRowSorter;
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
        JButton button = (JButton) event.getSource();
        if ("import".equals(button.getActionCommand())) {
            Excel excel = new Excel();
            if (excel.initialize()) {
                excel.loadData();
            }
        } else {
            //export
        }
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
        filtros.add(RowFilter.regexFilter("(?i)" + busqueda, 0,1,2,3,4,5,6,7,8));
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

}
