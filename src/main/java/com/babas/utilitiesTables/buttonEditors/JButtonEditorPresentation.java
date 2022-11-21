package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Color;
import com.babas.models.Presentation;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.utilitiesTables.tablesModels.PresentationsAbstractModel;
import com.babas.views.dialogs.DColor;
import com.babas.views.dialogs.DPresentation;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorPresentation extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorPresentation(boolean edit) {
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
            fireEditingStopped();
            Presentation presentation=((PresentationsAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DPresentation dPresentation=new DPresentation(presentation);
                dPresentation.setVisible(true);
            }else{
                if (presentation.getProduct().getPresentations().size()>1){
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Presentación",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        presentation.getProduct().getPresentations().remove(presentation);
                        if(presentation.getId()!=null){
                            presentation.refresh();
                            if(presentation.isDefault()){
                                presentation.getProduct().getPresentations().get(0).setDefault(true);
                                presentation.getProduct().getPresentations().get(0).save();
                                presentation.getProduct().setPresentationDefault(presentation.getProduct().getPresentations().get(0));
                            }
                            presentation.getPrices().forEach(price -> {
                                price.setActive(false);
                                price.save();
                            });
                            presentation.setActive(false);
                            presentation.save();
                        }
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Presentación eliminada");
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No puede eliminar todas las presentaciones");
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
