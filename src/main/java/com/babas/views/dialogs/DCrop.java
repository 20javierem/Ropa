package com.babas.views.dialogs;

import com.babas.custom.ImagePanel;
import com.babas.utilities.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DCrop extends JDialog {
    private JButton btnSave;
    private JPanel panelImagen;
    private JButton btnCancel;
    private JButton btnOpenFileChooser;
    private JPanel contentPane;
    public static BufferedImage imageSelected;

    public DCrop() {
        super(Utilities.getJFrame(),"Editar Logo",true);
        initComponents();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnCancel.addActionListener(e -> onDispose());
        btnOpenFileChooser.addActionListener(e -> loadImage());
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSelected=((ImagePanel)panelImagen).getImageSelected();
                onDispose();
            }
        });
    }

    public void loadImage(){
        String ruta=nuevaImagen();
        if(ruta!=null){
            ((ImagePanel)panelImagen).loadImage(ruta);
        }
    }

    public static String nuevaImagen(){
        File imagenProducto;
        JFileChooser selectorArchivos = new JFileChooser();
        FileFilter filtro1=new FileNameExtensionFilter("*.images","png","jpg","jpeg");
        selectorArchivos.addChoosableFileFilter(filtro1);
        int resultado = selectorArchivos.showOpenDialog(null);
        if (resultado != 0) {
            JOptionPane.showMessageDialog(selectorArchivos, "No seleccionó la imagen", "", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        else{
            imagenProducto = selectorArchivos.getSelectedFile();
            return imagenProducto.getAbsolutePath();
        }
    }
    private void onDispose(){
        dispose();
    }
    private void initComponents(){
        setContentPane(contentPane);
        imageSelected=null;
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        try {
            panelImagen=new ImagePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
