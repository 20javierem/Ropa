package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.Product;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.event.*;

public class DImageProduct extends JDialog{
    private Product product;
    private JPanel contentPane;
    private JButton btnNext;
    private JButton btnPrevious;
    private ImageSlide imageSlide;
    private JLabel lblTextImage;
    private JLabel lblProduct;

    public DImageProduct(Product product){
        super(Utilities.getJFrame(),"Imagenes",true);
        this.product=product;
        init();
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
        setContentPane(contentPane);
        loadImages();
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void loadImages(){
        lblProduct.setText(product.getStyle().getName()+", Marca "+product.getStyle().getBrand().getName()+", Color "+product.getColor().getName());
        product.getImages().forEach(img->{
            imageSlide.addImage(new ImageIcon(Utilities.getImage(img)));
        });
        lblTextImage.setText(imageSlide.getImagePosition());
    }
}
