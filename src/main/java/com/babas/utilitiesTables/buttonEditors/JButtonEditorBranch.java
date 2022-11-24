package com.babas.utilitiesTables.buttonEditors;

import com.babas.controllers.Stocks;
import com.babas.models.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DBrand;
import com.babas.views.frames.FPrincipal;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;

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
            fireEditingStopped();
            Branch branch=((BranchAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(edit){
                DBranch dBranch=new DBranch(branch,false);
                dBranch.setVisible(true);
            }else{
                if(FPrincipal.branchs.size()>1){
                    Utilities.getTabbedPane().getComponentAt(Utilities.getTabbedPane().getSelectedIndex()).setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?, esta acción no se puede deshacer","Eliminar Sucursal",JOptionPane.YES_NO_OPTION)==0;
                    if(si){
                        branch.refresh();
                        branch.getUsers().forEach(user -> {
                            user.getBranchs().remove(branch);
                            user.save();
                        });
                        if(!branch.getStocks().isEmpty()){
                            JComboBox comboBox = new JComboBox();
                            comboBox.setModel(new DefaultComboBoxModel(new Vector(FPrincipal.branchs)));
                            comboBox.setRenderer(new Branch.ListCellRenderer());
                            comboBox.removeItem(branch);
                            int option = JOptionPane.showOptionDialog(Utilities.getJFrame(), comboBox, "Transferencia de productos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Transferir", "Cancelar"}, "Transferir");
                            if (option == JOptionPane.OK_OPTION) {
                                Transfer transfer=new Transfer();
                                transfer.setState(1);
                                transfer.setSource(branch);
                                transfer.setDestiny((Branch) comboBox.getSelectedItem());
                                transfer.setDescription("Traslado por Sucursal cerrada");
                                branch.getStocks().forEach(stock -> {
                                    DetailTransfer detailTransfer=new DetailTransfer();
                                    detailTransfer.setTransfer(transfer);
                                    detailTransfer.setQuantity(stock.getQuantity());
                                    detailTransfer.setProduct(stock.getProduct());
                                    transfer.getDetailTransfers().add(detailTransfer);
                                });
                                transfer.calculateTotalProuctsTransfers();
                                transfer.setState(0);
                                transfer.save();
                                transfer.setState(1);
                                transfer.setUpdated(new Date());
                                transfer.save();
                                branch.getStocks().forEach(stock -> {
                                    Stock stock1=Stocks.getStock(transfer.getDestiny(),stock.getProduct());
                                    stock1.setOnStock(stock1.getOnStock()-stock.getOnRental());
                                    stock1.setOnReserve(stock1.getOnReserve()+stock.getOnReserve());
                                    stock1.setOnRental(stock1.getOnRental()+stock.getOnRental());
                                    stock1.save();
                                    stock.getProduct().getStocks().remove(stock);
                                    stock.setActive(false);
                                    stock.save();
                                });
                                branch.getTransfers().forEach(transfer1 -> {
                                    transfer1.setState(2);
                                    transfer1.save();
                                });
                                branch.getUsers().forEach(user -> {
                                    user.getBranchs().remove(branch);
                                    user.save();
                                });
                                FPrincipal.branchs.remove(branch);
                                FPrincipal.branchesWithAll.remove(branch);
                                branch.setActive(false);
                                branch.save();
                                Utilities.getLblIzquierda().setText("Sucursal: "+branch.getName()+" eliminada : "+Utilities.formatoFechaHora.format(branch.getUpdated()));
                                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Sucursal eliminada");
                            }
                        }else{
                            FPrincipal.branchs.remove(branch);
                            FPrincipal.branchesWithAll.remove(branch);
                            branch.setActive(false);
                            branch.save();
                            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Sucursal eliminada");
                        }
                    }
                    Utilities.getTabbedPane().getComponentAt(Utilities.getTabbedPane().getSelectedIndex()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No puede eliminar todas las sucursales");
                }
                Utilities.getTabbedPane().updateTab();
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
