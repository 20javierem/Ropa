package com.babas.views.dialogs;

import com.babas.models.*;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
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

public class DaddProductToRental extends JDialog{
    private JPanel contentPane;
    private FlatTextField txtSearchProduct;
    private FlatTable table;
    private JLabel lblProduct;
    private JButton btnHecho;
    private FlatSpinner spinerQuantity;
    private JButton btnAddProduct;
    private JComboBox cbbPresentation;
    private JComboBox cbbPrice;
    private Rental rental;
    private StockProductAbstractModel model;
    private int pX,pY;
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();
    private TableRowSorter<StockProductAbstractModel> modeloOrdenado;
    private List<RowFilter<StockProductAbstractModel, String>> filtros = new ArrayList<>();
    private RowFilter filtroand;
    private Product product;

    public DaddProductToRental(Rental rental){
        super(Utilities.getJFrame(),true);
        this.rental=rental;
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
        lblProduct.setText(null);
        setLocationRelativeTo(getOwner());
    }

    public void filtrar() {
        String busqueda = txtSearchProduct.getText().trim();
        filtros.clear();
        filtros.add(RowFilter.regexFilter("(?i)" +busqueda,1,2));
        listaFiltros.put(0, busqueda);
        listaFiltros.put(1, busqueda);
        filtroand = RowFilter.andFilter(filtros);
        modeloOrdenado.setRowFilter(filtroand);
    }

    private void loadTables(){
        model=new StockProductAbstractModel(rental.getBranch().getStocks());
        table.setModel(model);
        StockCellRendered.setCellRenderer(table,listaFiltros);
        UtilitiesTables.headerNegrita(table);
        table.removeColumn(table.getColumn("SUCURSAL"));
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
    }
    private void addProduct(){
        DetailRental detailRental=new DetailRental();
        detailRental.setRental(rental);
        detailRental.setProduct(product);
        detailRental.setPresentation((Presentation) cbbPresentation.getSelectedItem());
        detailRental.setQuantity((Integer) spinerQuantity.getValue());
        if(!((JTextField)cbbPrice.getEditor().getEditorComponent()).getText().isEmpty()){
            detailRental.setPrice(Double.valueOf(((JTextField)cbbPrice.getEditor().getEditorComponent()).getText()));
        }else{
            if(detailRental.getPresentation()!=null){
                detailRental.setPrice(detailRental.getPresentation().getPriceDefault().getPrice());
            }else{
                detailRental.setPrice(0.0);
            }
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(detailRental);
        if(constraintViolationSet.isEmpty()){
            searchProduct(detailRental);
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void searchProduct(DetailRental detailRental){
        boolean entro=false;
        for (DetailRental detailRental1 : rental.getDetailRentals()) {
            if(Objects.equals(detailRental1.getProduct().getId(), detailRental.getProduct().getId())&&Objects.equals(detailRental.getPresentation().getId(),detailRental1.getProduct().getId())){
                detailRental1.setQuantity(detailRental1.getQuantity()+detailRental.getQuantity());
                entro=true;
                break;
            }
        }
        if(!entro){
            rental.getDetailRentals().add(detailRental);
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
