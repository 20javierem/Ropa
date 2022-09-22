package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DImageProduct extends JDialog{
    private Product product;
    private JPanel contentPane;
    private JButton btnNext;
    private JButton btnPrevious;
    private ImageSlide imageSlide;
    private JLabel lblTextImage;
    private StockProductAbstractModel model;
    private FlatTable table;
    private JTabbedPane tabbedPane;
    private int pX,pY;

    public DImageProduct(Product product){
        super(Utilities.getJFrame(),product.getStyle().getName()+", "+product.getStyle().getBrand().getName()+", "+product.getSize().getName()+", "+product.getColor().getName(),true);
        this.product=product;
        init();
        tabbedPane.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me) {
                pX=me.getX();
                pY=me.getY();
            }
        });
        tabbedPane.addMouseMotionListener(new MouseAdapter(){
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
    }

    private void init(){
        setUndecorated(true);
        setContentPane(contentPane);
        loadImages();
        loadTable();
        pack();
        setLocationRelativeTo(getOwner());
    }
    private void loadTable(){
        model=new StockProductAbstractModel(product.getStocks());
        table.setModel(model);
        StockCellRendered.setCellRenderer(table,null);
        UtilitiesTables.headerNegrita(table);
        table.removeColumn(table.getColumn("PRODUCTO"));
    }
    private void loadImages(){
        product.getImages().forEach(img->{
            imageSlide.addImage(new ImageIcon(Utilities.getImage(img)));
        });
        lblTextImage.setText(imageSlide.getImagePosition());
    }
}
