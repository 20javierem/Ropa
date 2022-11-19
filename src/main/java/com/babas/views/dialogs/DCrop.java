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
        $$$setupUI$$$();
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
            imageSelectedx200 = getImage(bufferedImage, 200.00, 200.00);
            imageSelectedx400 = getImage(bufferedImage, 400.00, 400.00);
        }
        onDispose();
    }

    @Override
    public void setVisible(boolean b) {
        if (loadImage()) {
            super.setVisible(b);
        } else {
            dispose();
        }
    }

    public static BufferedImage getImage(BufferedImage bufferedImage, double scalWidth, double scalHeight) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (width > scalWidth || height > scalHeight) {
            double percen = Math.min(scalWidth / width, scalHeight / height);
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

    public boolean loadImage() {
        String ruta = nuevaImagen();
        if (ruta != null) {
            ((ImagePanel) panelImagen).loadImage(ruta);
            return true;
        }
        return false;
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("Guardar");
        panel1.add(btnSave, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnOpenFileChooser = new JButton();
        btnOpenFileChooser.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/carpeta.png")));
        btnOpenFileChooser.setIconTextGap(4);
        btnOpenFileChooser.setMargin(new Insets(2, 2, 2, 2));
        btnOpenFileChooser.setText("Seleccionar imagen");
        panel1.add(btnOpenFileChooser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCancel = new JButton();
        btnCancel.setText("Cancelar");
        panel1.add(btnCancel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), -1, -1));
        contentPane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel2.add(panelImagen, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(550, 550), new Dimension(550, 550), new Dimension(550, 550), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
