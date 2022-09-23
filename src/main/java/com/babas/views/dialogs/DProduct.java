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
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;

public class DProduct extends JDialog{
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JButton btnHecho;
    private JButton btnSave;
    private JComboBox cbbCategory;
    private JXHyperlink btnNewCategory;
    private JXHyperlink btnNewSize;
    private JXHyperlink btnNewColor;
    private JComboBox cbbSize;
    private JComboBox cbbColor;
    private FlatTable table;
    private JButton btnNewPresentation;
    private JComboBox cbbStyle;
    private JComboBox cbbSex;
    private JXHyperlink btnNewSex;
    private ImageSlide imageSlide;
    private JButton btnAddImage;
    private JLabel quantityImages;
    private JButton btnNext;
    private JButton btnPrevious;
    private JComboBox cbbBrand;
    private JXHyperlink btnNewBrand;
    private JXHyperlink btnNewStade;
    private JComboBox cbbDimention;
    private JXHyperlink btnNewDimention;
    private JComboBox cbbStade;
    private JXHyperlink btnRemoveImage;
    private Product product;
    private final boolean update;
    private PresentationsAbstractModel model;
    private Style style=new Style();
    private ActionListener actionListener;

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
                loadNewPresentation();
            }
        });
        btnAddImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAddNewImage();
            }
        });
        btnNewBrand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewBrand();
            }
        });
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toPrevious();
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSlide.toNext();
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
        btnNewStade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewStade();
            }
        });
        btnNewDimention.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewDimention();
            }
        });
        btnRemoveImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeImage();
            }
        });
    }
    private void removeImage(){
        if(!product.getImages().isEmpty()){
            boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Eliminar imagen",JOptionPane.YES_NO_OPTION)==0;
            if(si){
                product.getImages().remove(imageSlide.getIndexPosition());
                product.getIcons().remove(imageSlide.getIndexPosition());
                loadImages();
                imageSlide.toNext();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Imagen eliminada");
            }

        }
    }
    private void loadNewDimention(){
        DDimention dDimention=new DDimention(new Dimention());
        dDimention.setVisible(true);
    }
    private void loadNewStade(){
        DStade dStade=new DStade(new Stade());
        dStade.setVisible(true);
    }
    private void loadAddNewImage() {
        DCrop dCrop=new DCrop();
        dCrop.setVisible(true);
        BufferedImage bufferedImage=DCrop.imageSelected;
        if(bufferedImage!=null){
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", os);
                String nameImage=product.getId() + "-" + product.getImages().size()+"."+"png";
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                if(Utilities.newImage(inputStream, nameImage)){
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ÉXITO","Imagen guardada");
                    product.getImages().add(nameImage);
                    product.getIcons().add(new ImageIcon(Utilities.getImage(nameImage)));
                    if(update){
                        product.save();
                    }
                    imageSlide.addImage(new ImageIcon(Utilities.getImage(nameImage)));
                    loadQuantityImages();
                    imageSlide.toNext();
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Ocurrió un error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadNewPresentation(){
        DPresentation dPresentation=new DPresentation(new Presentation(product));
        dPresentation.setVisible(true);
    }
    private void loadNewSex(){
        DSex dSex=new DSex(new Sex());
        dSex.setVisible(true);
    }
    private void loadNewBrand(){
        DBrand dBrand=new DBrand(new Brand());
        dBrand.setVisible(true);
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
            if(!update){
                cbbCategory.setSelectedItem(style.getCategory());
            }
        }else{
            if(!update){
                style=new Style();
                cbbCategory.setSelectedIndex(-1);
            }
        }
    }
    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        actionListener= e -> model.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
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
        cbbDimention.setModel(new DefaultComboBoxModel(FPrincipal.dimentions));
        cbbDimention.setRenderer(new Dimention.ListCellRenderer());
        cbbStade.setModel(new DefaultComboBoxModel(FPrincipal.stades));
        cbbStade.setRenderer(new Stade.ListCellRenderer());
        if(!update){
            cbbStyle.setModel(new DefaultComboBoxModel(FPrincipal.styles));
            AutoCompleteDecorator.decorate(cbbStyle);
        }
        cbbStyle.setRenderer(new Style.ListCellRenderer());
        cbbCategory.setModel(new DefaultComboBoxModel(FPrincipal.categories));
        cbbCategory.setRenderer(new Category.ListCellRenderer());
        cbbSize.setModel(new DefaultComboBoxModel(FPrincipal.sizes));
        cbbSize.setRenderer(new Size.ListCellRenderer());
        cbbSex.setModel(new DefaultComboBoxModel(FPrincipal.sexs));
        cbbSex.setRenderer(new Sex.ListCellRenderer());
        cbbColor.setModel(new DefaultComboBoxModel(FPrincipal.colors));
        cbbColor.setRenderer(new Color.ListCellRenderer());
        cbbBrand.setModel(new DefaultComboBoxModel(FPrincipal.brands));
        cbbBrand.setRenderer(new Brand.ListCellRenderer());
        cbbStyle.setSelectedIndex(-1);
        cbbCategory.setSelectedIndex(-1);
        cbbSize.setSelectedIndex(-1);
        cbbColor.setSelectedIndex(-1);
        cbbSex.setSelectedIndex(-1);
        cbbBrand.setSelectedIndex(-1);
        cbbDimention.setSelectedIndex(-1);
        cbbStade.setSelectedIndex(-1);
    }
    private void load(){
        cbbStyle.setSelectedItem(product.getStyle());
        cbbCategory.setSelectedItem(product.getStyle().getCategory());
        cbbColor.setSelectedItem(product.getColor());
        cbbSize.setSelectedItem(product.getSize());
        cbbSex.setSelectedItem(product.getSex());
        cbbBrand.setSelectedItem(product.getBrand());
        cbbStade.setSelectedItem(product.getStade());
        cbbDimention.setSelectedItem(product.getDimention());
        loadImages();
        style=product.getStyle();
    }

    private void loadImages(){
        imageSlide.clear();
        product.getIcons().forEach(icon->{
            if(icon!=null){
                imageSlide.addImage(icon);
            }
        });
        loadQuantityImages();
    }

    private void loadQuantityImages(){
        quantityImages.setText(String.valueOf(product.getIcons().size()));
    }

    private void loadTable(){
        model=new PresentationsAbstractModel(product.getPresentations());
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        PresentationCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorPresentation(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorPresentation(true));
    }
    private void onSave(){
        if((cbbStyle.getSelectedItem() instanceof Style)){
            style= (Style) cbbStyle.getSelectedItem();
        }else{
            style.setName(String.valueOf(cbbStyle.getEditor().getItem()));
        }
        style.setCategory((Category) cbbCategory.getSelectedItem());
        product.setStyle(style);
        product.setColor((Color) cbbColor.getSelectedItem());
        product.setSex((Sex) cbbSex.getSelectedItem());
        product.setSize((Size) cbbSize.getSelectedItem());
        product.setStade((Stade) cbbStade.getSelectedItem());
        product.setBrand((Brand) cbbBrand.getSelectedItem());
        product.setDimention((Dimention) cbbDimention.getSelectedItem());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(product);
        constraintViolationSet.addAll(ProgramValidator.loadViolations(style));
        if(constraintViolationSet.isEmpty()){
            if(style.getId()==null){
                FPrincipal.styles.add(style);
            }
            style.save();
            product.save();
            if(!update){
                style.getProducts().add(product);
                FPrincipal.products.add(product);
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
                product=new Product();
                clear();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Producto registrado");
            }else{
                Utilities.updateDialog();
                Utilities.getTabbedPane().updateTab();
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
        cbbBrand.setSelectedIndex(-1);
        cbbSize.setSelectedIndex(-1);
        cbbColor.setSelectedIndex(-1);
        cbbSex.setSelectedIndex(-1);
        cbbDimention.setSelectedIndex(-1);
        cbbStade.setSelectedIndex(-1);
        loadStyle();
    }
    private void onHecho(){
        if(update){
            product.refresh();
        }
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }

}
