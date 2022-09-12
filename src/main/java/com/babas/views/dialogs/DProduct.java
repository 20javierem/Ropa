package com.babas.views.dialogs;

import com.babas.controllers.Styles;
import com.babas.models.Product;
import com.babas.models.Style;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class DProduct extends JDialog{
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbCategory;
    private JButton btnNewCategory;
    private JButton btnNewSize;
    private JButton btnNewColor;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private FlatTable table;
    private JButton btnNewPresentation;
    private FlatTextField txtStyle;
    private Product product;
    private boolean update;
    private JPopupMenu menu;

    public DProduct(Product product){
        super(Utilities.getJFrame(),"Nuevo producto",true);
        this.product=product;
        update=product.getId()!=null;
        init();
        txtStyle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()!=KeyEvent.VK_UP&&e.getKeyCode()!=KeyEvent.VK_DOWN&&e.getKeyCode()!=KeyEvent.VK_ENTER){
                    loadMenu();
                }
            }
        });
    }

    private void loadMenu(){
        menu.removeAll();
        menu.setSize(new Dimension(400,250));
        List<Style> styles= new ArrayList<>();
        if(!txtStyle.getText().isBlank()){
            styles.addAll(Styles.search(txtStyle.getText().trim()));
        }
        if(!styles.isEmpty()){
            styles.forEach(style -> {
                JMenuItem menuItem=new JMenuItem(style.getName());
                menuItem.addActionListener(e -> {
                    txtStyle.setText(style.getName());
                    menu.setVisible(false);
                });
                menu.add(menuItem);
            });
            menu.updateUI();
            menu.show(txtStyle,txtStyle.getVisibleRect().x,txtStyle.getVisibleRect().y+txtStyle.getHeight());
            txtStyle.requestFocus();
        }else{
            menu.setVisible(false);
        }
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        menu=new JPopupMenu();
        pack();
        setLocationRelativeTo(getOwner());
    }

}
