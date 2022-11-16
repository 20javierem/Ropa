package com.babas.utilitiesTables.buttonEditors;

import com.babas.controllers.Branchs;
import com.babas.controllers.Users;
import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;

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
            fireEditingStopped();
            User user=((UserAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DUser dUser=new DUser(false,user);
                dUser.setVisible(true);
            }else{
                if(!user.getId().equals(Babas.user.getId())){
                    if(!user.getId().equals(1L)){
                        if(FPrincipal.users.size()>1){
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
                        }else{
                            Notify.sendNotify(Utilities.getJFrame(),Notify.Type.WARNING,Notify.Location.TOP_CENTER,"ERROR","No puede eliminar a todos los usuarios");
                        }
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(),Notify.Type.WARNING,Notify.Location.TOP_CENTER,"ERROR","No puede eliminar al administrador");
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(),Notify.Type.WARNING,Notify.Location.TOP_CENTER,"ERROR","No puede eliminarse a si mismo");
                }
            }
            Utilities.getTabbedPane().updateTab();
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
