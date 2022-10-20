package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.custom.JPanelGradiente;
import com.babas.models.*;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Vector;

public class DProductCatalogue extends JDialog{
    private JLabel lblTextImage;
    private ImageSlide imageSlide;
    private JButton btnPrevious;
    private JButton btnNext;
    private JPanel contentPane;
    private JComboBox cbbBrand;
    private JLabel lblProduct;
    private JComboBox cbbColor;
    private JComboBox cbbSize;
    private JComboBox cbbStade;
    private JComboBox cbbDimention;
    private JComboBox cbbPresentation;
    private JComboBox cbbPrice;
    private JComboBox cbbSex;
    private JComboBox cbbStyle;
    private JLabel lblCode;
    private FlatTable table;
    private Product product;
    private StockProductAbstractModel model;
    private int pX,pY;

    public DProductCatalogue(Product product){
        super(Utilities.getJFrame(),true);
        this.product=product;
        init();
        contentPane.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me) {
                pX=me.getX();
                pY=me.getY();
            }
        });
        contentPane.addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x+me.getX()-pX,getLocation().y+me.getY()-pY);
            }
        });
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toPrevious();
                lblTextImage.setText(imageSlide.getImagePosition());
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toNext();
                lblTextImage.setText(imageSlide.getImagePosition());
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        cbbSex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cbbStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProduct();
            }
        });
        cbbPresentation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPrices();
            }
        });
    }

    private void init(){
        setUndecorated(true);
        setContentPane(contentPane);
        loadProducts();
        loadProduct();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadProduct(){
        product= (Product) cbbStyle.getSelectedItem();
        cbbBrand.removeAllItems();
        if(product.getBrand()!=null){
            cbbBrand.addItem(product.getBrand().getName());
        }else{
            cbbBrand.addItem("N/A");
        }
        cbbSex.removeAllItems();
        if(product.getSex()!=null){
            cbbSex.addItem(product.getSex().getName());
        }else{
            cbbSex.addItem("N/A");
        }
        cbbColor.removeAllItems();
        if(product.getColor()!=null){
            cbbColor.addItem(product.getColor().getName());
        }else{
            cbbColor.addItem("N/A");
        }
        cbbSize.removeAllItems();
        if(product.getSize()!=null){
            cbbSize.addItem(product.getSize().getName());
        }else{
            cbbSize.addItem("N/A");
        }
        cbbDimention.removeAllItems();
        if(product.getDimention()!=null){
            cbbDimention.addItem(product.getDimention().getName());
        }else{
            cbbDimention.addItem("N/A");
        }
        cbbStade.removeAllItems();
        if(product.getStade()!=null){
            cbbStade.addItem(product.getStade().getName());
        }else{
            cbbStade.addItem("N/A");
        }
        cbbPresentation.setModel(new DefaultComboBoxModel(new Vector(product.getPresentations())));
        cbbPresentation.setRenderer(new Presentation.ListCellRenderer());
        loadPrices();
        lblCode.setText(String.valueOf(product.getBarcode()));
        loadImages();
        loadTable();
    }
    private void loadPrices(){
        cbbPrice.setModel(new DefaultComboBoxModel(new Vector(((Presentation) cbbPresentation.getSelectedItem()).getPrices())));
        cbbPrice.setRenderer(new Price.ListCellRenderer());
    }
    private void loadProducts(){
        lblProduct.setText(product.getStyle().getName());
        cbbStyle.setModel(new DefaultComboBoxModel(new Vector(product.getStyle().getProducts())));
        cbbStyle.setRenderer(new Product.ListCellRenderer());
        cbbStyle.setSelectedItem(product);
    }

    private void loadImages(){
        imageSlide.clear();
        product.getIconsx400().forEach(icon-> {
            if(icon!=null){
                imageSlide.addImage(icon);
            }
        });
        lblTextImage.setText(imageSlide.getImagePosition());
        try {
            imageSlide.toNext();
        }catch (Exception ignored){

        }
    }

    private void loadTable(){
        model=new StockProductAbstractModel(product.getStocks());
        table.setModel(model);
        StockCellRendered.setCellRenderer(table,null);
        UtilitiesTables.headerNegrita(table);
        table.removeColumn(table.getColumn("PRODUCTO"));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new JPanelGradiente(new java.awt.Color(0xC6FFDD),new java.awt.Color(0xFBD786),new Color(0xF7797D));
    }
}
