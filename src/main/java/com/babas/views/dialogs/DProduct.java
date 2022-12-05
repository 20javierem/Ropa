package com.babas.views.dialogs;

import com.babas.custom.ImageSlide;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorPresentation;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorStock;
import com.babas.utilitiesTables.tablesCellRendered.PresentationCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.StockCellRendered;
import com.babas.utilitiesTables.tablesModels.PresentationsAbstractModel;
import com.babas.utilitiesTables.tablesModels.StockProductAbstractModel;
import com.babas.validators.ProgramValidator;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;
import jakarta.validation.ConstraintViolation;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;

public class DProduct extends JDialog {
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
    private JTextField txtBarcode;
    private FlatTable tableStocks;
    private Product product;
    private final boolean update;
    private PresentationsAbstractModel model;
    private StockProductAbstractModel model1;
    private Style style = new Style();
    private ActionListener actionListener;

    public DProduct(Product product) {
        super(Utilities.getJFrame(), "Nuevo producto", true);
        this.product = product;
        update = product.getId() != null;
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

    private void removeImage() {
        if (!product.getImagesx200().isEmpty()) {
            boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Eliminar imagen", JOptionPane.YES_NO_OPTION) == 0;
            if (si) {
                Utilities.deleteImage(product.getImagesx200().get(imageSlide.getIndexPosition()));
                Utilities.deleteImage(product.getImagesx400().get(imageSlide.getIndexPosition()));
                product.getImagesx200().remove(imageSlide.getIndexPosition());
                product.getImagesx400().remove(imageSlide.getIndexPosition());
                loadImages();
                imageSlide.toNext();
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Imagen eliminada");
            }
        }
    }

    private void loadNewDimention() {
        DDimention dDimention = new DDimention(new Dimention());
        dDimention.setVisible(true);
    }

    private void loadNewStade() {
        DStade dStade = new DStade(new Stade());
        dStade.setVisible(true);
    }

    private void loadAddNewImage() {
        DCrop dCrop = new DCrop();
        dCrop.setVisible(true);
        BufferedImage bufferedImage1 = DCrop.imageSelectedx200;
        BufferedImage bufferedImage2 = DCrop.imageSelectedx400;
        if (bufferedImage1 != null) {
            try {
                ByteArrayOutputStream os1 = new ByteArrayOutputStream();
                ByteArrayOutputStream os2 = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage1, "png", os1);
                ImageIO.write(bufferedImage2, "png", os2);
                Long id = product.getId();
                if (id == null) {
                    Product lastProduct = FPrincipal.products.get(FPrincipal.products.size() - 1);
                    id = lastProduct != null ? lastProduct.getId() + 1 : 1;
                }
                String nameImage1 = id + "-" + product.getImagesx200().size() + "x200" + "." + "png";
                String nameImage2 = id + "-" + product.getImagesx200().size() + "x400" + "." + "png";
                InputStream inputStream1 = new ByteArrayInputStream(os1.toByteArray());
                InputStream inputStream2 = new ByteArrayInputStream(os2.toByteArray());
                if (Utilities.newImage(inputStream1, nameImage1, false)) {
                    product.getImagesx200().add(nameImage1);
                    product.getIconsx200(false).add(new ImageIcon(Utilities.getImage(nameImage1, false)));
                    if (update) {
                        product.save();
                    }
                    if (Utilities.newImage(inputStream2, nameImage2, false)) {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "ÉXITO", "Imagen guardada");
                        product.getImagesx400().add(nameImage2);
                        product.getIconsx400(false).add(new ImageIcon(Utilities.getImage(nameImage2, false)));
                        if (update) {
                            product.save();
                        }
                        imageSlide.addImage(new ImageIcon(Utilities.getImage(nameImage2, false)));
                        loadQuantityImages();
                        imageSlide.toNext();
                    } else {
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Ocurrió un error");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadNewPresentation() {
        DPresentation dPresentation = new DPresentation(new Presentation(product));
        dPresentation.setVisible(true);
    }

    private void loadNewSex() {
        DSex dSex = new DSex(new Sex());
        dSex.setVisible(true);
    }

    private void loadNewBrand() {
        DBrand dBrand = new DBrand(new Brand());
        dBrand.setVisible(true);
    }

    private void loadNewCategory() {
        DCategory dCategory = new DCategory(new Category());
        dCategory.setVisible(true);
    }

    private void loadNewColor() {
        DColor dColor = new DColor(new Color());
        dColor.setVisible(true);
    }

    private void loadNewSize() {
        DSize dSize = new DSize(new Size());
        dSize.setVisible(true);
    }

    private void loadStyle() {
        if (cbbStyle.getSelectedItem() instanceof Style) {
            style = (Style) cbbStyle.getSelectedItem();
            if (!update) {
                cbbCategory.setSelectedItem(style.getCategory());
                cbbCategory.setEnabled(false);
            }
        } else {
            if (!update) {
                style = new Style();
                cbbCategory.setSelectedIndex(-1);
                cbbCategory.setEnabled(true);
            }
        }
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnSave);
        actionListener = e -> model.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        loadCombos();
        if (update) {
            setTitle("Editar producto");
            btnHecho.setText("Cancelar");
            btnSave.setText("Guardar");
            load();
        }
        loadTable();
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void loadCombos() {
        cbbDimention.setModel(new DefaultComboBoxModel(FPrincipal.dimentions));
        cbbDimention.setRenderer(new Dimention.ListCellRenderer());
        cbbStade.setModel(new DefaultComboBoxModel(FPrincipal.stades));
        cbbStade.setRenderer(new Stade.ListCellRenderer());
        if (!update) {
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

    private void load() {
        cbbStyle.setSelectedItem(product.getStyle());
        cbbCategory.setSelectedItem(product.getStyle().getCategory());
        cbbColor.setSelectedItem(product.getColor());
        cbbSize.setSelectedItem(product.getSize());
        cbbSex.setSelectedItem(product.getSex());
        cbbBrand.setSelectedItem(product.getBrand());
        cbbStade.setSelectedItem(product.getStade());
        cbbDimention.setSelectedItem(product.getDimention());
        txtBarcode.setText(product.getBarcode());
        loadImages();
        style = product.getStyle();
    }

    private void loadImages() {
        imageSlide.clear();
        product.getIconsx400(false).forEach(icon -> {
            if (icon != null) {
                imageSlide.addImage(icon);
            }
        });
        loadQuantityImages();
    }

    private void loadQuantityImages() {
        quantityImages.setText(String.valueOf(product.getIconsx400(false).size()));
    }

    private void loadTable() {
        model = new PresentationsAbstractModel(product.getPresentations());
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        PresentationCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorPresentation(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorPresentation(true));

        model1 = new StockProductAbstractModel(product.getStocks());
        tableStocks.setModel(model1);
        UtilitiesTables.headerNegrita(tableStocks);
        tableStocks.removeColumn(tableStocks.getColumn("PRODUCTO"));
        tableStocks.removeColumn(tableStocks.getColumn("CÓDIGO"));
        StockCellRendered.setCellRenderer(tableStocks, null);
        tableStocks.getColumnModel().getColumn(tableStocks.getColumnCount() - 1).setCellEditor(new JButtonEditorStock());
    }

    private void onSave() {
        if ((cbbStyle.getSelectedItem() instanceof Style)) {
            style = (Style) cbbStyle.getSelectedItem();
        } else {
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
        product.setBarcode(txtBarcode.getText().trim());
        Set<ConstraintViolation<Object>> constraintViolationSet = ProgramValidator.loadViolations(product);
        constraintViolationSet.addAll(ProgramValidator.loadViolations(style));
        if (constraintViolationSet.isEmpty()) {
            if (!product.getPresentations().isEmpty()) {
                if (style.getId() == null) {
                    FPrincipal.styles.add(style);
                }
                style.save();
                product.save();
                if (!update) {
                    style.getProducts().add(product);
                    FPrincipal.products.add(product);
                    Utilities.updateDialog();
                    Utilities.getTabbedPane().updateTab();
                    product = new Product();
                    clear();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Producto registrado");
                } else {
                    Utilities.updateDialog();
                    Utilities.getTabbedPane().updateTab();
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER, "ÉXITO", "Producto actualizado");
                    onHecho();
                }
            } else {
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER, "ERROR", "Verifique el campo: Presentaciones");
            }
        } else {
            ProgramValidator.mostrarErrores(constraintViolationSet);
        }
    }

    private void clear() {
        cbbStyle.setSelectedIndex(-1);
        cbbCategory.setSelectedIndex(-1);
        cbbBrand.setSelectedIndex(-1);
        cbbSize.setSelectedIndex(-1);
        cbbColor.setSelectedIndex(-1);
        cbbSex.setSelectedIndex(-1);
        cbbDimention.setSelectedIndex(-1);
        cbbStade.setSelectedIndex(-1);
        txtBarcode.setText(null);
        loadStyle();
        loadTable();
        loadImages();
    }

    private void onHecho() {
        if (update) {
            product.refresh();
        }
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 10, 10));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tabbedPane = new JTabbedPane();
        tabbedPane.setEnabled(true);
        panel1.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(11, 3, new Insets(10, 10, 0, 10), -1, 5));
        tabbedPane.addTab("Producto", panel2);
        final JLabel label1 = new JLabel();
        label1.setText("Estilo:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Categoría:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbCategory = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        cbbCategory.setModel(defaultComboBoxModel1);
        panel2.add(cbbCategory, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Talla:");
        panel2.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbSize = new JComboBox();
        panel2.add(cbbSize, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Color:");
        panel2.add(label4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbColor = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        cbbColor.setModel(defaultComboBoxModel2);
        panel2.add(cbbColor, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(600, 200), null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Presentaciones", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        table = new FlatTable();
        scrollPane1.setViewportView(table);
        btnNewPresentation = new JButton();
        btnNewPresentation.setText("Nueva presentación");
        panel2.add(btnNewPresentation, new GridConstraints(10, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbStyle = new JComboBox();
        cbbStyle.setEditable(true);
        panel2.add(cbbStyle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Marca:");
        panel2.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbBrand = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        cbbBrand.setModel(defaultComboBoxModel3);
        panel2.add(cbbBrand, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Estado:");
        panel2.add(label6, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbStade = new JComboBox();
        panel2.add(cbbStade, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNewStade = new JXHyperlink();
        btnNewStade.setText("[+]");
        panel2.add(btnNewStade, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNewColor = new JXHyperlink();
        btnNewColor.setText("[+]");
        panel2.add(btnNewColor, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNewSize = new JXHyperlink();
        btnNewSize.setText("[+]");
        panel2.add(btnNewSize, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNewCategory = new JXHyperlink();
        btnNewCategory.setText("[+]");
        panel2.add(btnNewCategory, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNewBrand = new JXHyperlink();
        btnNewBrand.setText("[+]");
        panel2.add(btnNewBrand, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Dimensión:");
        panel2.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbDimention = new JComboBox();
        panel2.add(cbbDimention, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNewDimention = new JXHyperlink();
        btnNewDimention.setText("[+]");
        panel2.add(btnNewDimention, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Género:");
        panel2.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbbSex = new JComboBox();
        panel2.add(cbbSex, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNewSex = new JXHyperlink();
        btnNewSex.setText("[+]");
        panel2.add(btnNewSex, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Código de barras:");
        panel2.add(label9, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtBarcode = new JTextField();
        panel2.add(txtBarcode, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 0, 10), -1, 5));
        tabbedPane.addTab("Imagenes", panel3);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNext = new JButton();
        btnNext.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaRigth.png")));
        btnNext.setText("");
        panel4.add(btnNext, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPrevious = new JButton();
        btnPrevious.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/flechaLeft.png")));
        btnPrevious.setText("");
        panel4.add(btnPrevious, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnRemoveImage = new JXHyperlink();
        btnRemoveImage.setText("[Quitar]");
        panel4.add(btnRemoveImage, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnAddImage = new JButton();
        btnAddImage.setText("Agregar imagen");
        panel5.add(btnAddImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Imagenes en total:");
        panel5.add(label10, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quantityImages = new JLabel();
        quantityImages.setText("0");
        panel5.add(quantityImages, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        imageSlide = new ImageSlide();
        panel6.add(imageSlide, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 0, 10), -1, 5));
        tabbedPane.addTab("Stocks", panel7);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel7.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableStocks = new FlatTable();
        scrollPane2.setViewportView(tableStocks);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHecho = new JButton();
        btnHecho.setText("Hecho");
        panel8.add(btnHecho, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSave = new JButton();
        btnSave.setText("Registrar");
        panel8.add(btnSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
