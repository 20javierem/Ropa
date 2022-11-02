package com.babas.views.dialogs;

import com.babas.custom.ImagePanel;
import com.babas.utilities.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        super(Utilities.getJFrame(), "Editar Logo", true);
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

    private void loadImagesScaled() {
        BufferedImage bufferedImage = ((ImagePanel) panelImagen).getImageSelected();
        if (bufferedImage != null) {
            imageSelectedx200 = getImage(bufferedImage, 200.00);
            imageSelectedx400 = getImage(bufferedImage, 400.00);
        }
        onDispose();
    }

    private BufferedImage getImage(BufferedImage bufferedImage, double scal) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (width > scal || height > scal) {
            double percen = Math.min(scal / width, scal / height);
            width = (int) (percen * width);
            height = (int) (percen * height);
        }
        // Draw the image on to the buffered image
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr1 = bufferedImage1.createGraphics();
        bGr1.drawImage(image, 0, 0, null);
        bGr1.dispose();
        return bufferedImage1;
    }

    public void loadImage() {
        String ruta = nuevaImagen();
        if (ruta != null) {
            ((ImagePanel) panelImagen).loadImage(ruta);
        }
    }

    public static String nuevaImagen() {
        File imagenProducto;
        JFileChooser selectorArchivos = new JFileChooser();
        FileFilter filtro1 = new FileNameExtensionFilter("*.images", "png", "jpg", "jpeg");
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

    private void onDispose() {
        dispose();
    }

    private void initComponents() {
        setContentPane(contentPane);
        imageSelectedx200 = null;
        imageSelectedx400 = null;
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        try {
            panelImagen = new ImagePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
