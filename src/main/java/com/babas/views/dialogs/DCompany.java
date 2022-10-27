package com.babas.views.dialogs;

import com.babas.models.Brand;
import com.babas.models.Company;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class DCompany extends JDialog {
    private JPanel contentPane;
    private FlatButton btnHecho;
    private FlatButton btnSave;
    private FlatTextField txtBusinessName;
    private JTextArea txtSlogan;
    private FlatTextField txtRuc;
    private FlatTextField txtTradeName;
    private FlatTextField txtFiscalAdress;
    private JLabel lblLogo;
    private FlatTextField txtLogo;
    private Company company;

    public DCompany() {
        super(Utilities.getJFrame(), "Actualizar datos de la empresa", true);
        this.company=Babas.company;
        init();
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
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadCrop();
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
    }
    private void onSave(){
        if(Babas.company==null){
            Babas.company=new Company();
            company=Babas.company;
        }
        if(company.getLogo()==null){
            company.setLogo("");
        }
        company.setBusinessName(txtBusinessName.getText().trim());
        company.setDirectionPrincipal(txtFiscalAdress.getText().trim());
        company.setRuc(txtRuc.getText().trim());
        company.setTradeName(txtTradeName.getText().trim());
        company.setSlogan(txtSlogan.getText().trim());
        Set<ConstraintViolation<Object>> constraintViolationSet= ProgramValidator.loadViolations(company);
        if(constraintViolationSet.isEmpty()){
            company.save();
            if(Babas.company.getId()==null){
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Datos registrados");
            }else{
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Datos actualizados");
            }
            onHecho();
        }else{
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }
    private void loadCrop(){
        DCrop dCrop=new DCrop();
        dCrop.setVisible(true);
        BufferedImage bufferedImage=DCrop.imageSelectedx400;
        if(bufferedImage!=null){
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", os);
                String nameImage="logoCompany.png";
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                if(Utilities.newImage(inputStream, nameImage)){
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER,"ÉXITO","Imagen guardada");
                    company.setLogo(nameImage);
                    Image image=Utilities.getImage(nameImage);
                    if(image!=null){
                        image=image.getScaledInstance(lblLogo.getWidth(),lblLogo.getHeight(),Image.SCALE_SMOOTH);
                        Icon icon=new ImageIcon(image);
                        lblLogo.setIcon(icon);
                    }else{
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Ocurrió un error");
                    }
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Ocurrió un error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        if(company!=null){
            load();
        }else{
            btnSave.setText("Registrar");
            btnHecho.setText("Cancelar");
        }
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void load(){
        txtRuc.setText(company.getRuc());
        txtBusinessName.setText(company.getBusinessName());
        txtTradeName.setText(company.getTradeName());
        txtFiscalAdress.setText(company.getDirectionPrincipal());
        txtSlogan.setText(company.getSlogan());
        if(Utilities.iconCompany!=null){
            lblLogo.setIcon(Utilities.iconCompany);
        }
    }
    private void onHecho(){
       if(company.getId()!=null){
           company.refresh();
       }
       dispose();
    }
}
