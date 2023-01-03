package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Category;
import com.babas.models.Size;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.SizeAbstractModel;
import com.babas.views.dialogs.DCategory;
import com.babas.views.dialogs.DSize;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorSize extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorSize(boolean edit) {
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
            Size size=((SizeAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DSize dSize=new DSize(size);
                dSize.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Talla",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    size.refresh();
                    FPrincipal.sizes.remove(size);
                    FPrincipal.sizesWithAll.remove(size);
                    size.setActive(false);
                    size.save();
                    Utilities.getLblIzquierda().setText("Talla eliminada : "+Utilities.formatoFechaHora.format(size.getUpdated()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Talla eliminada");
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
