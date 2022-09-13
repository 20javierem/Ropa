package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.Product;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DImageProduct extends JDialog{
    private Product product;
    private JPanel contentPane;
    private JButton btnNext;
    private JButton btnPrevious;
    private ImageSlide imageSlide;

    public DImageProduct(Product product){
        super(Utilities.getJFrame(),"Imagenes",true);
        this.product=product;
        init();
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toNext();
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toPrevious();
            }
        });
    }

    private void init(){
        setContentPane(contentPane);
        loadImages();
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void loadImages(){
        product.getImages().forEach(img->{
            imageSlide.addImage(new ImageIcon(Utilities.getImage(img)));
        });
    }
}
