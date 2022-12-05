package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Brand;
import com.babas.models.Sex;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.utilitiesTables.tablesModels.SexAbstractModel;
import com.babas.views.dialogs.DBrand;
import com.babas.views.dialogs.DSex;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorBrand extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorBrand(boolean edit) {
        this.edit=edit;
        if(edit){
            button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/edit.svg")));
        }else{
            button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
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
            Brand brand=((BrandAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DBrand dBrand=new DBrand(brand);
                dBrand.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Marca",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    brand.refresh();
                    FPrincipal.brands.remove(brand);
                    FPrincipal.brandsWithAll.remove(brand);
                    brand.setActive(false);
                    brand.save();
                    Utilities.getLblIzquierda().setText("Marca eliminada : "+Utilities.formatoFechaHora.format(brand.getUpdated()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Marca eliminada");
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
