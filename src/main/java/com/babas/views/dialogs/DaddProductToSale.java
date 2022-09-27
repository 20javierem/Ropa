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
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.*;

public class DaddProductToSale extends JDialog{
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
    private int pX,pY;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<StockProductAbstractModel> modeloOrdenado;
    private List<RowFilter<StockProductAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Product product;

    public DaddProductToSale(Sale sale){
        super(Utilities.getJFrame(),true);
        this.sale=sale;
        init();
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
        ((JButton)txtSearchProduct.getComponent(0)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchProduct.setText(null);
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
    private void loadPrices(){
        Presentation presentation= (Presentation) cbbPresentation.getSelectedItem();
        cbbPrice.removeAllItems();
        presentation.getPrices().forEach(price -> cbbPrice.addItem(price.getPrice()));
        cbbPrice.setSelectedItem(presentation.getPriceDefault().getPrice());
    }
    private void init(){
        setUndecorated(true);
        setContentPane(contentPane);
        loadTables();
        pack();
        setLocationRelativeTo(getOwner());
    }

    public void filtrar() {
        String busqueda;
        busqueda = txtSearchProduct.getText().trim();
        filtros.clear();
        filtros.add(RowFilter.regexFilter("(?i)" +busqueda,0,1,2,6,7));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        listaFiltros.put(2, busqueda);
        listaFiltros.put(3, busqueda);
        listaFiltros.put(4, busqueda);
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }

    private void loadTables(){
        model=new StockProductAbstractModel(sale.getBranch().getStocks());
        table.setModel(model);
        StockCellRendered.setCellRenderer(table,null);
        UtilitiesTables.headerNegrita(table);
        modeloOrdenado = new TableRowSorter<>(model);
        table.setRowSorter(modeloOrdenado);
    }

    private void loadProduct(){
        if(table.getSelectedRow()!=-1){
            product=model.getList().get(table.convertRowIndexToModel(table.getSelectedRow())).getProduct();
        }
        if(product!=null){
            lblProduct.setText(product.getStyle().getName()+", "+product.getSex().getName()+", "+product.getSize().getName()+", "+product.getColor().getName());
            cbbPresentation.setModel(new DefaultComboBoxModel(new Vector(product.getPresentations())));
            cbbPresentation.setRenderer(new Presentation.ListCellRenderer());
            cbbPresentation.setSelectedItem(product.getPresentationDefault());
            loadPrices();
        }else{
            lblProduct.setText(null);
            cbbPresentation.setModel(new DefaultComboBoxModel());
            cbbPresentation.setRenderer(new DefaultListCellRenderer());
            cbbPrice.removeAllItems();
        }
        pack();
    }
    private void addProduct(){
        DetailSale detailSale=new DetailSale();
        detailSale.setSale(sale);
        detailSale.setProduct(product);
        detailSale.setPresentation((Presentation) cbbPresentation.getSelectedItem());
        detailSale.setQuantity((Integer) spinerQuantity.getValue());
        if(!((JTextField)cbbPrice.getEditor().getEditorComponent()).getText().isEmpty()){
            detailSale.setPrice(Double.valueOf(((JTextField)cbbPrice.getEditor().getEditorComponent()).getText()));
        }else{
            if(detailSale.getPresentation()!=null){
                detailSale.setPrice(detailSale.getPresentation().getPriceDefault().getPrice());
            }else{
                detailSale.setPrice(0.0);
            }
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(detailSale);
        if(constraintViolationSet.isEmpty()){
            searchProduct(detailSale);
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void searchProduct(DetailSale detailSale){
        boolean entro=false;
        for (DetailSale detailSale1 : sale.getDetailSales()) {
            if(Objects.equals(detailSale1.getProduct().getId(), detailSale.getProduct().getId())&&Objects.equals(detailSale.getPresentation().getId(),detailSale1.getProduct().getId())){
                detailSale1.setQuantity(detailSale1.getQuantity()+detailSale.getQuantity());
                entro=true;
                break;
            }
        }
        if(!entro){
            sale.getDetailSales().add(detailSale);
        }
        Utilities.getTabbedPane().updateTab();
        table.clearSelection();
        product=null;
        loadProduct();
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
