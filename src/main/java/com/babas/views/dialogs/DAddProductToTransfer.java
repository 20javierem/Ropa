package com.babas.views.dialogs;

import com.babas.models.Branch;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.event.*;

public class DAddProductToTransfer extends JDialog{
    private JPanel contentPane;
    private JSpinner spinerQuantity;
    private JButton btnAddProduct;
    private JButton btnHecho;
    private FlatTable flatTable1;
    private FlatTextField txtSearchProduct;
    private JLabel lblProduct;
    private Branch branchSource;
    private ProductAbstractModel productAbstractModel;

    private DAddProductToTransfer(Branch branchSource){
        super(Utilities.getJFrame(),true);
        this.branchSource=branchSource;
        init();
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init(){
        setUndecorated(true);
        if(branchSource==null){

        }else{

        }
    }

    private void onHecho(){
        dispose();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
