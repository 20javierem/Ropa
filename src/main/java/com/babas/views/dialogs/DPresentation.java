package com.babas.views.dialogs;

import com.babas.models.Presentation;
import com.babas.models.Price;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorColor;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorPrice;
import com.babas.utilitiesTables.tablesCellRendered.PriceCellRendered;
import com.babas.utilitiesTables.tablesModels.PriceAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatSpinner;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Set;

public class DPresentation extends JDialog{
    private JPanel contentPane;
    private JButton btnHecho;
    private FlatSpinner spinnerQuantity;
    private JButton btnSave;
    private FlatTable table;
    private FlatSpinner spinnerPriceNew;
    private JButton btnNewPrice;
    private JCheckBox ckDefault;
    private Presentation presentation;
    private boolean update;
    private PriceAbstractModel model;
    private ActionListener actionListener;

    public DPresentation(Presentation presentation){
        super(Utilities.getJFrame(),"Nueva Presentación",true);
        this.presentation=presentation;
        update=presentation.getId()!=null;
        init();
        btnNewPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewPrice();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
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
    private void loadNewPrice(){
        DPrice dPrice=new DPrice(new Price(presentation));
        dPrice.setVisible(true);
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        actionListener= e -> UtilitiesTables.actualizarTabla(table);
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        if(update){
            setTitle("Actualizar Presentación");
            btnSave.setText("Guardar");
            btnHecho.setText("Cancelar");
            load();
        }
        loadTable();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void load(){
        spinnerQuantity.setValue(presentation.getQuantity());
        if(update){
            ckDefault.setSelected(presentation.isDefault());
        }
    }

    private void loadTable(){
        model=new PriceAbstractModel(presentation.getPrices());
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        PriceCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorPrice(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorPrice(true));
    }

    private void onSave(){
        presentation.setQuantity((Integer) spinnerQuantity.getValue());
        if(presentation.getStyle().getPresentations().size()<2){
            presentation.setDefault(true);
        }else{
            presentation.setDefault(ckDefault.isSelected());
        }
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(presentation);
        if(constraintViolationSet.isEmpty()){
            if(presentation.getStyle().getId()!=null){
                presentation.save();
                if(presentation.isDefault()&&presentation.getPriceDefault()!=null){
                    presentation.getStyle().getPresentationDefault().setDefault(false);
                    presentation.getStyle().getPresentationDefault().save();
                    presentation.getStyle().setPresentationDefault(presentation);
                    presentation.setDefault(true);
                    presentation.save();
                }
            }
            if(!update){
                presentation.getStyle().getPresentations().add(presentation);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                presentation=new Presentation(presentation.getStyle());
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Presentación registrada");
            }else{
                if(presentation.isDefault()){
                    presentation.getStyle().getPresentationDefault().setDefault(false);
                    presentation.getStyle().getPresentationDefault().save();
                    presentation.getStyle().setPresentationDefault(presentation);
                    presentation.setDefault(true);
                    presentation.save();
                }
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Presentación actualizada");
                onHecho();
            }

        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void onHecho(){
        if(update){
            presentation.refresh();
        }
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }
    private void clear(){
        spinnerQuantity.setValue(1);
        spinnerPriceNew.setValue(1.0);
        loadTable();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerQuantity=new FlatSpinner();
        spinnerQuantity.setModel(new SpinnerNumberModel(1, 1, 100000, 1));
        spinnerQuantity.setEditor(Utilities.getEditorPrice(spinnerQuantity));
        spinnerPriceNew=new FlatSpinner();
        spinnerPriceNew.setModel(new SpinnerNumberModel(1.00, 0.01, 100000.00, 0.50));
        spinnerPriceNew.setEditor(Utilities.getEditorPrice(spinnerPriceNew));
    }
}
