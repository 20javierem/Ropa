package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Brand;
import com.babas.models.Sex;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.utilitiesTables.tablesModels.SexAbstractModel;
import com.babas.views.dialogs.DBrand;
import com.babas.views.dialogs.DSex;
import com.babas.views.frames.FPrincipal;

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
            Brand brand=((BrandAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DBrand dBrand=new DBrand(brand);
                dBrand.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Marca",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    brand.refresh();
                    if(brand.getStyles().isEmpty()){
                        FPrincipal.brands.remove(brand);
                        brand.delete();
                    }else{
                        brand.setActive(false);
                        FPrincipal.brands.remove(brand);
                    }
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
