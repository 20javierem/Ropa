package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Presentation;
import com.babas.models.Price;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.PresentationsAbstractModel;
import com.babas.utilitiesTables.tablesModels.PriceAbstractModel;
import com.babas.views.dialogs.DPresentation;
import com.babas.views.dialogs.DPrice;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorPrice extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorPrice(boolean edit) {
        this.edit=edit;
        if(edit){
            button=new JButtonAction("x16/editar.png");
        }else{
            button=new JButtonAction("x16/remove.png");
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
            Price price=((PriceAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DPrice dPrice=new DPrice(price);
                dPrice.setVisible(true);
            }else{
                if (price.getPresentation().getPrices().size()>1){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Precio",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        price.refresh();
                        price.getPresentation().getPrices().remove(price);
                        if(price.isDefault()){
                            price.getPresentation().getPrices().get(0).setDefault(true);
                            price.getPresentation().getPrices().get(0).save();
                            price.getPresentation().setPriceDefault(price.getPresentation().getPrices().get(0));
                        }
                        price.delete();
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Precio eliminado");
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No puede eliminar todos los precios");
                }
            }
            Utilities.updateDialog();
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