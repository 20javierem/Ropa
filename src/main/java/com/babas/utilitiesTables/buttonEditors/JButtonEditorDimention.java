package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Color;
import com.babas.models.Dimention;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.utilitiesTables.tablesModels.DimentionAbstractModel;
import com.babas.views.dialogs.DColor;
import com.babas.views.dialogs.DDimention;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorDimention extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorDimention(boolean edit) {
        this.edit=edit;
        if(edit){
            button=new JButtonAction("x16/editar.png");
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
            Dimention dimention=((DimentionAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DDimention dDimention=new DDimention(dimention);
                dDimention.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Dimensión",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    dimention.refresh();
                    FPrincipal.dimentions.remove(dimention);
                    FPrincipal.dimentionsWithAll.remove(dimention);
                    dimention.setActive(false);
                    dimention.save();
                    Utilities.getLblIzquierda().setText("Dimensión eliminada : "+Utilities.formatoFechaHora.format(dimention.getUpdated()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Dimensión eliminada");
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
