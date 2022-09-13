package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorPresentation;
import com.babas.utilitiesTables.tablesCellRendered.PresentationCellRendered;
import com.babas.utilitiesTables.tablesModels.PresentationsAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class DProduct extends JDialog{
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbCategory;
    private JButton btnNewCategory;
    private JButton btnNewSize;
    private JButton btnNewColor;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private FlatTable table;
    private JButton btnNewPresentation;
    private JComboBox cbbStyle;
    private JComboBox cbbSex;
    private JButton btnNewSex;
    private ImageSlide imageSlide;
    private JButton btnAddImage;
    private JLabel quantityImages;
    private JButton btnNext;
    private JButton btnPrevious;
    private Product product;
    private boolean update;
    private PresentationsAbstractModel model;
    private Style style=new Style();

    public DProduct(Product product){
        super(Utilities.getJFrame(),"Nuevo producto",true);
        this.product=product;
        update=product.getId()!=null;
        init();
        cbbStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStyle();
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
        btnNewSex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewSex();
            }
        });
        btnNewCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewCategory();
            }
        });
        btnNewColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewColor();
            }
        });
        btnNewSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewSize();
            }
        });
        btnNewPresentation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnNewPresentation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewPresentation();
            }
        });
        btnAddImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddNewImage();
            }
        });
    }
    private void loadAddNewImage(){
        DCrop dCrop=new DCrop();
        dCrop.setVisible(true);
        BufferedImage bufferedImage=DCrop.imageSelected;
        DataBufferByte byteArrayOutputStream= (DataBufferByte) bufferedImage.getData().getDataBuffer();
        try {
            Utilities.newImage(new FileInputStream(Arrays.toString(byteArrayOutputStream.getData())),"imagen");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void loadNewPresentation(){
        DPresentation dPresentation=new DPresentation(new Presentation(style));
        dPresentation.setVisible(true);

    }
    private void loadNewSex(){
        DSex dSex=new DSex(new Sex());
        dSex.setVisible(true);
    }
    private void loadNewCategory(){
        DCategory dCategory=new DCategory(new Category());
        dCategory.setVisible(true);
    }
    private void loadNewColor(){
        DColor dColor=new DColor(new Color());
        dColor.setVisible(true);
    }
    private void loadNewSize(){
        DSize dSize=new DSize(new Size());
        dSize.setVisible(true);
    }
    private void loadStyle(){
        if(cbbStyle.getSelectedItem() instanceof Style){
            style= (Style) cbbStyle.getSelectedItem();
            cbbCategory.setSelectedItem(style.getCategory());
            loadTable();
            UtilitiesTables.actualizarTabla(table);
        }else{
            style=new Style();
            cbbCategory.setSelectedIndex(-1);
            loadTable();
            UtilitiesTables.actualizarTabla(table);
        }
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        Utilities.setActionsdOfDialog(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UtilitiesTables.actualizarTabla(table);
            }
        });
        loadCombos();
        if(update){
            btnHecho.setText("Cancelar");
            btnSave.setText("Guardar");
            load();
        }
        loadTable();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadCombos(){
        cbbStyle.setModel(new DefaultComboBoxModel(FPrincipal.styles));
        cbbStyle.setRenderer(new Style.ListCellRenderer());
        cbbCategory.setModel(new DefaultComboBoxModel(FPrincipal.categories));
        cbbCategory.setRenderer(new Category.ListCellRenderer());
        cbbSize.setModel(new DefaultComboBoxModel(FPrincipal.sizes));
        cbbSize.setRenderer(new Size.ListCellRenderer());
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexs));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbColor.setModel(new DefaultComboBoxModel(FPrincipal.colors));
        cbbColor.setRenderer(new Color.ListCellRenderer());
        cbbStyle.setSelectedIndex(-1);
        cbbCategory.setSelectedIndex(-1);
        cbbSize.setSelectedIndex(-1);
        cbbColor.setSelectedIndex(-1);
        cbbSex.setSelectedIndex(-1);
        AutoCompleteDecorator.decorate(cbbStyle);
    }
    private void load(){
        cbbStyle.setSelectedItem(product.getStyle());
        cbbCategory.setSelectedItem(product.getStyle().getCategory());
        cbbColor.setSelectedItem(product.getColor());
        cbbSize.setSelectedItem(product.getSize());
        cbbSex.setSelectedItem(product.getSex());
        style=product.getStyle();
    }
    private void loadTable(){
        model=new PresentationsAbstractModel(style.getPresentations());
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        PresentationCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorPresentation(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorPresentation(true));
    }
    private void onSave(){
        boolean createdStyle=true;
        if((cbbStyle.getSelectedItem() instanceof Style)){
            style= (Style) cbbStyle.getSelectedItem();
            createdStyle=false;
        }else{
            style.setName(String.valueOf(cbbStyle.getEditor().getItem()));
        }
        style.setCategory((Category) cbbCategory.getSelectedItem());
        product.setStyle(style);
        product.setColor((Color) cbbColor.getSelectedItem());
        product.setSex((Sex) cbbSex.getSelectedItem());
        product.setSize((Size) cbbSize.getSelectedItem());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(style);
        constraintViolationSet.addAll(ProgramValidator.loadViolations(product));
        if(constraintViolationSet.isEmpty()){
            style.save();
            product.save();
            if(createdStyle){
                FPrincipal.styles.add(style);
            }
            if(!update){
                FPrincipal.products.add(product);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                product=new Product();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Producto registrado");
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Producto actualizado");
                onHecho();
            }

        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void clear(){
        cbbStyle.setSelectedIndex(-1);
        cbbCategory.setSelectedIndex(-1);
        cbbSize.setSelectedIndex(-1);
        cbbColor.setSelectedIndex(-1);
        cbbSex.setSelectedIndex(-1);
        loadStyle();
    }
    private void onHecho(){
        if(update){
            product.refresh();
        }
        dispose();
    }

}
