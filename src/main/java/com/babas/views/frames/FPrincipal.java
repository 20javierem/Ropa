package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.*;
import com.babas.custom.*;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.*;
import com.babas.views.menus.*;
import com.babas.views.tabs.TabBoxSesion;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FPrincipal extends JFrame {
    private JPanel contentPane;
    private JMenu btnMenuInicio;
    private JButton btnNewSale;
    private JButton btnRecordSales;
    private JButton btnCatalogue;
    private JButton btnRecordTransfers;
    private JButton btnActualizar;
    private JLabel lblCenter;
    private JLabel lblRigth;
    private JLabel lblLeft;
    private JSplitPane splitPane;
    private JPanel panelControles;
    private FlatToggleButton btnInventary;
    private FlatToggleButton btnSales;
    private FlatButton btnExit;
    private FlatToggleButton btnManagement;
    private FlatToggleButton btnAjustes;
    private FlatToggleButton btnAdministrador;
    private JPanel panelMenus;
    private JLabel lblUsuario;
    private TabbedPane tabbedPane;
    private JPanel cPane;
    private FlatToggleButton btnTraslades;
    private FlatToggleButton btnReserves;
    private FlatToggleButton btnRentals;
    private JMenu btnMenuBox;
    private JButton btnBoxSesion;
    private JButton btnNewReserve;
    private JButton btnNewRental;
    private FlatToggleButton btnBoxes;
    private FlatButton btnSettings;
    private FlatButton btnNotify;
    private MenuSales menuSales;
    private MenuManage menuManage;
    private MenuTraslade menuTraslade;
    private MenuReserves menuReserves;
    private MenuRentals menuRentals;
    private MenuBoxes menuBoxes;
    public static Vector<Branch> branchs = new Vector<>();
    public static Vector<Branch> branchesWithAll = new Vector<>();
    public static Vector<User> users = Users.getActives();
    public static Vector<User> usersWithAll = new Vector<>();
    public static Vector<Product> products = new Vector<>();
    public static Vector<Category> categories = new Vector<>();
    public static Vector<Category> categoriesWithAll = new Vector<>();
    public static Vector<Color> colors = new Vector<>();
    public static Vector<Stade> stades = new Vector<>();
    public static Vector<Stade> stadesWithAll = new Vector<>();
    public static Vector<Dimention> dimentions = new Vector<>();
    public static Vector<Dimention> dimentionsWithAll = new Vector<>();
    public static Vector<Color> colorsWithAll = new Vector<>();
    public static Vector<Brand> brands = new Vector<>();
    public static Vector<Brand> brandsWithAll = new Vector<>();
    public static Vector<Size> sizes = new Vector<>();
    public static Vector<Size> sizesWithAll = new Vector<>();
    public static Vector<Sex> sexs = new Vector<>();
    public static Vector<Sex> sexsWithAll = new Vector<>();
    public static Vector<Style> styles = new Vector<>();
    public static List<Transfer> transfersOnWait = new ArrayList<>();
    public static List<Rental> rentalsActives = new ArrayList<>();
    public static List<Reserve> reservesActives = new ArrayList<>();
    public static Vector<Permission> groupPermnitions = new Vector<>();

    private JPopupMenu pop_up = new JPopupMenu();

    public FPrincipal() {
        $$$setupUI$$$();
        init();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuSales();
            }
        });
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadNewSale();
            }
        });
        btnRecordSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadRecordSales();
            }
        });
        btnManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuManage();
            }
        });
        btnTraslades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuTransfer();
            }
        });
        btnReserves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuReserves();
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        btnRecordTransfers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuTraslade.loadRecordTraslades();
            }
        });
        btnRentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuRentals();
            }
        });
        btnBoxSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBoxSesion();
            }
        });
        btnCatalogue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadCatalogue();
            }
        });
        btnNewRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuRentals.loadNewRental();
            }
        });
        btnNewReserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuReserves.loadNewReserve();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitSession();
            }
        });
        btnBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuBoxes();
            }
        });
        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSettingUser();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pop_up.show(btnExit, -btnExit.getX(), btnExit.getVisibleRect().y - 58);
            }
        });
        btnNotify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDialogTransfers();
            }
        });
    }

    private void loadSettingUser() {
        DUser dUser = new DUser(true, Babas.user);
        dUser.setVisible(true);
    }

    private void reloadData() {
        Utilities.consult = true;
        Babas.user.getPermitions().refresh();
        btnActualizar.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        branchs.forEach(Babas::refresh);
        branchs.forEach(branch -> branch.getStocks().forEach(Babas::refresh));
        users.forEach(Babas::refresh);
        products.forEach(Babas::refresh);
        categories.forEach(Babas::refresh);
        colors.forEach(Babas::refresh);
        brands.forEach(Babas::refresh);
        sizes.forEach(Babas::refresh);
        sexs.forEach(Babas::refresh);
        styles.forEach(Babas::refresh);
        tabbedPane.updateTab();
        loadTransferOnWait();
        groupPermnitions.forEach(Babas::refresh);
        products.forEach(product -> product.getIconsx200(true));
        products.forEach(product -> product.getIconsx400(true));
        loadPermisses();
        Babas.company.refresh();
        if (!Babas.company.getLogo().isBlank()) {
            if (Utilities.openConection()) {
                Utilities.downloadLogo(Babas.company.getLogo());
            }
        }
        btnActualizar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void loadDialogTransfers() {
        if (!transfersOnWait.isEmpty()) {
            DTransfersOnWait dTransfersOnWait = new DTransfersOnWait(transfersOnWait);
            dTransfersOnWait.setVisible(true);
        }
    }

    private void exitSession() {
        boolean si = JOptionPane.showConfirmDialog(Utilities.getJFrame(), "¿Está seguro?", "Cerrar sesión", JOptionPane.YES_NO_OPTION) == 0;
        if (si) {
            System.out.println("rapido");
            FLogin fLogin = new FLogin();
            this.dispose();
            System.out.println("lento");
            fLogin.setVisible(true);
        }
    }

    private void suspendSession() {
        setVisible(false);
        FLogin fLogin = new FLogin(this);
        fLogin.setVisible(true);
    }

    private void loadMenuItems() {
        pop_up.setBorder(BorderFactory.createEmptyBorder());
        JMenuItem menuSettings = new JMenuItem("Configuraciones");
        JMenuItem menuCompany = new JMenuItem("Compañia");
        JMenuItem menuBox = new JMenuItem("Apertura/Cierre de caja");
        JMenuItem menuShowBox = new JMenuItem("Ver movimientos de caja");
        menuSettings.setIcon(new ImageIcon(App.class.getResource("icons/x16/settings.png")));
        menuCompany.setIcon(new ImageIcon(App.class.getResource("icons/x16/settings.png")));
        menuBox.setIcon(new ImageIcon(App.class.getResource("icons/x16/caja-registradora.png")));
        menuShowBox.setIcon(new ImageIcon(App.class.getResource("icons/x16/caja-registradora.png")));
        menuCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCompany();
            }
        });
        menuSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSettings();
            }
        });
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBox();
            }
        });
        menuShowBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBoxSesion();
            }
        });
        btnMenuInicio.add(menuSettings);
        btnMenuInicio.add(menuCompany);
        btnMenuBox.add(menuBox);
        btnMenuBox.add(menuShowBox);
        menuCompany.setEnabled(Babas.user.getPermitions().isManageCompany());
    }

    private void loadMenuExit() {
        JMenuItem suspendSession = new JMenuItem("Suspender sesión");
        JMenuItem closeSession = new JMenuItem("Cerrar sesión");
        suspendSession.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/moon.svg")));
        closeSession.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/exit.svg")));
        pop_up.add(suspendSession);
        pop_up.add(closeSession);
        suspendSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suspendSession();
            }
        });
        closeSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitSession();
            }
        });
    }

    private void loadBoxSesion() {
        if (Babas.boxSession.getId() != null) {
            if (tabbedPane.indexOfTab("Caja actual") == -1) {
                TabBoxSesion tabBoxSesion = new TabBoxSesion(Babas.boxSession);
                tabbedPane.addTab(tabBoxSesion.getTabPane().getTitle(), tabBoxSesion.getTabPane());
            }
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Caja actual"));
        } else {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.INFO, Notify.Location.TOP_CENTER, "Mensaje", "No aperturó una caja");
        }
    }

    private void loadBox() {
        DBoxSesion dBoxSesion = new DBoxSesion();
        dBoxSesion.setVisible(true);
    }

    private void loadSettings() {
        DSettings dSettings = new DSettings(this);
        dSettings.setVisible(true);
    }

    private void loadCompany() {
        DCompany dCompany = new DCompany();
        dCompany.setVisible(true);
    }

    private void loadMenuSales() {
        splitPane.setRightComponent(menuSales.getContentPane());
    }

    private void loadMenuManage() {
        splitPane.setRightComponent(menuManage.getContentPane());
    }

    private void loadMenuTransfer() {
        splitPane.setRightComponent(menuTraslade.getContentPane());
    }

    private void loadMenuReserves() {
        splitPane.setRightComponent(menuReserves.getContentPane());
    }

    private void loadMenuRentals() {
        splitPane.setRightComponent(menuRentals.getContentPane());
    }

    private void loadMenuBoxes() {
        splitPane.setRightComponent(menuBoxes.getContentPane());
    }

    private void init() {
        setContentPane(contentPane);
        setTitle("Software-Tienda");
        Utilities.setJFrame(this);
        Utilities.setTabbedPane(tabbedPane);
        Utilities.setLblIzquierda(lblLeft);
        Utilities.setLblCentro(lblCenter);
        Utilities.setLblDerecha(lblRigth);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarCursores();
        pack();
        setLocationRelativeTo(null);
        loadLists();
        lblUsuario.setText("Usuario: " + Babas.user.getFirstName());
        menuSales = new MenuSales(tabbedPane);
        menuManage = new MenuManage(tabbedPane);
        menuTraslade = new MenuTraslade(tabbedPane);
        menuReserves = new MenuReserves(tabbedPane);
        menuRentals = new MenuRentals(tabbedPane);
        menuBoxes = new MenuBoxes(tabbedPane);
        setExtendedState(MAXIMIZED_BOTH);
        loadPermisses();
        loadMenuItems();
        loadMenuSales();
        loadMenuExit();
        loadTransferOnWait();
        btnActualizar.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/buildLoadChanges.svg")));
        btnSettings.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/settings.svg")));
        btnExit.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/exit.svg")));
        btnNotify.setIcon(new FlatSVGIcon(App.class.getResource("icons/svg/notification.svg")));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void loadPermisses() {
        btnCatalogue.setEnabled(Babas.user.getPermitions().isShowCatalogue());
        btnNewSale.setEnabled(Babas.user.getPermitions().isNewSale());
        btnNewRental.setEnabled(Babas.user.getPermitions().isNewSale());
        btnNewReserve.setEnabled(Babas.user.getPermitions().isNewSale());
        btnBoxSesion.setEnabled(Babas.user.getPermitions().isNewSale());
        btnRecordSales.setEnabled(Babas.user.getPermitions().isRecordSales());
        btnRecordTransfers.setEnabled(Babas.user.getPermitions().isRecordTransfers());
        btnNotify.setEnabled(Babas.user.getPermitions().isAceptTransfer());
    }

    public void loadTransferOnWait() {
        transfersOnWait.clear();
        final int[] count = {0};
        Babas.user.getBranchs().forEach(branch -> branch.getTransfers().forEach(transfer -> {
            if (Objects.equals(transfer.getDestiny().getId(), branch.getId())) {
                if (transfer.getState() == 0) {
                    transfersOnWait.add(transfer);
                    count[0]++;
                }
            }
        }));
        if (count[0] > 0) {
            btnNotify.setVisible(true);
            if (count[0] > 9) {
                btnNotify.setText("+9");
            } else {
                btnNotify.setText(String.valueOf(count[0]));
            }
        } else {
            btnNotify.setVisible(false);
        }
    }

    private void loadLists() {
        branchs = Branchs.getActives();
        stades = Stades.getActives();
        stadesWithAll = new Vector<>(stades);
        stadesWithAll.add(0, new Stade("TODAS"));
        dimentions = Dimentions.getActives();
        dimentionsWithAll = new Vector<>(dimentions);
        dimentionsWithAll.add(0, new Dimention("TODAS"));
        branchesWithAll = new Vector<>(branchs);
        branchesWithAll.add(0, new Branch("TODAS"));
        categories = Categorys.getActives();
        categoriesWithAll = new Vector<>(categories);
        categoriesWithAll.add(0, new Category("TODAS"));
        colors = Colors.getActives();
        colorsWithAll = new Vector<>(colors);
        colorsWithAll.add(0, new Color("TODOS"));
        sizes = Sizes.getActives();
        sizesWithAll = new Vector<>(sizes);
        sizesWithAll.add(0, new Size("TODAS"));
        brands = Brands.getActives();
        brandsWithAll = new Vector<>(brands);
        brandsWithAll.add(0, new Brand("TODAS"));
        sexs = Sexs.getActives();
        sexsWithAll = new Vector<>(sexs);
        sexsWithAll.add(0, new Sex("TODOS"));
        products = Products.getActives();
        products.forEach(product -> product.getIconsx200(true));
        products.forEach(product -> product.getIconsx400(true));
        styles = Styles.getTodos();
        groupPermnitions = Permissions.getGroups();
        Babas.user.getBranchs().forEach(branch -> rentalsActives.addAll(Rentals.getActives(branch)));
        Babas.user.getBranchs().forEach(branch -> reservesActives.addAll(Reserves.getActives(branch)));
    }

    private void cargarCursores() {
        btnAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdministrador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalogue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRecordTransfers.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRecordSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTraslades.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReserves.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRentals.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBoxSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewRental.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewReserve.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBoxes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotify.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panelControles = new CustomPane(2);
        panelControles.updateUI();
        cPane = new CustomPane(2);
        cPane.updateUI();
        btnSales = new CToggleButton();
        btnInventary = new CToggleButton();
        btnManagement = new CToggleButton();
        btnAjustes = new CToggleButton();
        btnAdministrador = new CToggleButton();
        btnTraslades = new CToggleButton();
        btnReserves = new CToggleButton();
        btnRentals = new CToggleButton();
        btnBoxes = new CToggleButton();
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
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), 0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, 0));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JMenuBar menuBar1 = new JMenuBar();
        menuBar1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(menuBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnMenuInicio = new JMenu();
        btnMenuInicio.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        btnMenuInicio.setBackground(new java.awt.Color(-1049857));
        btnMenuInicio.setBorderPainted(false);
        btnMenuInicio.setForeground(new java.awt.Color(-16777216));
        btnMenuInicio.setText("Inicio");
        menuBar1.add(btnMenuInicio, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        menuBar1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnMenuBox = new JMenu();
        btnMenuBox.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        btnMenuBox.setBackground(new java.awt.Color(-1049857));
        btnMenuBox.setBorderPainted(false);
        btnMenuBox.setForeground(new java.awt.Color(-16777216));
        btnMenuBox.setText("Caja");
        menuBar1.add(btnMenuBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        panel1.add(toolBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        btnNewSale = new JButton();
        btnNewSale.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/nuevaVenta.png")));
        btnNewSale.setText("Nueva venta");
        toolBar1.add(btnNewSale);
        btnNewRental = new JButton();
        btnNewRental.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnNewRental.setText("Nuevo alquiler");
        toolBar1.add(btnNewRental);
        btnNewReserve = new JButton();
        btnNewReserve.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnNewReserve.setText("Nueva reserva");
        toolBar1.add(btnNewReserve);
        btnCatalogue = new JButton();
        btnCatalogue.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/catalogue.png")));
        btnCatalogue.setText("Catálogo");
        toolBar1.add(btnCatalogue);
        btnBoxSesion = new JButton();
        btnBoxSesion.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/caja-registradora.png")));
        btnBoxSesion.setText("Movimientos de caja");
        toolBar1.add(btnBoxSesion);
        btnRecordSales = new JButton();
        btnRecordSales.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/historialVentas.png")));
        btnRecordSales.setText("Historial de ventas");
        toolBar1.add(btnRecordSales);
        btnRecordTransfers = new JButton();
        btnRecordTransfers.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/recordTransactions.png")));
        btnRecordTransfers.setText("Historial de transferencias");
        toolBar1.add(btnRecordTransfers);
        final Spacer spacer2 = new Spacer();
        toolBar1.add(spacer2);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setMaximumSize(new Dimension(3, 2147483647));
        panel2.setOpaque(false);
        toolBar1.add(panel2);
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        panel2.add(separator1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnActualizar = new JButton();
        btnActualizar.setBorderPainted(true);
        btnActualizar.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/refresh.png")));
        btnActualizar.setMargin(new Insets(0, 0, 0, 0));
        btnActualizar.setMaximumSize(new Dimension(38, 38));
        btnActualizar.setMinimumSize(new Dimension(38, 38));
        btnActualizar.setPreferredSize(new Dimension(38, 38));
        btnActualizar.setText("");
        toolBar1.add(btnActualizar);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 5, new Insets(0, 10, 0, 10), -1, -1));
        panel3.setBackground(new java.awt.Color(-16777216));
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 28), new Dimension(-1, 28), new Dimension(-1, 28), 0, false));
        lblLeft = new JLabel();
        Font lblLeftFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblLeft.getFont());
        if (lblLeftFont != null) lblLeft.setFont(lblLeftFont);
        lblLeft.setForeground(new java.awt.Color(-1));
        lblLeft.setText("");
        panel3.add(lblLeft, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblCenter = new JLabel();
        Font lblCenterFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblCenter.getFont());
        if (lblCenterFont != null) lblCenter.setFont(lblCenterFont);
        lblCenter.setForeground(new java.awt.Color(-1));
        lblCenter.setText("");
        panel3.add(lblCenter, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblRigth = new JLabel();
        Font lblRigthFont = this.$$$getFont$$$(null, Font.BOLD, -1, lblRigth.getFont());
        if (lblRigthFont != null) lblRigth.setFont(lblRigthFont);
        lblRigth.setForeground(new java.awt.Color(-1));
        lblRigth.setText("Monto caja: S/0.00");
        panel3.add(lblRigth, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), 0, 0));
        panel4.setBackground(new java.awt.Color(-4013374));
        contentPane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(130, -1), null, 0, false));
        splitPane = new JSplitPane();
        splitPane.setDividerSize(0);
        panel4.add(splitPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 200), null, 0, false));
        panelControles.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, 0));
        splitPane.setLeftComponent(panelControles);
        final JSeparator separator2 = new JSeparator();
        panelControles.add(separator2, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 4, new Insets(2, 0, 2, 0), -1, -1));
        panel5.setOpaque(false);
        panelControles.add(panel5, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel5.add(spacer6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSettings = new FlatButton();
        btnSettings.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnSettings.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x16/iniciar-sesion.png")));
        btnSettings.setMaximumSize(new Dimension(38, 38));
        btnSettings.setMinimumSize(new Dimension(38, 38));
        btnSettings.setPreferredSize(new Dimension(38, 38));
        btnSettings.setText("");
        panel5.add(btnSettings, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnExit = new FlatButton();
        btnExit.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnExit.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x16/iniciar-sesion.png")));
        btnExit.setMaximumSize(new Dimension(38, 38));
        btnExit.setMinimumSize(new Dimension(38, 38));
        btnExit.setPreferredSize(new Dimension(38, 38));
        btnExit.setText("");
        panel5.add(btnExit, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panelControles.add(spacer7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(2, 0, 2, 0), -1, -1));
        panel6.setOpaque(false);
        panelControles.add(panel6, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNotify = new FlatButton();
        btnNotify.setButtonType(FlatButton.ButtonType.toolBarButton);
        btnNotify.setHorizontalTextPosition(0);
        btnNotify.setText("");
        btnNotify.setVisible(true);
        panel6.add(btnNotify, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(32, 32), null, new Dimension(40, 40), 0, false));
        final Spacer spacer8 = new Spacer();
        panel6.add(spacer8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel6.add(spacer9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSales.setButtonType(FlatButton.ButtonType.tab);
        btnSales.setFocusable(false);
        Font btnSalesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnSales.getFont());
        if (btnSalesFont != null) btnSales.setFont(btnSalesFont);
        btnSales.setHorizontalTextPosition(0);
        btnSales.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/ventasSelected.png")));
        btnSales.setIconTextGap(-2);
        btnSales.setMargin(new Insets(10, 5, 10, 5));
        btnSales.setRolloverEnabled(false);
        btnSales.setSelected(true);
        btnSales.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/ventasDefault.png")));
        btnSales.setTabUnderlineHeight(5);
        btnSales.setTabUnderlinePlacement(2);
        btnSales.setText("Ventas");
        btnSales.setVerticalTextPosition(3);
        panelControles.add(btnSales, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnManagement.setButtonType(FlatButton.ButtonType.tab);
        btnManagement.setFocusable(false);
        Font btnManagementFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnManagement.getFont());
        if (btnManagementFont != null) btnManagement.setFont(btnManagementFont);
        btnManagement.setHorizontalTextPosition(0);
        btnManagement.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/gestionarSelected.png")));
        btnManagement.setIconTextGap(-2);
        btnManagement.setMargin(new Insets(10, 5, 10, 5));
        btnManagement.setRolloverEnabled(false);
        btnManagement.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/gestionarDefault.png")));
        btnManagement.setTabUnderlineHeight(5);
        btnManagement.setTabUnderlinePlacement(2);
        btnManagement.setText("Gestionar");
        btnManagement.setVerticalTextPosition(3);
        panelControles.add(btnManagement, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnReserves.setButtonType(FlatButton.ButtonType.tab);
        btnReserves.setFocusable(false);
        Font btnReservesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnReserves.getFont());
        if (btnReservesFont != null) btnReserves.setFont(btnReservesFont);
        btnReserves.setHorizontalTextPosition(0);
        btnReserves.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnReserves.setIconTextGap(-2);
        btnReserves.setMargin(new Insets(10, 5, 10, 5));
        btnReserves.setRolloverEnabled(false);
        btnReserves.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveDefault.png")));
        btnReserves.setTabUnderlineHeight(5);
        btnReserves.setTabUnderlinePlacement(2);
        btnReserves.setText("Reservas");
        btnReserves.setVerticalTextPosition(3);
        panelControles.add(btnReserves, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnTraslades.setButtonType(FlatButton.ButtonType.tab);
        btnTraslades.setFocusable(false);
        Font btnTrasladesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnTraslades.getFont());
        if (btnTrasladesFont != null) btnTraslades.setFont(btnTrasladesFont);
        btnTraslades.setHorizontalTextPosition(0);
        btnTraslades.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/transferirDefault.png")));
        btnTraslades.setIconTextGap(-2);
        btnTraslades.setMargin(new Insets(10, 5, 10, 5));
        btnTraslades.setRolloverEnabled(false);
        btnTraslades.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/transferirSelected.png")));
        btnTraslades.setTabUnderlineHeight(5);
        btnTraslades.setTabUnderlinePlacement(2);
        btnTraslades.setText("Traslados");
        btnTraslades.setVerticalTextPosition(3);
        panelControles.add(btnTraslades, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnRentals.setButtonType(FlatButton.ButtonType.tab);
        btnRentals.setFocusable(false);
        Font btnRentalsFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnRentals.getFont());
        if (btnRentalsFont != null) btnRentals.setFont(btnRentalsFont);
        btnRentals.setHorizontalTextPosition(0);
        btnRentals.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveSelected.png")));
        btnRentals.setIconTextGap(-2);
        btnRentals.setMargin(new Insets(10, 5, 10, 5));
        btnRentals.setRolloverEnabled(false);
        btnRentals.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/reserveDefault.png")));
        btnRentals.setTabUnderlineHeight(5);
        btnRentals.setTabUnderlinePlacement(2);
        btnRentals.setText("Alquileres");
        btnRentals.setVerticalTextPosition(3);
        panelControles.add(btnRentals, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnBoxes.setButtonType(FlatButton.ButtonType.tab);
        btnBoxes.setFocusable(false);
        Font btnBoxesFont = this.$$$getFont$$$(null, Font.BOLD, -1, btnBoxes.getFont());
        if (btnBoxesFont != null) btnBoxes.setFont(btnBoxesFont);
        btnBoxes.setHorizontalTextPosition(0);
        btnBoxes.setIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/boxSelected.png")));
        btnBoxes.setIconTextGap(-2);
        btnBoxes.setMargin(new Insets(10, 5, 10, 5));
        btnBoxes.setRolloverEnabled(false);
        btnBoxes.setSelectedIcon(new ImageIcon(getClass().getResource("/com/babas/icons/x32/boxDefault.png")));
        btnBoxes.setTabUnderlineHeight(5);
        btnBoxes.setTabUnderlinePlacement(2);
        btnBoxes.setText("Cajas");
        btnBoxes.setVerticalTextPosition(3);
        panelControles.add(btnBoxes, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelMenus = new JPanel();
        panelMenus.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(panelMenus);
        cPane.setLayout(new GridLayoutManager(1, 1, new Insets(5, 10, 5, 0), -1, -1));
        panel4.add(cPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 33), new Dimension(-1, 33), new Dimension(-1, 33), 0, false));
        lblUsuario = new JLabel();
        lblUsuario.setForeground(new java.awt.Color(-1));
        lblUsuario.setText("Usuario: javier");
        cPane.add(lblUsuario, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel7, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tabbedPane = new TabbedPane();
        tabbedPane.setTabLayoutPolicy(1);
        panel7.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(btnSales);
        buttonGroup.add(btnManagement);
        buttonGroup.add(btnTraslades);
        buttonGroup.add(btnReserves);
        buttonGroup.add(btnRentals);
        buttonGroup.add(btnBoxes);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
