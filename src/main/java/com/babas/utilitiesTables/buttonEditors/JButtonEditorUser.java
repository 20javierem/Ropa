package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FPrincipal;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorUser extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorUser(boolean edit) {
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
            User user=((UserAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DUser dUser=new DUser(user);
                dUser.setVisible(true);
            }else{
                boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Usuario",JOptionPane.YES_NO_OPTION)==0;
                if(si){
                    user.refresh();
                    user.getBranchs().forEach(branch -> {
                        branch.getUsers().remove(user);
                        branch.save();
                    });
                    if(user.getSales().isEmpty()){
                        user.delete();
                    }else{
                        user.setActive(false);
                        user.save();
                    }
                    FPrincipal.users.remove(user);
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
