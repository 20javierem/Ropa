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

}
