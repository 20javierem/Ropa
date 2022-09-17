package com.babas.views.frames;

import com.babas.controllers.Products;
import com.babas.models.Product;
import com.formdev.flatlaf.extras.components.FlatLabel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PruebaList extends JFrame{
    private JPanel contentPane;
    private JList list;

    public PruebaList(){
        DefaultListModel<Product> productDefaultListModel=new DefaultListModel<>();
        list.setModel(productDefaultListModel);
        Products.getTodos().forEach(productDefaultListModel::addElement);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component=super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Product product=((Product) value);
                Diseño diseño=new Diseño(product);
                diseño.getContentPane().setBackground(component.getBackground());
                return diseño.getContentPane();
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX()>list.getVisibleRect().getWidth()-23){
                    productDefaultListModel.getElementAt(list.getSelectedIndex()).setActive(!productDefaultListModel.getElementAt(list.getSelectedIndex()).isActive());
                    productDefaultListModel.set(list.getSelectedIndex(),Products.getTodos().get(list.getSelectedIndex()));
                }
            }
        });
        productDefaultListModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                System.out.println("cmabio");
            }
        });
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        list.clearSelection();
        System.out.println(list.getModel().getSize());
    }
}
