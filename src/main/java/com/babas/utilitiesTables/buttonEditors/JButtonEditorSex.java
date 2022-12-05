package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Color;
import com.babas.models.Sex;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.utilitiesTables.tablesModels.SexAbstractModel;
import com.babas.views.dialogs.DAllSexs;
import com.babas.views.dialogs.DColor;
import com.babas.views.dialogs.DSex;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorSex extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorSex(boolean edit) {
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
            Sex sex=((SexAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DSex dSex=new DSex(sex);
                dSex.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Género",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    sex.refresh();
                    FPrincipal.sexs.remove(sex);
                    FPrincipal.sexsWithAll.remove(sex);
                    sex.setActive(false);
                    sex.save();
                    Utilities.getLblIzquierda().setText("Género eliminado : "+Utilities.formatoFechaHora.format(sex.getUpdated()));
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Género eliminado");
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
