package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.dialogs.DProduct;
import com.babas.views.dialogs.DProductCatalogue;
import com.babas.views.frames.FPrincipal;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorProduct extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private String type;

    public JButtonEditorProduct(String type) {
        this.type=type;
        switch (type){
            case "edit":
                button=new JButtonAction("x16/editar.png");
                break;
            case "delete":
                button=new JButtonAction("x16/remove.png");
                break;
            case "images":
                button=new JButtonAction("x16/mostrarContraseña.png");
                break;
        }
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        button.setActionCommand("edit");
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable)button.getParent();
        if(table.getSelectedRow()!=-1){
            fireEditingStopped();
            Product product=((ProductAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            switch (type){
                case "edit":
                    DProduct dProduct=new DProduct(product);
                    dProduct.setVisible(true);
                    Utilities.updateDialog();
                    Utilities.getTabbedPane().updateTab();
                    break;
                case "delete":
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Producto",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        product.setActive(false);
                        product.save();
                        FPrincipal.products.remove(product);
                        product.getStyle().getProducts().remove(product);
                        Utilities.updateDialog();
                        Utilities.getTabbedPane().updateTab();
                    }
                    break;
                case "images":
                    DProductCatalogue dImageProduct=new DProductCatalogue(product);
                    dImageProduct.setVisible(true);
                    break;
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}
