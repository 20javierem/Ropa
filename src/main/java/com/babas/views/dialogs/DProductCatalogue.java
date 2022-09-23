package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

public class DProductCatalogue extends JDialog{
    private JLabel lblTextImage;
    private ImageSlide imageSlide;
    private JButton btnPrevious;
    private JButton btnNext;
    private JPanel contentPane;
    private JComboBox cbbBrands;
    private JLabel lblProduct;
    private JComboBox cbbColors;
    private JComboBox cbbSizes;
    private JComboBox cbbStades;
    private JComboBox cbbDimentions;
    private JComboBox cbbQuantity;
    private JComboBox cbbPrice;
    private JComboBox cbbSex;
    private Product product;
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
    }
    private void filter(){

    }
    private void init(){
        setUndecorated(true);
        setContentPane(contentPane);
        loadStyle();
        loadProduct();
        loadImages();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadProduct(){
        if(product.getBrand()!=null){
            cbbBrands.setSelectedItem(product.getBrand());
        }
        if(product.getColor()!=null){
            cbbColors.setSelectedItem(product.getColor());
        }
        if(product.getSize()!=null){
            cbbSizes.setSelectedItem(product.getSize());
        }
        if(product.getStade()!=null){
            cbbStades.setSelectedItem(product.getStade());
        }
        if(product.getDimention()!=null){
            cbbDimentions.setSelectedItem(product.getDimention());
        }
        if(product.getSex()!=null){
            cbbDimentions.setSelectedItem(product.getSex());
        }
        cbbQuantity.setModel(new DefaultComboBoxModel(new Vector(product.getPresentations())));
        cbbQuantity.setRenderer(new Presentation.ListCellRenderer());
        cbbPrice.setModel(new DefaultComboBoxModel(new Vector(((Presentation)cbbQuantity.getSelectedItem()).getPrices())));
        cbbPrice.setRenderer(new Price.ListCellRenderer());
    }
    private void loadStyle(){
        lblProduct.setText(product.getStyle().getName());
        Vector<Brand> brands=new Vector<>();
//        brands.add(new Brand("--TODAS--"));
        Vector<Color> colors=new Vector<>();
//        colors.add(new Color("--TODOS--"));
        Vector<Size> sizes=new Vector<>();
//        sizes.add(new Size("--TODOS--"));
        Vector<Stade> stades=new Vector<>();
//        stades.add(new Stade("--TODOS--"));
        Vector<Dimention> dimentions=new Vector<>();
//        dimentions.add(new Dimention("--TODAS--"));
        Vector<Sex> sexs=new Vector<>();
        product.getStyle().getProducts().forEach(product1 -> {
            if(product1.getBrand()!=null&&!brands.contains(product1.getBrand())){
                brands.add(product1.getBrand());
            }
            if(product1.getColor()!=null&&!colors.contains(product1.getColor())){
                colors.add(product1.getColor());
            }
            if(product1.getSize()!=null&&!sizes.contains(product1.getSize())){
                sizes.add(product1.getSize());
            }
            if(product1.getStade()!=null&&!stades.contains(product1.getStade())){
                stades.add(product1.getStade());
            }
            if(product1.getDimention()!=null&&!dimentions.contains(product1.getDimention())){
                dimentions.add(product1.getDimention());
            }
            if(product1.getSex()!=null&&!sexs.contains(product1.getSex())){
                sexs.add(product1.getSex());
            }
        });
        cbbSex.setModel(new DefaultComboBoxModel(sexs));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbBrands.setModel(new DefaultComboBoxModel(brands));
        cbbBrands.setRenderer(new Brand.ListCellRenderer());
        cbbColors.setModel(new DefaultComboBoxModel(colors));
        cbbColors.setRenderer(new Color.ListCellRenderer());
        cbbSizes.setModel(new DefaultComboBoxModel(sizes));
        cbbSizes.setRenderer(new Size.ListCellRenderer());
        cbbStades.setModel(new DefaultComboBoxModel(stades));
        cbbStades.setRenderer(new Stade.ListCellRenderer());
        cbbDimentions.setModel(new DefaultComboBoxModel(dimentions));
        cbbDimentions.setRenderer(new Dimention.ListCellRenderer());
    }

    private void loadImages(){
        product.getImages().forEach(img->{
            Image image=Utilities.getImage(img);
            if(image!=null){
                ImageIcon icon=new ImageIcon(image);
                imageSlide.addImage(icon);
            }else{
                return;
            }
        });
        lblTextImage.setText(imageSlide.getImagePosition());
    }

}
