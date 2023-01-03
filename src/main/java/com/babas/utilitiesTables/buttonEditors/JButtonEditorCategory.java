package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Category;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.views.dialogs.DCategory;
import com.babas.views.dialogs.DColor;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorCategory extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorCategory(boolean edit) {
        this.edit=edit;
        if(edit){
            button=new JButtonAction("edit");
        }else{
            button=new JButtonAction("error");
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
            Category category=((CategoryAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DCategory dCategory=new DCategory(category);
                dCategory.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Categoría",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    category.refresh();
                    FPrincipal.categories.remove(category);
                    FPrincipal.categoriesWithAll.remove(category);
                    category.setActive(false);
                    category.save();
                    Utilities.getLblIzquierda().setText("Categoría eliminada : "+Utilities.formatoFechaHora.format(category.getUpdated()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Categoría eliminada");
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
