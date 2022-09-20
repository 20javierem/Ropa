package com.babas.views.frames;

import com.babas.App;
import com.babas.controllers.*;
import com.babas.custom.CToggleButton;
import com.babas.custom.CustomPane;
import com.babas.custom.FondoPanel;
import com.babas.custom.TabbedPane;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DSettings;
import com.babas.views.menus.*;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class FPrincipal extends JFrame{
    private JPanel contentPane;
    private JMenu btnMenuInicio;
    private JButton btnNewSale;
    private JButton btnHistorialVentas;
    private JButton btnNuevoInventario;
    private JButton btnHistorialTransfers;
    private JButton btnActualizar;
    private JLabel lblCentro;
    private JLabel lblDerecha;
    private JLabel lblSucursal;
    private JSplitPane splitPane;
    private JPanel panelControles;
    private FlatToggleButton btnInventary;
    private FlatToggleButton btnSales;
    private JButton btnExit;
    private FlatToggleButton btnAlmacen;
    private FlatToggleButton btnManagement;
    private FlatToggleButton btnAjustes;
    private FlatToggleButton btnAdministrador;
    private JPanel panelMenus;
    private JLabel lblUsuario;
    private TabbedPane tabbedPane;
    private JPanel paneNotify;
    private JLabel lblNotify;
    private JPanel cPane;
    private FlatToggleButton btnTraslades;
    private FlatToggleButton btnReserves;
    private Propiedades propiedades;
    private MenuSales menuSales;
    private MenuInventory menuInventory;
    private MenuAlmacen menuAlmacen;
    private MenuManage menuManage;
    private MenuTraslade menuTraslade;
    private MenuReserves menuReserves;
    public static Vector<Stock> stocks;
    public static Vector<Branch> branchs;
    public static Vector<Branch> branchesWithAll;
    public static Vector<User> users;
    public static Vector<User> usersWithAll;
    public static Vector<Product> products;
    public static Vector<Category> categories;
    public static Vector<Category> categoriesWithAll;
    public static Vector<Color> colors;
    public static Vector<Color> colorsWithAll;
    public static Vector<Brand> brands;
    public static Vector<Brand> brandsWithAll;
    public static Vector<Size> sizes;
    public static Vector<Size> sizesWithAll;
    public static Vector<Sex> sexs;
    public static Vector<Sex> sexsWithAll;
    public static Vector<Style> styles;

    public FPrincipal(){
        init();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuSales();
            }
        });
        btnInventary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuInventory();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadNewSale();
            }
        });
        btnHistorialVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSales.loadRecordSales();
            }
        });
        paneNotify.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lblNotify.setVisible(!lblNotify.isVisible());
            }
        });
        btnAlmacen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMenuAlmacen();
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
    }
    private void exit(){
        FLogin fLogin =new FLogin();
        dispose();
        fLogin.setVisible(true);
    }
    private void loadMenuItems(){
        JMenuItem menuSettings=new JMenuItem("Configuraciones");
        JMenuItem menuCompany=new JMenuItem("Compañia");
        menuSettings.setIcon(new ImageIcon(App.class.getResource("Icons/x16/settings.png")));
        menuCompany.setIcon(new ImageIcon(App.class.getResource("Icons/x16/settings.png")));
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
        btnMenuInicio.add(menuSettings);
        btnMenuInicio.add(menuCompany);
    }
    private void loadSettings(){
        DSettings dSettings=new DSettings(this);
        dSettings.setVisible(true);
    }
    private void loadCompany(){
        DCompany dCompany=new DCompany();
        dCompany.setVisible(true);
    }
    private void loadMenuInventory() {
        splitPane.setRightComponent(menuInventory.getContentPane());
    }
    private void loadMenuAlmacen() {
        splitPane.setRightComponent(menuAlmacen.getContentPane());
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
    private void loadMenuReserves(){
        splitPane.setRightComponent(menuReserves.getContentPane());
    }

    private void init(){
        setContentPane(contentPane);
        setTitle("Software-Tienda");
        Utilities.setJFrame(this);
        Utilities.setTabbedPane(tabbedPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarCursores();
        pack();
        setLocationRelativeTo(null);
        loadLists();
        menuSales=new MenuSales(tabbedPane);
        menuInventory=new MenuInventory(tabbedPane);
        menuAlmacen=new MenuAlmacen(tabbedPane);
        menuManage=new MenuManage(tabbedPane);
        menuTraslade=new MenuTraslade(tabbedPane);
        menuReserves=new MenuReserves(tabbedPane);
        setExtendedState(MAXIMIZED_BOTH);
        loadMenuItems();
        loadMenuSales();
        menuSales.loadNewSale();
    }

    private void loadLists(){
        users=Users.getActives();
        stocks=Stocks.getTodos();
        branchs=Branchs.getActives();
        branchesWithAll=new Vector<>(branchs);
        branchesWithAll.add(0,new Branch("TODAS"));
        categories= Categorys.getActives();
        categoriesWithAll=new Vector<>(categories);
        categoriesWithAll.add(0,new Category("TODAS"));
        colors= Colors.getActives();
        colorsWithAll=new Vector<>(colors);
        colorsWithAll.add(0,new Color("TODOS"));
        sizes= Sizes.getActives();
        sizesWithAll=new Vector<>(sizes);
        sizesWithAll.add(0,new Size("TODAS"));
        brands=Brands.getActives();
        brandsWithAll=new Vector<>(brands);
        brandsWithAll.add(0,new Brand("TODAS"));
        sexs= Sexs.getActives();
        sexsWithAll=new Vector<>(sexs);
        sexsWithAll.add(0,new Sex("TODOS"));
        products= Products.getActives();
        styles=Styles.getTodos();
    }
    private void cargarCursores(){
        btnAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSales.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAlmacen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdministrador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewSale.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialTransfers.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorialVentas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTraslades.setCursor(new Cursor(Cursor.HAND_CURSOR));
        paneNotify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReserves.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        paneNotify=new FondoPanel("Icons/x32/notificacion.png",new Dimension(32,32));
        panelControles=new CustomPane(2);
        panelControles.updateUI();
        cPane=new CustomPane(2);
        cPane.updateUI();
        btnSales=new CToggleButton();
        btnInventary=new CToggleButton();
        btnAlmacen=new CToggleButton();
        btnManagement =new CToggleButton();
        btnAjustes=new CToggleButton();
        btnAdministrador=new CToggleButton();
        btnTraslades=new CToggleButton();
        btnReserves=new CToggleButton();
    }
}
