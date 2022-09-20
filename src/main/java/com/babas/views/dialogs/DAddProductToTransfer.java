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
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.*;

public class DAddProductToTransfer extends JDialog{
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
    private int pX,pY;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<ProductAbstractModel> modeloOrdenado1;
    private List<RowFilter<ProductAbstractModel, String>> filtros1 = new ArrayList<>();
    private TableRowSorter<StockProductAbstractModel> modeloOrdenado2;
    private List<RowFilter<StockProductAbstractModel, String>> filtros2 = new ArrayList<>();
    private RowFilter filtroand;
    private Product product;

    public DAddProductToTransfer(Transfer transfer,Branch branchSource){
        super(Utilities.getJFrame(),true);
        this.transfer=transfer;
        this.branchSource=branchSource;
        init();
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
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 38 || e.getKeyCode() == 40) {
                    loadProduct();
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadProduct();
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
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me) {
                pX=me.getX();
                pY=me.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x+me.getX()-pX,getLocation().y+me.getY()-pY);
            }
        });
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
    }
    private void addProduct(){
        DetailTransfer detailTransfer=new DetailTransfer();
        detailTransfer.setProduct(product);
        detailTransfer.setTransfer(transfer);
        detailTransfer.setQuantity((Integer) spinerQuantity.getValue());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(detailTransfer);
        if(constraintViolationSet.isEmpty()){
            searchProduct(detailTransfer);
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void searchProduct(DetailTransfer detailTransfer){
        boolean entro=false;
        for (DetailTransfer detailTransfer1 : transfer.getDetailTransfers()) {
            if(Objects.equals(detailTransfer1.getProduct().getId(), detailTransfer.getProduct().getId())){
                detailTransfer1.setQuantity(detailTransfer1.getQuantity()+detailTransfer.getQuantity());
                entro=true;
                break;
            }
        }
        if(!entro){
            transfer.getDetailTransfers().add(detailTransfer);
        }
        Utilities.getTabbedPane().updateTab();
        table.clearSelection();
        product=null;
        loadProduct();
    }
    private void loadProduct() {
        if(branchSource==null){
            if(table.getSelectedRow()!=-1){
                product=productAbstractModel.getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            }
        }else{
            if(table.getSelectedRow()!=-1){
                product=stockProductAbstractModel.getList().get(table.convertRowIndexToModel(table.getSelectedRow())).getProduct();
            }
        }
        if(product!=null){
            lblProduct.setText(product.getStyle().getName()+", "+product.getSex().getName()+", "+product.getSize().getName()+", "+product.getColor().getName());
        }else{
            lblProduct.setText(null);
        }
    }
    public void filtrar() {
        String busqueda;
        busqueda = txtSearchProduct.getText().trim();
        if(branchSource==null){
            filtros1.clear();
            filtros1.add(RowFilter.regexFilter("(?i)" +busqueda,0,1,3,4,5));
            listaFiltros.put(0, busqueda);
            listaFiltros.put(1, busqueda);
            listaFiltros.put(2, busqueda);
            listaFiltros.put(3, busqueda);
            listaFiltros.put(4, busqueda);
            filtroand = RowFilter.andFilter(filtros1);
            modeloOrdenado1.setRowFilter(filtroand);
        }else{
            filtros2.clear();
            filtros2.add(RowFilter.regexFilter("(?i)" +busqueda,0,1,3,4,5));
            listaFiltros.put(0, busqueda);
            listaFiltros.put(1, busqueda);
            listaFiltros.put(2, busqueda);
            listaFiltros.put(3, busqueda);
            listaFiltros.put(4, busqueda);
            filtroand = RowFilter.andFilter(filtros2);
            modeloOrdenado2.setRowFilter(filtroand);
        }

    }
    private void init(){
        setContentPane(contentPane);
        setUndecorated(true);
        if(branchSource==null){
            loadTable1();
        }else{
            loadTable2();
        }
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void loadTable1(){
        productAbstractModel=new ProductAbstractModel(FPrincipal.products);
        table.setModel(productAbstractModel);
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table,listaFiltros);
        table.removeColumn(table.getColumn("PRECIO"));
        table.removeColumn(table.getColumn("TOTAL-STOCK"));
        table.removeColumn(table.getColumn(""));
        table.removeColumn(table.getColumn(""));
        table.removeColumn(table.getColumn(""));
        modeloOrdenado1 = new TableRowSorter<>(productAbstractModel);
        table.setRowSorter(modeloOrdenado1);
    }
    private void loadTable2(){
        stockProductAbstractModel=new StockProductAbstractModel(branchSource.getStocks());
        table.setModel(stockProductAbstractModel);
        UtilitiesTables.headerNegrita(table);
        StockCellRendered.setCellRenderer(table,listaFiltros);
        modeloOrdenado2 = new TableRowSorter<>(stockProductAbstractModel);
        table.setRowSorter(modeloOrdenado2);
    }
    private void onHecho(){
        dispose();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinerQuantity=new FlatSpinner();
        spinerQuantity.setModel(new SpinnerNumberModel(1, 1, 100000, 1));
        spinerQuantity.setEditor(Utilities.getEditorPrice(spinerQuantity));
    }
}
