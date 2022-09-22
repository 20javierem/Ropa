package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Branch;
import com.babas.models.Brand;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DBrand;
import com.babas.views.frames.FPrincipal;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorBranch extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorBranch(boolean edit) {
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
            Branch branch=((BranchAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DBranch dBranch=new DBranch(branch);
                dBranch.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Sucursal",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    branch.refresh();
                    branch.getUsers().forEach(user -> {
                        user.getBranchs().remove(branch);
                        user.save();
                    });
                    FPrincipal.branchs.remove(branch);
                    FPrincipal.branchesWithAll.remove(branch);
                    if(branch.getSales().isEmpty()){
                        branch.delete();
                    }else{
                        branch.setActive(false);
                        branch.save();
                    }
                }
            }
            Utilities.updateDialog();
            fireEditingStopped();
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
