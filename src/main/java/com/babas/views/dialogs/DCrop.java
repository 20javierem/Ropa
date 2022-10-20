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
    public static BufferedImage imageSelectedx200;
    public static BufferedImage imageSelectedx400;

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
                loadImagesScaled();
            }
        });
    }
    private void loadImagesScaled(){
        BufferedImage bufferedImage=((ImagePanel)panelImagen).getImageSelected();

        int width=bufferedImage.getWidth();
        int height=bufferedImage.getHeight();
        if(width>200||height>200){
            double percen= Math.min(200.00/width,200.00/height);
            width= (int) (percen*width);
            height=(int) (percen*height);
        }
        // Draw the image on to the buffered image
        Image image=bufferedImage.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
        BufferedImage bufferedImage1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);;
        Graphics2D bGr1 = bufferedImage1.createGraphics();
        bGr1.drawImage(image, 0, 0, null);
        bGr1.dispose();
        imageSelectedx200=bufferedImage1;

        width=bufferedImage.getWidth();
        height=bufferedImage.getHeight();
        if(width>400||height>400){
            double percen= Math.min(400.00/width,400.00/height);
            width= (int) (percen*width);
            height=(int) (percen*height);
        }
        // Draw the image on to the buffered image
        Image image2=bufferedImage.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
        BufferedImage bufferedImage2 = new BufferedImage(image2.getWidth(null), image2.getHeight(null), BufferedImage.TYPE_INT_ARGB);;
        Graphics2D bGr2 = bufferedImage2.createGraphics();
        bGr2.drawImage(image2, 0, 0, null);
        bGr2.dispose();
        imageSelectedx400=bufferedImage2;

        onDispose();
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
        int resultado = selectorArchivos.showOpenDialog(Utilities.getJFrame());
        if (resultado != 0) {
            JOptionPane.showMessageDialog(selectorArchivos, "No seleccionó la imagen", "", JOptionPane.WARNING_MESSAGE);
            return null;
        } else {
            imagenProducto = selectorArchivos.getSelectedFile();
            return imagenProducto.getAbsolutePath();
        }
    }
    private void onDispose(){
        dispose();
    }
    private void initComponents(){
        setContentPane(contentPane);
        imageSelectedx200=null;
        imageSelectedx400=null;
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
